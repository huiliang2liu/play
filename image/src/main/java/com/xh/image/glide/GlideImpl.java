package com.xh.image.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.xh.image.AImageLoad;
import com.xh.image.transform.ITransform;

import java.security.MessageDigest;

public class GlideImpl extends AImageLoad {
    private Context context;
    private Bitmap.Config config;
    private Glide glide;
    private GlideGlideModule glideGlideModule;
    private static final int CACHE_SIXE = 1024 * 1023 * 100;

    public GlideImpl(Bitmap.Config config, Context context, String path) {
        this.config = config;
        this.context = context;
        if (path == null || path.isEmpty())
            path = context.getExternalFilesDir(null).getParent() + "/glide";
        switch (config) {
            case ALPHA_8:
            case RGB_565:
            case HARDWARE:
            case RGBA_F16:
            case ARGB_4444:
                GlideGlideModule.decodeFormat = DecodeFormat.PREFER_RGB_565;
                break;
            case ARGB_8888:
                GlideGlideModule.decodeFormat = DecodeFormat.PREFER_ARGB_8888;
                break;

        }
        GlideGlideModule.mPath = path;
        GlideGlideModule.mSize = CACHE_SIXE;
        this.glide = Glide.get(context);
    }

    @Override
    public void load(int defaultImg, int error, String path, ImageView imageView, ITransform transform) {
        if (imageView == null)
            return;
        load(defaultImg, error, path, transform, new DrawableImageViewTarget(imageView));
    }

    @Override
    public void loadBg(int defaultImg, int error, String path, View view, ITransform transform) {
        if (view == null)
            return;
        load(defaultImg, error, path, transform, new BitmapViewTarget(view));
    }

    private void load(int defaultImg, int error, String path, ITransform transform, ViewTarget target) {
        RequestOptions options = new RequestOptions().error(error).placeholder(defaultImg).transform(new BitmapTransformation() {
            @NonNull

            @Override
            protected Bitmap transform(
                    @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return transform!=null?transform.transform(toTransform):toTransform;
            }

            @Override
            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                messageDigest.update(String.valueOf(hashCode()).getBytes());
            }
        });
        Glide.with(context).load(path).apply(options).into(target);
    }
}
