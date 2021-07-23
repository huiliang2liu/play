package com.xh.play.image.transform;

import android.graphics.Bitmap;

import com.xh.play.image.ImageUtil;

/**
 * 2018/7/4 10:30
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class NostalgiaTransform implements ITransform {
    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.nostalgia(bitmap);
    }
}
