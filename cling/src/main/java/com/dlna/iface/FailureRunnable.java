package com.dlna.iface;

import com.dlna.listener.ControlListener;

public class FailureRunnable implements Runnable {
    private ControlListener listener;

    public FailureRunnable(ControlListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        listener.onControlFailure();
    }
}
