package com.xh.play.image.transform;

import android.graphics.Bitmap;

import com.xh.play.image.ImageUtil;


/**
 * 2018/7/4 11:09
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class HeartTransform implements ITransform {




    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.heart_bitmap(bitmap);
    }
}
