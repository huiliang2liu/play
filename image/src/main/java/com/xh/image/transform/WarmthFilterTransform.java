package com.xh.image.transform;

import android.graphics.Bitmap;

import com.xh.image.ImageUtil;

/**
 * 2018/7/4 11:19
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class WarmthFilterTransform implements ITransform {
    private float mCenterX;
    private float mCenterY;

    public WarmthFilterTransform() {
        this(0, 0);
    }

    public WarmthFilterTransform(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        int cX = 0, cY = 0;
        if (mCenterX < 0 || mCenterX > 1)
            cX = bitmap.getWidth() >> 1;
        else
            cX = (int) (bitmap.getWidth() * mCenterX);
        if (mCenterY < 0 || mCenterY > bitmap.getHeight())
            cY = bitmap.getHeight() >> 1;
        else
            cY = (int) (bitmap.getHeight() * mCenterY);
        return ImageUtil.warmthFilter(bitmap, cX, cY);
    }
}
