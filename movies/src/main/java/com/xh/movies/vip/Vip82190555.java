package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip82190555 extends AbsVip {
    public Vip82190555(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "http://www.82190555.com/index/qqvod.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
