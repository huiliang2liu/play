package com.xh.keepalive.component;

import android.content.Intent;
import android.os.IBinder;


import com.xh.base.utils.Utils;
import com.xh.keepalive.notification.NotifyResidentService;

public class DaemonService extends DaemonBaseService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        Utils.startService(this,NotifyResidentService.class);
        Utils.startService(this,AssistService1.class);
        Utils.startService(this,AssistService2.class);
        super.onCreate();
    }
}
