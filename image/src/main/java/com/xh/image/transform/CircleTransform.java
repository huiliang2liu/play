package com.xh.image.transform;

import android.graphics.Bitmap;

import com.xh.image.ImageUtil;


/**
 * 2018/7/4 10:47
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class CircleTransform implements ITransform {
    private int mX;
    private int mY;

    public CircleTransform() {
        this(0, 0);
    }

    public CircleTransform(int x, int y) {
        mX = x;
        mY = y;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        int width=bitmap.getWidth();
        int heigth=bitmap.getHeight();
        if(mX<=0||mX>=width||mY<=0||mY>=heigth)
            return  ImageUtil.circle_bitmap(bitmap);
        width=width-mX;
        heigth=heigth-mY;
        int r=width>heigth?heigth>>1:width<<1;
        return ImageUtil.circle_bitmap(bitmap,mX,mY,r);
    }
}
