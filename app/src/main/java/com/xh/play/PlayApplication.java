package com.xh.play;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;
import com.xh.base.log.AndroidLog;
import com.xh.base.log.Logger;
import com.xh.image.IImageLoad;
import com.xh.paser.IPlatform;
import com.xh.paser.IVip;
import com.xh.play.entities.IP;
import com.xh.play.entities.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PlayApplication extends Application {
    public IImageLoad imageLoad;

    public HttpManager httpManager;
    public IP ip;
    public Update update;
    public boolean splash = false;
    public List<IPlatform> platforms;
    public IVip vip;
    public List<IVip> vips = new ArrayList<>();
    public SharedPreferences cache;
    private Class PlatformsManager;
    private int port = -1;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Logger.addLog(new AndroidLog());
        MultiDex.install(this);
        Log.e("PlayApplication", "attachBaseContext");
//        DaemonHolder.getInstance().attach(base, this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("PlayApplication", "onCreate");
        CrashReport.initCrashReport(getApplicationContext(), "d5bed07280", false);
        imageLoad = new IImageLoad.Builder().context(this).build();
        httpManager = new HttpManager(this);
        cache = getSharedPreferences("cache", Context.MODE_PRIVATE);
    }

    public synchronized void setPlatformsManager(Class clazz) {
        PlatformsManager = clazz;
        try {
            Method method = clazz.getDeclaredMethod("init", Context.class);
            method.invoke(null, this);
            method = clazz.getDeclaredMethod("getPlatforms");
            platforms = (List<IPlatform>) method.invoke(null);
            method = clazz.getDeclaredMethod("getVip");
            vip = (IVip) method.invoke(null);
            method = clazz.getDeclaredMethod("getVips");
            List<IVip> vs = (List) method.invoke(null);
            if (vs != null && vs.size() > 0)
                vips.addAll(vs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        port();
    }

    public synchronized void setPort(int port) {
        this.port = port;
        port();
    }

    private void port() {
        if (PlatformsManager == null)
            return;
        if (port == -1)
            return;
        try {
            Method method = PlatformsManager.getDeclaredMethod("setPort", int.class);
            method.invoke(null, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
