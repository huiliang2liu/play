package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip1dior extends AbsVip {
    public Vip1dior(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://123.1dior.cn/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
