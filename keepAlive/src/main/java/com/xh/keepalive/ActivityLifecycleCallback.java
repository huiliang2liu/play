package com.xh.keepalive;

import android.app.Activity;
import android.app.Application;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.xh.base.log.Logger;
import com.xh.keepalive.component.DaemonService;
import com.xh.keepalive.utils.ServiceHolder;

import java.util.HashMap;
import java.util.Map;

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private static Map<Activity, ServiceConnection> connCache = new HashMap<>();
    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        Logger.v(Logger.TAG, String.format("====> [%s] created", activity.getLocalClassName()));
        ServiceHolder.getInstance().bindService(activity, DaemonService.class,
                new ServiceHolder.OnServiceConnectionListener() {
                    @Override
                    public void onServiceConnection(ServiceConnection connection, boolean isConnected) {
                        if (isConnected) {
                            connCache.put(activity, connection);
                        }
                    }
                });
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.v(Logger.TAG, String.format("====> [%s] started", activity.getLocalClassName()));
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.v(Logger.TAG, String.format("====> [%s] resumed", activity.getLocalClassName()));
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.v(Logger.TAG, String.format("====> [%s] paused", activity.getLocalClassName()));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.v(Logger.TAG, String.format("====> [%s] stopped", activity.getLocalClassName()));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.v(Logger.TAG, String.format("====> [%s] save instance state", activity.getLocalClassName()));
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.v(Logger.TAG, String.format("====> [%s] destroyed", activity.getLocalClassName()));
        if (connCache.containsKey(activity)) {
            ServiceHolder.getInstance().unbindService(activity, connCache.get(activity));
        }
    }
}
