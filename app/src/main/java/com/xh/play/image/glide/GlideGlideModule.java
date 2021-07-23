package com.xh.play.image.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;


/**
 * 2018/7/3 18:46
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
 public class GlideGlideModule implements GlideModule {
    public static  int mSize;
    public static DecodeFormat decodeFormat;
    public static String mPath;

//    public GlideGlideModule(int size, String path, Bitmap.Config bitmapConfig) {
//        switch (bitmapConfig) {
//            case ALPHA_8:
//            case RGB_565:
//            case HARDWARE:
//            case RGBA_F16:
//            case ARGB_4444:
//                decodeFormat = DecodeFormat.PREFER_RGB_565;
//                break;
//            case ARGB_8888:
//                decodeFormat = DecodeFormat.PREFER_ARGB_8888;
//                break;
//
//        }
//        mPath = path;
//        mSize = size;
//        Log.e("path",path);
//    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(mPath,  mSize));
//        builder.setDecodeFormat(decodeFormat);
    }

//    @Override
//    public void registerComponents(Context context, Glide glide) {
//    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
