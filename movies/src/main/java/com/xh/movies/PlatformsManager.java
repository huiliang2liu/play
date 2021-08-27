package com.xh.movies;

import android.content.Context;

import com.xh.movies.platforms.AiKanMeiJu;
import com.xh.movies.platforms.BT4KYingYuan;
import com.xh.movies.platforms.CaoMinDianYing;
import com.xh.movies.platforms.DianYingMao;
import com.xh.movies.platforms.DianYingWang;
import com.xh.movies.platforms.FKYingShi;
import com.xh.movies.platforms.IQiYi;
import com.xh.movies.platforms.MGTV;
import com.xh.movies.platforms.MeiJuNiao;
import com.xh.movies.platforms.MeiJuTianTang;
import com.xh.movies.platforms.MeiJuWang;
import com.xh.movies.platforms.PPTV;
import com.xh.movies.platforms.RenRen;
import com.xh.movies.platforms.TenCent;
import com.xh.movies.platforms.UUMeiJu;
import com.xh.movies.platforms.WaiJuWang;
import com.xh.movies.platforms.WuKongMeiJu;
import com.xh.movies.platforms.XiGuaShiPin;
import com.xh.movies.platforms.YingShiDaQuan;
import com.xh.movies.platforms.YouKu;
import com.xh.movies.vip.VipImpl;
import com.xh.paser.IPlatform;
import com.xh.paser.IVip;

import java.util.ArrayList;
import java.util.List;

public class PlatformsManager {
    public static IVip vip;
    private static Context context;
    private static List<IPlatform> platformList;

    public static void init(Context c) {
        context = c.getApplicationContext();
        vip = new VipImpl(context.getApplicationContext());
        platformList = new ArrayList<>();
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
        platformList.add(new TenCent());
        platformList.add(new IQiYi());
        platformList.add(new MGTV());
        platformList.add(new YouKu());
        platformList.add(new PPTV());
    }

    public static List<IPlatform> getPlatforms() {
        return platformList;
    }

    public static IVip getVip() {
        return vip;
    }
}
