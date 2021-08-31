package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipSigujx extends AbsVip {
    public VipSigujx(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://api.sigujx.com/jx/?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
