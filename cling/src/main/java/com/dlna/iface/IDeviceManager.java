package com.dlna.iface;

import com.dlna.listener.DeviceChangeListener;

public interface IDeviceManager {
    void search(DeviceChangeListener listener);

    void cancel();

    IControl findDevice(String name);
}
