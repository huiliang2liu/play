package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip660e extends AbsVip {
    public Vip660e(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://660e.com/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
