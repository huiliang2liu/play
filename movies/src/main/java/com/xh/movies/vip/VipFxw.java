package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipFxw extends AbsVip {
    public VipFxw(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://vip.fxw.la/m3u8/index.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
