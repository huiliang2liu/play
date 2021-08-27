package com.xh.movies;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.xh.movies.platforms.IQiYi;
import com.xh.movies.platforms.PPTV;
import com.xh.movies.platforms.Platform1905;
import com.xh.paser.IPlatform;
import com.xh.paser.Title;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends FragmentActivity {
    private static final String TAG = "TestActivity";
    Handler handler = new Handler(Looper.getMainLooper());
    ViewPager viewPager;
    RecyclerView recyclerView;
    IPlatform platform;
    RVAdapter adapter;
    VPAdapter vpAdapter;
    EditText text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlatformsManager.init(getApplication());
        setContentView(R.layout.activity_test);
        text = findViewById(R.id.activity_test_et);
        findViewById(R.id.activity_test_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Log.e(TAG, ""+platform.search(text.getText().toString()).size());
                    }
                }.start();
            }
        });
        viewPager = findViewById(R.id.activity_test_vp);
        recyclerView = findViewById(R.id.activity_test_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        platform = new Platform1905();
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<Title> titles = platform.types();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new RVAdapter(titles);
                        recyclerView.setAdapter(adapter);
                        List<Fragment> fragments = new ArrayList<>();
                        for (Title title : titles) {
                            fragments.add(new TestFragment().setPlatform(platform).setUrl(title.url));
                        }
                        vpAdapter = new VPAdapter(getSupportFragmentManager(), fragments);
                        viewPager.setAdapter(vpAdapter);
                    }
                });
            }
        }.start();
    }

    private class VPAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public VPAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private class RVAdapter extends RecyclerView.Adapter {
        List<Title> titles = new ArrayList<>();

        public RVAdapter(List<Title> titles) {
            if (titles != null)
                this.titles = titles;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.tap, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).setTitle(titles.get(position), position);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView textView;
        int position;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tap_tv);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position, false);
                }
            });
        }

        private void setTitle(Title title, int position) {
            textView.setText(title.title);
            this.position = position;

        }
    }
}
