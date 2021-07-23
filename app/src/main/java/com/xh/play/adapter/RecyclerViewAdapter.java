package com.xh.play.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.xh.play.adapter.tag.ViewHolder;
import com.xh.play.widget.CardLayoutManager;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter implements IAdapter {
    private List<T> objects = new ArrayList<>();
    protected RecyclerView recyclerView;
    protected Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);
        recyclerView.setAdapter(this);
    }

    public void notifyDataSetChanged(int position) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            int first = linearLayoutManager.findFirstVisibleItemPosition();
            int last = linearLayoutManager.findLastVisibleItemPosition();
            if (position >= first && position <= last)
                notifyItemChanged(position);
        } else if (manager instanceof GridLayoutManager) {
            GridLayoutManager linearLayoutManager = (GridLayoutManager) manager;
            int first = linearLayoutManager.findFirstVisibleItemPosition();
            int last = linearLayoutManager.findLastVisibleItemPosition();
            if (position >= first && position <= last)
                notifyItemChanged(position);
        }
    }

    @Override
    public void setSelection(int position) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        manager.scrollToPosition(position);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new VH(inflater.inflate(getView(viewType), viewGroup, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH vh = (VH) viewHolder;
        vh.bindView(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public void addItem(Object o) {
        if (o == null)
            return;
        objects.add((T) o);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
            CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
            cardLayoutManager.resetSize();
        }
        notifyDataSetChanged();
    }


    @Override
    public void addItem(List list) {
        if (list == null || list.size() <= 0)
            return;
        objects.addAll(list);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
            CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
            cardLayoutManager.resetSize();
        }
        if (list.size() == objects.size())
            notifyDataSetChanged();
        else
            notifyDataSetChanged(objects.size() - list.size()-1);
    }

    @Override
    public void remove(Object o) {
        if (o == null)
            return;
        if (objects.remove(o)) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                cardLayoutManager.resetSize();
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(List list) {
        if (list == null || list.size() <= 0)
            return;
        if (objects.removeAll(list)) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                cardLayoutManager.resetSize();
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void clean() {
        objects.clear();
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
            CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
            cardLayoutManager.resetSize();
        }
        notifyDataSetChanged();
    }

    private class VH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ViewHolder viewHolder;
        private int position;

        public VH(@NonNull View itemView, int itemType) {
            super(itemView);
            viewHolder = getViewHolder(itemType);
            viewHolder.setAdapter(RecyclerViewAdapter.this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            viewHolder.setView(itemView);
        }

        public void bindView(int position) {
            this.position = position;
            viewHolder.setContext(position);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(VH.this.itemView, position, RecyclerViewAdapter.this.getItemId(position));
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null)
                return onItemLongClickListener.onItemLongClick(itemView, position, RecyclerViewAdapter.this.getItemId(position));
            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, long id);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position, long id);
    }

    public abstract ViewHolder<T> getViewHolder(int itemType);

    public abstract int getView(int itemType);
}
