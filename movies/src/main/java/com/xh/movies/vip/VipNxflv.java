package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipNxflv extends AbsVip {
    public VipNxflv(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.nxflv.com/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
