package com.xh.keepalive.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xh.base.log.Logger;
import com.xh.base.utils.Utils;

public class DaemonReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.v(Logger.TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! receiver: "
                + intent);
        Utils.startService(context,DaemonService.class);
    }
}
