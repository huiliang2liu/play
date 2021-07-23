package com.xh.play.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;


import com.xh.play.adapter.tag.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class Adapter<T> extends BaseAdapter implements IAdapter {
    private static final String TAG = "Adapter";
    private List<T> objects;
    protected Context context;
    public AdapterView adapterView;
    private LayoutInflater inflater;

    public Adapter(AdapterView adapterView) {
        // TODO Auto-generated constructor stub
        objects = new ArrayList<T>();
        context = adapterView.getContext();
        inflater = LayoutInflater.from(context);
        this.adapterView = adapterView;
        adapterView.setAdapter(this);
    }

    public void notifyDataSetChanged(int position) {
        int first = adapterView.getFirstVisiblePosition();
        int last = adapterView.getLastVisiblePosition();
        if (position >= first && position <= last) {
            View item = adapterView.getChildAt(position - first);
            ViewHolder<T> baseTag = (ViewHolder<T>) item.getTag();
            baseTag.setContext(position);
        }
    }

    @Override
    public void setSelection(int position) {
        adapterView.setSelection(position);
    }

    public void addItem(Object t) {
        if (t == null)
            return;
        Log.e(TAG, "addItem");
        objects.add((T) t);
        notifyDataSetChanged();
    }

    public void addItem(List ts) {
        if (ts == null || ts.size() <= 0)
            return;
        Log.e(TAG, "addItem");
        objects.addAll(ts);
        notifyDataSetChanged();
    }

    public void remove(Object t) {
        if (t == null)
            return;
        if (objects.remove(t))
            notifyDataSetChanged();
    }

    public void remove(List ts) {
        if (ts == null || ts.size() <= 0)
            return;
        if (objects.removeAll(ts))
            notifyDataSetChanged();
    }

    public void clean() {
        objects.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return super.getViewTypeCount();
    }

    @Override
    public T getItem(int arg0) {
        // TODO Auto-generated method stub
        return objects.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return getItem(arg0).hashCode();
    }

    @Override
    public View getView(int position, View converView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        int itemType = getItemViewType(position);
        if (converView == null) {
            Log.e(TAG, "converView is null");
            converView = inflater.inflate(getView(itemType), null);
        }
        if (converView == null)
            return null;
        ViewHolder<T> baseTag = (ViewHolder<T>) converView.getTag();
        if (baseTag == null) {
            baseTag = getViewHolder(itemType);
            baseTag.setAdapter(this);
            baseTag.setView(converView);
            converView.setTag(baseTag);
        }
        baseTag.setContext(position);
        return converView;
    }

    public abstract ViewHolder<T> getViewHolder(int itemType);

    public abstract int getView(int itemType);
}

