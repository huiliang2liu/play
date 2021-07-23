package com.xh.play.fragments;

import android.view.View;

import com.xh.play.R;
import com.xh.play.adapter.RecyclerViewAdapter;

import butterknife.ButterKnife;

public class MjwFragment extends BaseFragment implements RecyclerViewAdapter.OnItemClickListener {


    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
    }

    @Override
    public int layout() {
        return R.layout.layout;
    }
    @Override
    public void onItemClick(View view, int position, long id) {

    }
}
