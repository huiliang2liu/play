package com.xh.play.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xh.base.adapter.FragmentAdapter;
import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.RecyclerView;
import com.xh.base.widget.ViewPager;
import com.xh.paser.IPlatform;
import com.xh.paser.Title;
import com.xh.play.R;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Tap;
import com.xh.play.fragments.ChildFragment;
import com.xh.play.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlatformActivity extends BaseActivity {
    public static final String PLATFORM = "platform";
    public static final String TAG = "PlatformActivity";
    @BindView(R.id.activity_platform_vp)
    ViewPager viewPager;
    @BindView(R.id.activity_platform_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activity_platform_tv)
    TextView textView;
    IPlatform platform;
    private FragmentAdapter adapter;
    private TabAdapter tabAdapter;


    @Override
    protected void bindData() {
        super.bindData();
        if (application.platforms == null)
            return;
        platform = application.platforms.get(getIntent().getIntExtra(PLATFORM, 0));
        tabAdapter = new TabAdapter(recyclerView);
        tabAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                int select = 0;
                for (int i = 0; i < tabAdapter.getCount(); i++) {
                    if (tabAdapter.getItem(i).select) {
                        select = i;
                        break;
                    }
                }
                tabAdapter.getItem(select).select = false;
                tabAdapter.getItem(position).select = true;
                tabAdapter.notifyDataSetChanged(select);
                tabAdapter.notifyDataSetChanged(position);
                viewPager.setCurrentItem(position, false);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitles();
                textView.setVisibility(View.GONE);
            }
        });
        getTitles();
    }

    @Override
    protected int layout() {
        return R.layout.activity_platform;
    }

    private void getTitles() {
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                final List<Title> titles = platform.types();
                if (titles.size() <= 0) {
                    Log.e(TAG, "获取标题失败");
                    PoolManager.runUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setVisibility(View.VISIBLE);
                        }
                    });
                    return;
                }
                PoolManager.runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Tap> taps = new ArrayList<>();
                        List<Fragment> fragments = new ArrayList<>();
                        for (Title title : titles) {
                            Tap tap = new Tap();
                            tap.select = false;
                            tap.title = title.title;
                            taps.add(tap);
                            fragments.add(new ChildFragment().setPlatform(platform).setUrl(title.url));
                        }
                        Tap tap = new Tap();
                        tap.select = false;
                        tap.title = "搜索";
                        taps.add(tap);
                        taps.get(0).select = true;
                        tabAdapter.addItem(taps);
                        fragments.add(new SearchFragment().setPlatform(platform));
                        adapter = new FragmentAdapter(PlatformActivity.this, fragments);
                        viewPager.setAdapter(adapter);
                    }
                });
            }
        });
    }

}
