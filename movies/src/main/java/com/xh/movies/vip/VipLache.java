package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipLache extends AbsVip {
    public VipLache(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://jx.lache.me/cc/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
