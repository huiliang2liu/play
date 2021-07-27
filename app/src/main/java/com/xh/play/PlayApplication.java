package com.xh.play;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.xh.play.image.IImageLoad;
import com.xh.play.platforms.BT4KYingYuan;
import com.xh.play.platforms.CaoMinDianYing;
import com.xh.play.platforms.DianYingMao;
import com.xh.play.platforms.DianYingWang;
import com.xh.play.platforms.FKYingShi;
import com.xh.play.platforms.IPlatform;
import com.xh.play.platforms.AiKanMeiJu;
import com.xh.play.platforms.MeiJuNiao;
import com.xh.play.platforms.MeiJuTianTang;
import com.xh.play.platforms.MeiJuWang;
import com.xh.play.platforms.RenRen;
import com.xh.play.platforms.UUMeiJu;
import com.xh.play.platforms.WaiJuWang;
import com.xh.play.platforms.WuKongMeiJu;
import com.xh.play.platforms.XiGuaShiPin;
import com.xh.play.platforms.YingShiDaQuan;

import java.util.ArrayList;
import java.util.List;

public class PlayApplication extends Application {
    public IImageLoad imageLoad;
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
        platformList.add(new WaiJuWang());
        platformList.add(new DianYingWang());
        platformList.add(new UUMeiJu());
        platformList.add(new FKYingShi());
        platformList.add(new WuKongMeiJu());
        platformList.add(new AiKanMeiJu());
        platformList.add(new MeiJuWang());
        platformList.add(new YingShiDaQuan());
        platformList.add(new RenRen());
        platformList.add(new MeiJuTianTang());
        platformList.add(new MeiJuNiao());
        platformList.add(new DianYingMao());
        platformList.add(new BT4KYingYuan());
        platformList.add(new XiGuaShiPin());
        platformList.add(new CaoMinDianYing());
    }
}
