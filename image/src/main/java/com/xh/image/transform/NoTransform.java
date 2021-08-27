package com.xh.image.transform;

import android.graphics.Bitmap;

/**
 * 2018/7/2 10:22
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class NoTransform implements ITransform {
    @Override
    public Bitmap transform(Bitmap bitmap) {
        return bitmap;
    }
}
