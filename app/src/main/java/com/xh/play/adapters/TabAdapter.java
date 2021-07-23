package com.xh.play.adapters;

import android.graphics.Color;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xh.play.R;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapter.tag.ViewHolder;
import com.xh.play.entities.Tap;

public class TabAdapter extends RecyclerViewAdapter<Tap> {
    public TabAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public int getView(int itemType) {
        return R.layout.adapter_tab;
    }

    @Override
    public ViewHolder<Tap> getViewHolder(int itemType) {
        return new Holder();
    }

    private class Holder extends ViewHolder<Tap> {
        TextView textView;

        @Override
        public void setContext(Tap s) {
            textView.setText(s.title);
            if(s.select){
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.RED);
            }else{
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void bindView() {
            textView = findViewById(R.id.adapter_tab_tv);
        }
    }
}
