package com.xh.image.transform;

import android.graphics.Bitmap;

import com.xh.image.ImageUtil;

/**
 * 2018/7/4 11:15
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class StarTransform implements ITransform {
    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.star_bitmap(bitmap);
    }
}
