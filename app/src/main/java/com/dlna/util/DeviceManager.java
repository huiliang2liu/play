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

public class DeviceManager implements IDeviceManager {
    private static final String TAG = "DeviceManager";
    private Context context;

    private IDeviceManager deviceManager;

    public DeviceManager(Context context) {
        this.context = context.getApplicationContext();
        deviceManager = (IDeviceManager) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IDeviceManager.class}, new InvocationHandler() {
            IDeviceManager deviceManager = new ClingDeviceManagerImpl(DeviceManager.this.context);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(deviceManager, args);
            }
        });
    }

    @Override
    public void search(DeviceChangeListener listener) {
        deviceManager.search(listener);
    }

    @Override
    public void cancel() {
        deviceManager.cancel();
    }

    @Override
    public IControl findDevice(String name) {
        return deviceManager.findDevice(name);
    }

    public IPlayControl findPlayControl(String name) {
        return new PlayControlImpl(findDevice(name));
    }
}
