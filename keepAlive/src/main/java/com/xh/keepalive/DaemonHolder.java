package com.xh.keepalive;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.xh.base.log.Logger;
import com.xh.base.utils.RuntimeUtil;
import com.xh.base.utils.Utils;
import com.xh.keepalive.component.DaemonInstrumentation;
import com.xh.keepalive.component.DaemonReceiver;
import com.xh.keepalive.component.DaemonService;
import com.xh.keepalive.notification.NotifyResidentService;

public class DaemonHolder {

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            RuntimeUtil.exemptAll();
        }
    }


    private DaemonHolder() {
    }

    private static class Holder {
        private static volatile DaemonHolder INSTANCE = new DaemonHolder();
    }

    public static DaemonHolder getInstance() {
        return Holder.INSTANCE;
    }

    public void attach(Context base, Application app) {
//        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallback());

        JavaDaemon.getInstance().fire(
                base,
                new Intent(base, DaemonService.class),
                new Intent(base, DaemonReceiver.class),
                new Intent(base, DaemonInstrumentation.class)
        );

        KeepAliveConfigs configs = new KeepAliveConfigs(
                new KeepAliveConfigs.Config(base.getPackageName() + ":resident",
                        NotifyResidentService.class.getCanonicalName()));
//        configs.ignoreBatteryOptimization();
//        configs.rebootThreshold(10 * 1000, 3);
        configs.setOnBootReceivedListener(new KeepAliveConfigs.OnBootReceivedListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Logger.d(Logger.TAG, "############################# onReceive(): intent=" + intent);
                Utils.startService(context,DaemonService.class);
            }
        });
        KeepAlive.init(base, configs);
    }
}
