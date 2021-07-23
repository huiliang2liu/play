package com.xh.play.image.transform;

import android.graphics.Bitmap;

import com.xh.play.image.ImageUtil;

/**
 * 2018/7/4 10:42
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class RoundTransform implements ITransform {
    private float mRx;
    private float mRy;

    public RoundTransform() {
        this(0, 0);
    }

    public RoundTransform(float rx, float ry) {
        mRx = rx<=0?20:rx;
        mRy = ry<=0?20:ry;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.rounded_bitmap(bitmap,mRx,mRy);
    }
}
