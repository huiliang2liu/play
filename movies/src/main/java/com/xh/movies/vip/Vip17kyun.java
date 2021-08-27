package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class Vip17kyun extends AbsVip {
    public Vip17kyun(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://17kyun.com/api.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return url.contains("sf1-ttcdn-tos.pstatp.com");
    }
}
