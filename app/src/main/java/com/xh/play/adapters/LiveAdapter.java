package com.xh.play.adapters;


import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.paser.Detial;
import com.xh.play.R;
import com.xh.play.ViewHolder;

public class LiveAdapter extends RecyclerViewAdapter<Detial.DetailPlayUrl> {
    public LiveAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public ViewHolder<Detial.DetailPlayUrl> getViewHolder(int itemType) {
        return new Holder();
    }

    @Override
    public int getView(int itemType) {
        return R.layout.adapter_live;
    }

    private class Holder extends ViewHolder<Detial.DetailPlayUrl> {
        TextView textView;

        @Override
        public void setContext(Detial.DetailPlayUrl playUrl) {
            textView.setText(playUrl.title);
        }

        @Override
        public void bindView() {
            textView = findViewById(R.id.adapter_live_tv);
        }
    }
}
