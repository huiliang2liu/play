package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipPanGuJieXi extends AbsVip {
    public VipPanGuJieXi(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.pangujiexi.cc/jiexi.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
//        https://migu.vod.pptv.com/5059788a1fd4bd99fd67b6150f903e8f.mp4?type=tv.android&w=1&k=f40f3f0005dc8ec917a81e21a5ca9349-33cf-1630068969%26bppcataid%3D9
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
