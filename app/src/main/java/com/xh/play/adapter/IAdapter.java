package com.xh.play.adapter;


import java.util.List;

public interface IAdapter<T> {
    T getItem(int position);

    void addItem(T t);

    void addItem(List<T> ts);

    void remove(T t);

    void remove(List<T> ts);

    void clean();

    int getCount();

    void notifyDataSetChanged();

    void notifyDataSetChanged(int position);

    void setSelection(int position);

}
