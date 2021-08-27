package com.xh.keepalive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.xh.base.log.Logger;

public class KeepAliveService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bindDaemonService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private IBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = iBinder;
            Logger.i(Logger.TAG, "++++++++++++++++++++++++++++++++++++++++++++ " + iBinder);
            try {
                iBinder.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            binder = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (binder != null) {
                binder.unlinkToDeath(this, 0);
                binder = null;
            }
            bindDaemonService();
        }
    };

    protected void bindDaemonService() {
        if (binder != null)
            return;
        try {
            if (KeepAlive.client != null && KeepAlive.client.mConfigurations != null) {
                String processName = KeepAlive.getProcessName();
                if (processName == null) {
                    return;
                }

                KeepAliveConfigs configs = KeepAlive.client.mConfigurations;
                if (processName.startsWith(configs.PERSISTENT_CONFIG.processName)) {
                    Intent intent = new Intent();
                    ComponentName component = new ComponentName(getPackageName(),
                            configs.DAEMON_ASSISTANT_CONFIG.serviceName);
                    intent.setComponent(component);
                    bindService(intent, conn, BIND_AUTO_CREATE);
                } else if (processName.startsWith(configs.DAEMON_ASSISTANT_CONFIG.processName)) {
                    Intent intent = new Intent();
                    ComponentName component = new ComponentName(getPackageName(),
                            configs.PERSISTENT_CONFIG.serviceName);
                    intent.setComponent(component);
                    bindService(intent, conn, BIND_AUTO_CREATE);
                }
            }
        } catch (Throwable e) {
        }
    }
}
