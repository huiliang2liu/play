package com.xh.image.transform;

import android.graphics.Bitmap;

import com.xh.image.ImageUtil;

/**
 * 2018/7/4 10:37
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class SharpenTransform implements ITransform {
    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.sharpen(bitmap);
    }
}
