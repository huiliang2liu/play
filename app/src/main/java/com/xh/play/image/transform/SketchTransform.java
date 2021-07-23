package com.xh.play.image.transform;

import android.graphics.Bitmap;
import com.xh.play.image.ImageUtil;

/**
 * 2018/7/4 10:39
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/

public class SketchTransform implements ITransform {
    @Override
    public Bitmap transform(Bitmap bitmap) {
        return ImageUtil.sketch(bitmap);
    }
}
