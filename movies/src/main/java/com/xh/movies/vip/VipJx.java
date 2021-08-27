package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipJx extends AbsVip {
    public VipJx(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://jx.m3u8.tv/jiexi/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
