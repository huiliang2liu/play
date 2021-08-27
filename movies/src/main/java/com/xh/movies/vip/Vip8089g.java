package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip8089g extends AbsVip {
    public Vip8089g(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.8090g.cn/jiexi/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
