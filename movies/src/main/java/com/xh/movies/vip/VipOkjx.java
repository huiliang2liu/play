package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipOkjx extends AbsVip {
    public VipOkjx(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://okjx.cc/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
