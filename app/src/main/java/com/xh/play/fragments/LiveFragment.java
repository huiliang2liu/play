package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;

import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.RecyclerView;
import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapters.LiveAdapter;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Tap;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LiveFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fragment_home_rv)
    RecyclerView recyclerView;
    LiveAdapter adapter;
    @BindView(R.id.fragment_home_yab)
    RecyclerView tabRV;
    TabAdapter tabAdapter;
    int tab = 0;


    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        adapter = new LiveAdapter(recyclerView);
        tabAdapter = new TabAdapter(tabRV);
        tabAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                tabAdapter.getItem(tab).select = false;
                tab = position;
                tabAdapter.getItem(tab).select = true;
                tabAdapter.notifyDataSetChanged();
                adapter.clean();
            }
        });
        PlayApplication application = (PlayApplication) getContext().getApplicationContext();
    }


    @Override
    public int layout() {
        return R.layout.fragment_home;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void event(String tab) {

    }
}
