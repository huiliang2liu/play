package com.xh.movies;

import android.content.Context;

import com.xh.movies.platforms.*;
import com.xh.movies.vip.*;
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
        vips.add(new VipPanGuJieXi(context));
        vips.add(new VipMw0(context));
        vips.add(new VipJieXi(context));
        vips.add(new VipH8jx(context));
        vips.add(new VipOkjx(context));
        vips.add(new Vip1717yuan(context));
        vips.add(new Vipm1907(context));
        vips.add(new Vip8089g(context));
        vips.add(new VipWmxz(context));
        vips.add(new Vip82190555(context));
        vips.add(new VipAdministratorw(context));
        vips.add(new Vip660e(context));
        vips.add(new VipNxflv(context));
        vips.add(new Vip618g(context));
        vips.add(new Vip17kyun(context));
        vips.add(new Vip1dior(context));
        vips.add(new VipMurl(context));
        vips.add(new VipNmgbq(context));
        vips.add(new VipBljiex(context));
        vips.add(new VipFxw(context));
        vips.add(new Vip653520(context));
        vips.add(new VipFlvsp(context));
        vips.add(new VipLache(context));
        vips.add(new VipSigujx(context));
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
