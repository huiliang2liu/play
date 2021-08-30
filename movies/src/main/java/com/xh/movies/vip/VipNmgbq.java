package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipNmgbq extends AbsVip {
    public VipNmgbq(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "http://5.nmgbq.com/2/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
