package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip1717yuan extends AbsVip {
    public Vip1717yuan(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.1717yun.com/jx/ty.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
