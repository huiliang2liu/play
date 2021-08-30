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
import com.xh.movies.vip.Vip1717yuan;
import com.xh.movies.vip.Vip17kyun;
import com.xh.movies.vip.Vip1dior;
import com.xh.movies.vip.Vip618g;
import com.xh.movies.vip.Vip660e;
import com.xh.movies.vip.Vip8089g;
import com.xh.movies.vip.VipBljiex;
import com.xh.movies.vip.VipFxw;
import com.xh.movies.vip.VipH8jx;
import com.xh.movies.vip.VipImpl;
import com.xh.movies.vip.VipJieXi;
import com.xh.movies.vip.VipJqaa;
import com.xh.movies.vip.VipJx;
import com.xh.movies.vip.VipMurl;
import com.xh.movies.vip.VipNmgbq;
import com.xh.movies.vip.VipOkjx;
import com.xh.movies.vip.VipPanGuJieXi;
import com.xh.movies.vip.VipWmxz;
import com.xh.movies.vip.Vipm1907;
import com.xh.paser.IPlatform;
import com.xh.paser.IVip;

import java.util.ArrayList;
import java.util.List;

public class PlatformsManager {
    public static IVip vip;
    private static Context context;
    private static List<IPlatform> platformList;
    public static int PORT = 8000;
    public static List<IVip> vips = new ArrayList<>();

    public static void init(Context c) {
        context = c.getApplicationContext();
        vips.add(new VipJx(context));
        vips.add(new Vip660e(context));
        vips.add(new Vip618g(context));
        vips.add(new Vip17kyun(context));
        vips.add(new Vipm1907(context));
        vips.add(new Vip8089g(context));
        vips.add(new VipPanGuJieXi(context));
        vips.add(new VipJieXi(context));
        vips.add(new VipH8jx(context));
        vips.add(new VipOkjx(context));
        vips.add(new Vip1717yuan(context));
        vips.add(new Vip1dior(context));
        vips.add(new VipMurl(context));
        vips.add(new VipJqaa(context));
        vips.add(new VipNmgbq(context));
        vips.add(new VipWmxz(context));
        vips.add(new VipBljiex(context));
        vips.add(new VipFxw(context));
        vip = new VipImpl(vips);
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

    public static List<IVip> getVips() {
        return vips;
    }

    public static void setPort(int port) {
        PORT = port;
    }
}
