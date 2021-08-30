package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipWmxz extends AbsVip {
    public VipWmxz(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "http://www.wmxz.wang/video.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
