package com.xh.keepalive.component;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.xh.base.log.Logger;
import com.xh.keepalive.IMonitorService;

public abstract class DaemonBaseService extends Service {

    private IMonitorService.Stub binder = new IMonitorService.Stub() {
        @Override
        public void processMessage(Bundle bundle) throws RemoteException {
            DaemonBaseService.this.processMessage(bundle);
        }
    };

    private void processMessage(Bundle bundle) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Notification noti = NotificationUtil.createNotification(
//                this,
//                0,
//                null,
//                null,
//                null
//        );
//        NotificationUtil.showNotification(this, noti);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(Logger.TAG, "############### intent: " + intent + ", startId: " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
