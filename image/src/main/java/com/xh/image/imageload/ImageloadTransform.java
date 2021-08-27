package com.xh.image.imageload;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.xh.image.transform.ITransform;

public class ImageloadTransform implements BitmapDisplayer {
    private ITransform transform;

    public ImageloadTransform(ITransform transform) {
        this.transform = transform;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(transform == null ? bitmap : transform.transform(bitmap));
    }
}
