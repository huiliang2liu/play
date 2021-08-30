package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vipm1907 extends AbsVip {
    public Vipm1907(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://z1.m1907.cn/?jx=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
