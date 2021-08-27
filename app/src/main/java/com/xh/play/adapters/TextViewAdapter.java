package com.xh.play.adapters;

import android.widget.TextView;

import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.play.R;
import com.xh.play.ViewHolder;
import com.xh.play.entities.TextViewData;

import androidx.recyclerview.widget.RecyclerView;

public class TextViewAdapter extends RecyclerViewAdapter<TextViewData> {
    public TextViewAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public ViewHolder<TextViewData> getViewHolder(int itemType) {
        return new Holder();
    }

    @Override
    public int getView(int itemType) {
        return R.layout.text_view;
    }

    public static class Holder extends ViewHolder<TextViewData> {
        public TextView tv;

        @Override
        public void setContext(TextViewData textViewData) {
            tv.setText(textViewData.name);
        }

        @Override
        public void bindView() {
            tv = findViewById(R.id.text_view_tv);
        }
    }
}
