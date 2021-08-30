package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipMurl extends AbsVip {
    public VipMurl(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "http://www.murl.us/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
