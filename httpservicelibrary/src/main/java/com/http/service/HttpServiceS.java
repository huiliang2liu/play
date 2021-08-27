package com.http.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class HttpServiceS extends Service {
    private HttpService service;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                super.run();
                service = new com.http.service.Service(getApplication(), HttpServiceProvider.getHostname(), HttpServiceProvider.getPort());
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (service != null) {
            service.stop();
            service = null;
        }
    }
}
