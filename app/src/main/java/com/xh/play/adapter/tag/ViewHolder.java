package com.xh.play.adapter.tag;

import android.view.View;

import com.xh.play.PlayApplication;
import com.xh.play.adapter.IAdapter;
import com.xh.play.image.IImageLoad;


/**
 * com.tvblack.lamp.adapter.tag
 * 2019/2/18 16:40
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public abstract class ViewHolder<T> {
    protected IAdapter<T> adapter;
    protected int position = -1;
    protected View context;
    protected IImageLoad imageLoad;

    public final void setAdapter(IAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public final void setView(View context) {
        this.context = context;
        imageLoad = ((PlayApplication) context.getContext().getApplicationContext()).imageLoad;
        bindView();
    }

    public final void setContext(int position) {
        this.position = position;
        setContext(adapter.getItem(position));
    }

    public final <T extends View> T findViewById(int viewId) {
        return context.findViewById(viewId);
    }

    public abstract void setContext(T t);

    public abstract void bindView();
}