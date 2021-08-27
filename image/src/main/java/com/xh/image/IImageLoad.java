package com.xh.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.xh.image.glide.GlideImpl;
import com.xh.image.imageload.ImageloadImpl;
import com.xh.image.picasso.PicassoImpl;
import com.xh.image.transform.ITransform;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface IImageLoad {
    void load(int defaultImg, int error, String path, ImageView imageView, ITransform transform);

    void load(String path, ImageView imageView, ITransform transform);

    void loadBg(int defaultImg, int error, String path, View view, ITransform transform);

    void loadBg(String path, View view, ITransform transform);

    void load(int src, ImageView imageView, ITransform transform);
    void loadBg(int src, View view, ITransform transform);

    class Builder {
        private static final String TAG = "Builder";
        private Context context;
        private Bitmap.Config config = Bitmap.Config.RGB_565;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder config(Bitmap.Config config) {
            if (config != null)
                this.config = config;
            return this;
        }

        public IImageLoad build() {
            return (IImageLoad) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IImageLoad.class}, new InvocationHandler() {
                private IImageLoad imageLoad;

                {
                    try {
                        Class.forName("com.bumptech.glide.Glide");
                        imageLoad = new GlideImpl(config, context,"");
                        Log.e(TAG, "Glide");
                    } catch (ClassNotFoundException e) {
                        try {
                            Class.forName("com.squareup.picasso.Picasso");
                            imageLoad = new PicassoImpl(config, context);
                            Log.e(TAG, "Picasso");
                        } catch (ClassNotFoundException e1) {
                            try {
                                Class.forName("com.nostra13.universalimageloader.core.ImageLoader");
                                imageLoad = new ImageloadImpl(config, context, null);
                                Log.e(TAG, "ImageLoader");
                            } catch (ClassNotFoundException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }


                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(imageLoad, args);
                }
            });
        }
    }
}
