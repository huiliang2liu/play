package com.xh.movies.vip;

import android.content.Context;

import com.xh.paser.AbsVip;

public class VipJqaa extends AbsVip {
    public VipJqaa(Context context) {
        super(context);
    }

    @Override
    protected String base() {
        return "https://jqaaa.com/jx.php?url=";
    }

    @Override
    protected boolean isMoive(String url) {
        return false;
    }
}
