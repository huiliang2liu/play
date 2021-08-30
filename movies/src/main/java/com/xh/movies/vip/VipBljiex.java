package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipBljiex extends AbsVip {
    public VipBljiex(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://vip.bljiex.com/?v=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
