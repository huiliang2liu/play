package com.xh.keepalive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import com.xh.base.log.Logger;
import com.xh.base.utils.Utils;
import com.xh.keepalive.component.DaemonService;
import com.xh.keepalive.scheduler.FutureScheduler;
import com.xh.keepalive.scheduler.SingleThreadFutureScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaDaemon {
    private static final String COLON_SEPARATOR = ":";
    private static volatile FutureScheduler futureScheduler;

    private JavaDaemon() {
        if (futureScheduler == null) {
            synchronized (JavaDaemon.class) {
                if (futureScheduler == null) {
                    futureScheduler = new SingleThreadFutureScheduler(
                            "javadaemon-holder",
                            true
                    );
                }
            }
        }
    }

    private static class Holder {
        private static volatile JavaDaemon INSTANCE = new JavaDaemon();
    }

    public static JavaDaemon getInstance() {
        return Holder.INSTANCE;
    }

    public void fire(Context context, Intent intent, Intent intent2, Intent intent3) {
        DaemonEnv env = new DaemonEnv();
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        env.publicSourceDir = applicationInfo.publicSourceDir;
        env.nativeLibraryDir = applicationInfo.nativeLibraryDir;
        env.intent = intent;
        env.intent2 = intent2;
        env.intent3 = intent3;
        env.processName = Utils.getProcessName();

        String[] strArr = {"daemon", "assist1", "assist2"};
        fire(context, env, strArr);
    }

    private void fire(Context context, DaemonEnv env, String[] strArr) {
        Logger.i(Logger.TAG, "############################################## !!! fire(): " +
                "env=" + env + ", strArr=" + Arrays.toString(strArr));
        boolean z;
        String processName = Utils.getProcessName();
        Logger.v(Logger.TAG, "processName: " + processName);
        Logger.v(Logger.TAG, "packageName: " + context.getPackageName());
        if (processName.startsWith(context.getPackageName()) && processName.contains(COLON_SEPARATOR)) {
            String substring = processName.substring(processName.lastIndexOf(COLON_SEPARATOR) + 1);
            List<String> list = new ArrayList();
            if (strArr != null) {
                z = false;
                for (String str : strArr) {
                    if (str.equals(substring)) {
                        z = true;
                    } else {
                        list.add(str);
                    }
                }
            } else {
                z = false;
            }
            if (z) {
                Logger.v(Logger.TAG, "app lock file start: " + substring);
                NativeKeepAlive.lockFile(context.getFilesDir() + "/" + substring + "_d");
                Logger.v(Logger.TAG, "app lock file finish");
                String[] strArr2 = new String[list.size()];
                for (int i = 0; i < strArr2.length; i++) {
                    strArr2[i] = context.getFilesDir() + "/" + list.get(i) + "_d";
                }
                futureScheduler.scheduleFuture(new AppProcessRunnable(env, strArr2, "daemon"), 0);
            }
        } else if (processName.equals(context.getPackageName())) {
            Utils.startBindService(context, DaemonService.class);
//            ContextCompat.startForegroundService(context, new Intent(context, DaemonService.class));
        }
    }
}
