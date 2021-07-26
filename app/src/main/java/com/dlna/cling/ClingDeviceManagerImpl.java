package com.dlna.cling;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.dlna.iface.IControl;
import com.dlna.iface.IPlayControl;
import com.dlna.iface.IDeviceManager;
import com.dlna.listener.DeviceChangeListener;
import com.dlna.util.PlayControlImpl;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClingDeviceManagerImpl implements IDeviceManager, RegistryListener, ServiceConnection {
    private static final DeviceType DMR_DEVICE_TYPE = new UDADeviceType("MediaRenderer");
    private Context context;
    private DeviceChangeListener deviceChangeListener;
    private AndroidUpnpService upnpService;
    private Handler handler = new Handler(Looper.myLooper());

    public ClingDeviceManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void search(DeviceChangeListener listener) {
        deviceChangeListener = listener;
        context.bindService(new Intent(context, AndroidUpnpServiceImpl.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void cancel() {
        context.unbindService(this);
    }

    @Override
    public IControl findDevice(String name) {
        Collection<Device> devices = upnpService.getRegistry().getDevices(DMR_DEVICE_TYPE);
        if (devices == null || devices.size() <= 0)
            return null;
        for (Device device1 : devices) {
            if (device1.getDetails().getFriendlyName().equals(name)) {
                return new ClingControlImpl(device1, upnpService);
            }
        }
        return null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        upnpService = (AndroidUpnpService) service;
        upnpService.getRegistry().addListener(this);
        Collection<Device> devices = upnpService.getRegistry().getDevices(DMR_DEVICE_TYPE);
        for (Device device : devices) {
            deviceAdded(device);
        }
        upnpService.getControlPoint().search();
    }


    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (upnpService != null)
            upnpService.getRegistry().removeListener(this);
        upnpService = null;
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {

    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception ex) {
        deviceRemoved(device);
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {

    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    @Override
    public void beforeShutdown(Registry registry) {

    }

    @Override
    public void afterShutdown() {

    }

    private void deviceAdded(Device device) {
        if (upnpService == null)
            return;
        Collection<Device> devices = upnpService.getRegistry().getDevices(DMR_DEVICE_TYPE);
        if (devices == null || devices.size() <= 0)
            return;
        final List<String> strings = new ArrayList<>(devices.size());
        for (Device device1 : devices)
            strings.add(device1.getDetails().getFriendlyName());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (deviceChangeListener != null)
                    deviceChangeListener.onChange(strings);
            }
        });
    }

    private void deviceRemoved(Device device) {
        deviceAdded(device);
    }
}
