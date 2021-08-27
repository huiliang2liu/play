package com.xh.keepalive.notification;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.xh.base.log.Logger;
import com.xh.keepalive.KeepAliveService;
import com.xh.keepalive.component.DaemonService;
import com.xh.keepalive.utils.ServiceHolder;

public class NotifyResidentService extends KeepAliveService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(Logger.TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ " +
                "intent: " + intent + ", startId: " + startId);

//        Notification noti = NotificationUtil.createNotification(
//                this,
//                intent.getIntExtra("noti_icon", 0),
//                intent.getStringExtra("noti_title"),
//                intent.getStringExtra("noti_text"),
//                intent.getStringExtra("noti_activity")
//        );
//        NotificationUtil.showNotification(this, noti);

        ServiceHolder.getInstance().bindService(this, DaemonService.class, null);
        return super.onStartCommand(intent, flags, startId);
    }
}
