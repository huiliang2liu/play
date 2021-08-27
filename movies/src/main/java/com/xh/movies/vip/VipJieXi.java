package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipJieXi extends AbsVip {
    public VipJieXi(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://api.jiexi.la/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
