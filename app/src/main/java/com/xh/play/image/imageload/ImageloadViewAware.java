package com.xh.play.image.imageload;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.nostra13.universalimageloader.core.imageaware.ViewAware;

public class ImageloadViewAware extends ViewAware {
    public ImageloadViewAware(View view) {
        super(view);
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        View view = viewRef.get();
        if (view == null)
            return false;
        setImageBitmapInto(bitmap, view);
        return true;
    }

    @Override
    protected void setImageDrawableInto(Drawable drawable, View view) {
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT > 15)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    @Override
    protected void setImageBitmapInto(Bitmap bitmap, View view) {
        if (view == null)
            return;
        if (Build.VERSION.SDK_INT > 15)
            setImageDrawableInto(new BitmapDrawable(view.getResources(), bitmap), view);
        else
            setImageDrawableInto(new BitmapDrawable(bitmap), view);
    }
}
