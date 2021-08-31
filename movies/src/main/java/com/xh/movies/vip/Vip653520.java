package com.xh.movies.vip;

import android.content.Context;
import android.util.Log;

import com.xh.paser.AbsVip;

public class Vip653520 extends AbsVip {
    public Vip653520(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://api.653520.top/vip/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        Log.e("dadad", url);
        return false;
    }
}
