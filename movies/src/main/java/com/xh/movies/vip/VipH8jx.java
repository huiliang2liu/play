package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipH8jx extends AbsVip {
    public VipH8jx(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.h8jx.com/jiexi.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
