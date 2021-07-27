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

import com.xh.play.R;
import com.xh.play.adapter.FragmentAdapter;
import com.xh.play.entities.Tap;
import com.xh.play.entities.Title;
import com.xh.play.fragments.ChildFragment;
import com.xh.play.fragments.SearchFragment;
import com.xh.play.platforms.IPlatform;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlatformActivity extends FragmentActivity implements View.OnClickListener {
    public static final String PLATFORM = "platform";
    public static final String TAG = "PlatformActivity";
    @BindView(R.id.activity_platform_vp)
    ViewPager viewPager;
    @BindView(R.id.activity_platform_ll)
    LinearLayout layout;
    @BindView(R.id.activity_platform_tv)
    TextView textView;
    List<TextView> buttons = new ArrayList<>();
    IPlatform platform;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_platform);
        ButterKnife.bind(this);
        platform = (IPlatform) getIntent().getSerializableExtra(PLATFORM);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitles();
                textView.setVisibility(View.GONE);
            }
        });
        getTitles();
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
                        List<Fragment> fragments = new ArrayList<>();
                        for (Title title : titles) {
                            TextView tv = new TextView(PlatformActivity.this);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(15);
                            tv.setText(title.title);
                            tv.setBackgroundColor(Color.WHITE);
                            tv.setTextColor(Color.BLACK);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.weight = 1;
                            tv.setLayoutParams(layoutParams);
                            layout.addView(tv);
                            buttons.add(tv);
                            fragments.add(new ChildFragment().setPlatform(platform).setUrl(title.url));
                        }
                        {
                            TextView tv = new TextView(PlatformActivity.this);
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(15);
                            tv.setText("搜索");
                            tv.setBackgroundColor(Color.WHITE);
                            tv.setTextColor(Color.BLACK);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.weight = 1;
                            tv.setLayoutParams(layoutParams);
                            layout.addView(tv);
                            buttons.add(tv);
                            fragments.add(new SearchFragment().setPlatform(platform));
                        }
                        for (TextView tv : buttons) {
                            tv.setOnClickListener(PlatformActivity.this);
                        }
                        TextView tv = buttons.get(0);
                        tv.setBackgroundColor(Color.RED);
                        tv.setTextColor(Color.WHITE);
                        adapter = new FragmentAdapter(PlatformActivity.this, fragments);
                        viewPager.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        for (TextView tv : buttons) {
            tv.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.BLACK);
        }
        TextView tv = (TextView) view;
        tv.setBackgroundColor(Color.RED);
        tv.setTextColor(Color.WHITE);
        viewPager.setCurrentItem(buttons.indexOf(tv), false);
    }
}
