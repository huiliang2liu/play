package com.xh.play.image.transform;

import android.graphics.Bitmap;

import com.xh.play.image.ImageUtil;

/**
 * 2018/7/4 11:13
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class PolygonTransfrom implements ITransform {
    private int mVariable;

    public PolygonTransfrom() {
        this(5);
    }

    public PolygonTransfrom(int variable) {
        mVariable = variable <= 2 ? 5 : variable;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.polygon_bitmap(bitmap, mVariable);
    }
}
