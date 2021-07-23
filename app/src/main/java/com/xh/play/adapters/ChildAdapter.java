package com.xh.play.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xh.play.R;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapter.tag.ViewHolder;
import com.xh.play.entities.Detial;

public class ChildAdapter extends RecyclerViewAdapter<Detial> {
    public ChildAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public ViewHolder<Detial> getViewHolder(int itemType) {
        return new Holder();
    }

    @Override
    public int getView(int itemType) {
        return R.layout.adapter_child;
    }

    public static class Holder extends ViewHolder<Detial> {
        ImageView imageView;
        TextView textView;
        TextView score;

        @Override
        public void setContext(Detial detial) {
            imageLoad.load(detial.img, imageView, null);
            textView.setText(detial.name);
            score.setText(detial.score);
            score.setVisibility(detial.score == null || detial.score.isEmpty() ? View.GONE : View.VISIBLE);
        }

        @Override
        public void bindView() {
            imageView = findViewById(R.id.adapter_child_iv);
            textView = findViewById(R.id.adapter_child_tv);
            score = findViewById(R.id.adapter_child_score);
        }
    }
}
