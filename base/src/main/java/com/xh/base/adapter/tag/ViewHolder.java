package com.xh.base.adapter.tag;

import android.view.View;

import com.xh.base.adapter.IAdapter;


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

    public final void setAdapter(IAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public  void setView(View context) {
        this.context = context;
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