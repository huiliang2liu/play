package com.xh.play.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.xh.play.image.transform.ITransform;


public abstract class AImageLoad implements IImageLoad {
    @Override
    public void load(String path, ImageView imageView, ITransform transform) {
        load(-1, -1, path, imageView, transform);
    }

    @Override
    public void loadBg(String path, View view, ITransform transform) {
        loadBg(-1, -1, path, view, transform);
    }

    public void load(int src, ImageView imageView, ITransform transform) {
        Bitmap bitmap = BitmapFactory.decodeResource(imageView.getResources(), src);
        imageView.setImageBitmap(transform.transform(bitmap));
    }

    public void loadBg(int src, View view, ITransform transform) {
        Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), src);
        if (Build.VERSION.SDK_INT < 16)
            view.setBackgroundDrawable(new BitmapDrawable(transform.transform(bitmap)));
        else
            view.setBackground(new BitmapDrawable(view.getResources(), transform.transform(bitmap)));
    }
}
