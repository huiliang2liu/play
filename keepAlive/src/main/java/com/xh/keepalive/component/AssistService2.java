package com.xh.keepalive.component;

import android.content.Intent;
import android.os.IBinder;

import com.xh.keepalive.utils.ServiceHolder;

public class AssistService2 extends DaemonProcessService {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceHolder.getInstance().bindService(this, DaemonService.class, null);
    }
}
