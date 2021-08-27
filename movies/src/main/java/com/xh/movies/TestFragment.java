package com.xh.movies;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.xh.paser.IPlatform;
import com.xh.paser.Title;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {
    private IPlatform platform;
    private String url;
    ViewPager viewPager;
    RecyclerView tab;
    RVAdapter adapter;
    VPAdapter vpAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    public TestFragment() {

    }

    public TestFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    public TestFragment setPlatform(IPlatform platform) {
        this.platform = platform;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        viewPager = view.findViewById(R.id.fragment_test_vp);
        tab = view.findViewById(R.id.fragment_test_tab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        tab.setLayoutManager(linearLayoutManager);
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<Title> titles = platform.titles(url);
                if (titles == null)
                    return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new RVAdapter(titles);
                        tab.setAdapter(adapter);
                        List<Fragment> fragments = new ArrayList<>();
                        for (Title title : titles) {
                            fragments.add(new ChildFragment().setPlatform(platform).setUrl(title.url));
                        }
                        vpAdapter = new VPAdapter(getChildFragmentManager(), fragments);
                        viewPager.setAdapter(vpAdapter);
                    }
                });

            }
        }.start();
        return view;
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
