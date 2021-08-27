package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip618g extends AbsVip {
    public Vip618g(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://jx.618g.com/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
