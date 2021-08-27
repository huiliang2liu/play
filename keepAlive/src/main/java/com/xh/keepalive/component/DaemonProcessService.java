package com.xh.keepalive.component;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xh.base.log.Logger;

public class DaemonProcessService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v(Logger.TAG, "onCreate");
    }
}
