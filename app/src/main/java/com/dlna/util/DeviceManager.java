package com.dlna.util;

import android.content.Context;

import com.dlna.cling.ClingDeviceManagerImpl;
import com.dlna.cybergarage.CybergarageDeviceManagerImpl;
import com.dlna.iface.IControl;
import com.dlna.iface.IPlayControl;
import com.dlna.iface.IDeviceManager;
import com.dlna.listener.DeviceChangeListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DeviceManager implements IDeviceManager {
    private static final String TAG = "DeviceManager";
    private Context context;
    private List<IDeviceManager> deviceManagers = new ArrayList<>();


    public DeviceManager(Context context) {
        this.context = context.getApplicationContext();
        deviceManagers.add(new ClingDeviceManagerImpl(context));
        deviceManagers.add(new CybergarageDeviceManagerImpl(context));
    }


    @Override
    public void search(DeviceChangeListener listener) {
        for (IDeviceManager deviceManager : deviceManagers)
            deviceManager.search(listener);
    }

    @Override
    public void cancel() {
        for (IDeviceManager deviceManager : deviceManagers)
            deviceManager.cancel();
    }

    @Override
    public IControl findDevice(String name) {
        for (IDeviceManager deviceManager : deviceManagers) {
            IControl control = deviceManager.findDevice(name);
            if (control != null)
                return control;
        }
        return null;
    }

    public IPlayControl findPlayControl(String name) {
        return new PlayControlImpl(findDevice(name));
    }
}
