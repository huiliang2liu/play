package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipMw0 extends AbsVip {
    public VipMw0(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://jx.mw0.cc/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
