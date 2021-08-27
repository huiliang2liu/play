package com.xh.play;

import android.view.View;

import com.xh.image.IImageLoad;

public abstract class ViewHolder<T> extends com.xh.base.adapter.tag.ViewHolder<T> {
    protected IImageLoad imageLoad;
    protected PlayApplication application;

    @Override
    public final void setView(View context) {
        super.setView(context);
        application = (PlayApplication) context.getContext().getApplicationContext();
        imageLoad = application.imageLoad;
    }
}
