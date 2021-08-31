package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipFlvsp extends AbsVip {
    public VipFlvsp(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://api.flvsp.com/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
