package com.xh.play.fragments;


import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.xh.play.R;
import com.xh.play.adapter.FragmentAdapter;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Tap;
import com.xh.play.entities.Title;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AkmjFragment extends BaseFragment implements RecyclerViewAdapter.OnItemClickListener {
    private static final String TAG = "LiveFragment";
    @BindView(R.id.main_fragment_rv)
    RecyclerView liveTab;
    @BindView(R.id.main_fragment_vp)
    ViewPager liveVp;
    private FragmentAdapter fragmentAdapter;
    IPlatforms platforms;
    TabAdapter tabAdapter;
    int tab = 0;
    boolean loading = false;
    Runnable load = new Runnable() {
        @Override
        public void run() {
            final List<Title> titles = platforms.main();
            PoolManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    List<Fragment> entities = new ArrayList<>(titles.size());
                    for (int i = 0; i < titles.size(); i++) {
                        Title title = titles.get(i);
                        Tap tap = new Tap();
                        tap.select = i == tab;
                        tap.title = title.title;
                        tabAdapter.addItem(tap);
                        ChildFragment fragment = new ChildFragment();
                        fragment.setUrl(title.url);
                        fragment.setPlatforms(platforms);
                        entities.add(fragment);
                    }
                    fragmentAdapter = new FragmentAdapter(AkmjFragment.this, entities);
                    liveVp.setAdapter(fragmentAdapter);
                    loading = false;
                }
            });
        }
    };

    public AkmjFragment setPlatforms(IPlatforms platforms) {
        this.platforms = platforms;
        return this;
    }


    @Override
    protected void visible() {
        super.visible();
        if (tabAdapter.getCount() <= 0 && !loading) {
            loading = true;
            PoolManager.io(load);
        }
    }

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        tabAdapter = new TabAdapter(liveTab);
        tabAdapter.setOnItemClickListener(this);
    }

    @Override
    public int layout() {
        return R.layout.main_fragment;
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        tabAdapter.getItem(tab).select = false;
        liveVp.setCurrentItem(position, false);
        tab = position;
        tabAdapter.getItem(tab).select = true;
        tabAdapter.notifyDataSetChanged();
    }
}
