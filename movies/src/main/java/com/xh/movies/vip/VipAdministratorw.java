package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipAdministratorw extends AbsVip {
    public VipAdministratorw(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://www.administratorw.com/video.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
