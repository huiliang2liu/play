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

import java.util.List;

public class PlayApplication extends Application {
    public IImageLoad imageLoad;

    public HttpManager httpManager;
    public IP ip;
    public Update update;
    public boolean splash = false;
    public List<IPlatform> platforms;
    public IVip vip;
    public SharedPreferences cache;

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
        cache = getSharedPreferences("cache",Context.MODE_PRIVATE);
    }
}
