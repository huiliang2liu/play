package com.dlna.iface;

import com.dlna.listener.ControlListener;

public class SuccessRunnable implements Runnable {
    private ControlListener listener;

    public SuccessRunnable(ControlListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onControlSuccess();
    }
}
