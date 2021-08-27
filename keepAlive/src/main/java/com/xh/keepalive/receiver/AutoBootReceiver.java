package com.xh.keepalive.receiver;

import android.content.Context;
import android.content.Intent;

import com.xh.keepalive.KeepAliveConfigs;
import com.xh.keepalive.component.DaemonReceiver;

public class AutoBootReceiver extends DaemonReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (KeepAliveConfigs.bootReceivedListener != null) {
            KeepAliveConfigs.bootReceivedListener.onReceive(context, intent);
        }
    }
}
