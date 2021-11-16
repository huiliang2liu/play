package com.xh.image.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.xh.image.transform.ITransform;

import java.security.MessageDigest;

class GlideBitmapTransformation extends BitmapTransformation {
    private static final String ID = "com.xh.image.glide.GlideBitmapTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    ITransform transform;

    GlideBitmapTransformation(ITransform transform) {
        this.transform = transform;
    }

    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return transform.transform(toTransform);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        messageDigest.update(ID_BYTES);
    }
}
