package com.xh.play.image.transform;

import android.graphics.Bitmap;

import com.xh.play.image.ImageUtil;

/**
 * 2018/7/4 10:33
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class SunshineTransform implements ITransform {
    private float mLigthX;
    private float mLigthY;

    public SunshineTransform() {
        this(0, 0);
    }

    public SunshineTransform(float ligthX, float ligthY) {
        mLigthX = ligthX;
        mLigthY = ligthY;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        int sx, sy;
        if (mLigthX < 0 || mLigthX > 1)
            sx = bitmap.getWidth() >> 1;
        else
            sx = (int) (bitmap.getWidth() * mLigthX);
        if (mLigthY < 0 || mLigthY > 1)
            sy = bitmap.getHeight() >> 1;
        else
            sy = (int) (bitmap.getHeight() * mLigthY);
        return ImageUtil.sunshine(bitmap, sx, sy);
    }
}
