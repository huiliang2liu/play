package com.dlna.cybergarage;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dlna.iface.IControl;
import com.dlna.iface.IDeviceManager;
import com.dlna.listener.DeviceChangeListener;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.ssdp.SSDPPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CybergarageDeviceManagerImpl implements IDeviceManager, NotifyListener, SearchResponseListener, org.cybergarage.upnp.device.DeviceChangeListener {
    private static final String TAG = "Cybergarage";
    private ControlPoint controlPoint;
    private Map<String, Device> deviceMap;
    private DeviceChangeListener listener;
    private Handler handler = new Handler(Looper.myLooper());

    public CybergarageDeviceManagerImpl(Context context) {
        controlPoint = new ControlPoint();
        controlPoint.addNotifyListener(this);
        controlPoint.addSearchResponseListener(this);
        controlPoint.addDeviceChangeListener(this);
        deviceMap = new HashMap<>();

    }

    @Override
    public void search(DeviceChangeListener listener) {
        this.listener = listener;
        new Thread() {
            @Override
            public void run() {
                controlPoint.start();
                controlPoint.search();
            }
        }.start();
    }

    @Override
    public void cancel() {
        new Thread() {
            @Override
            public void run() {
                controlPoint.stop();
            }
        }.start();
    }

    @Override
    public IControl findDevice(String name) {
        if (deviceMap.containsKey(name)) {
            return new CybergarageControlImpl(deviceMap.get(name));
        }
        return null;
    }

    @Override
    public void deviceNotifyReceived(SSDPPacket ssdpPacket) {
        Log.i(TAG, "Got Notification from device, remoteAddress is" + ssdpPacket.getRemoteAddress());
    }

    @Override
    public void deviceSearchResponseReceived(SSDPPacket ssdpPacket) {
        Log.i(TAG, "A new device was searched, remoteAddress is" + ssdpPacket.getRemoteAddress());
    }

    @Override
    public void deviceRemoved(Device dev) {
        Log.i(TAG, "Device was removed, device name: " + dev.getFriendlyName());
        if (deviceMap.containsKey(dev.getFriendlyName()))
            deviceMap.remove(dev.getFriendlyName());
        update();
    }

    @Override
    public void deviceAdded(Device dev) {
        Log.i(TAG, "Device was added, device name:" + dev.getFriendlyName());
        if (isMediaRenderDevice(dev)) {
            deviceMap.put(dev.getFriendlyName(), dev);
            update();
        }
    }

    private void update() {
        if (deviceMap.size() < 0)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<String> strings = new ArrayList<>();
                strings.addAll(deviceMap.keySet());
                if (listener != null)
                    listener.onChange(strings);
            }
        });
    }

    public static boolean isMediaServerDevice(Device device) {
        if ("urn:schemas-upnp-org:device:MediaServer:1".equalsIgnoreCase(device.getDeviceType())) {
            return true;
        }
        return false;
    }


    public static boolean isMediaRenderDevice(Device device) {
        if ("urn:schemas-upnp-org:device:MediaRenderer:1".equalsIgnoreCase(device.getDeviceType())) {
            return true;
        }
        return false;
    }
}
