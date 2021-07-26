package com.xh.play;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.xh.play.image.IImageLoad;
import com.xh.play.platforms.DianYingMao;
import com.xh.play.platforms.DianYingWang;
import com.xh.play.platforms.FKYingShi;
import com.xh.play.platforms.IPlatform;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.platforms.AiKanMeiJu;
import com.xh.play.platforms.MeiJuNiao;
import com.xh.play.platforms.MeiJuTianTang;
import com.xh.play.platforms.MeiJuWang;
import com.xh.play.platforms.RenRen;
import com.xh.play.platforms.UUMeiJu;
import com.xh.play.platforms.WaiJuWang;
import com.xh.play.platforms.WuKongMeiJu;
import com.xh.play.platforms.YingShiDaQuan;

import java.util.ArrayList;
import java.util.List;

public class PlayApplication extends Application {
    public IImageLoad imageLoad;
    public List<IPlatforms> platforms =new ArrayList<>();
    public List<IPlatform> platformList =new ArrayList<>();
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imageLoad = new IImageLoad.Builder().context(this).build();
        platforms.add(new AiKanMeiJu());
        platforms.add(new MeiJuWang());
        platforms.add(new YingShiDaQuan());
        platforms.add(new RenRen());
        platforms.add(new MeiJuTianTang());
        platforms.add(new MeiJuNiao());
        platforms.add(new DianYingMao());
        platformList.add(new WaiJuWang());
        platformList.add(new DianYingWang());
        platformList.add(new UUMeiJu());
        platformList.add(new FKYingShi());
        platformList.add(new WuKongMeiJu());
    }
}
