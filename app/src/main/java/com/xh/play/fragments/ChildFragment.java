package com.xh.play.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.xh.play.R;
import com.xh.play.adapter.FragmentAdapter;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapter.tag.ViewHolder;
import com.xh.play.adapters.TextViewAdapter;
import com.xh.play.entities.TextViewData;
import com.xh.play.entities.Title;
import com.xh.play.platforms.IPlatform;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;
import com.xh.play.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildFragment extends BaseFragment {
    @BindView(R.id.child_fragment_tab)
    RecyclerView tab;
    TextViewAdapter textView;
    @BindView(R.id.child_fragment_vp)
    ViewPager viewPager;
    IPlatform platform;
    String url;
    IPlatforms platforms;
    int index = 0;
    private FragmentAdapter adapter;

    public void setPlatforms(IPlatforms platforms) {
        this.platforms = platforms;
    }

    public ChildFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    public ChildFragment setPlatform(IPlatform platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        textView = new TextViewAdapter(tab) {
            @Override
            public ViewHolder<TextViewData> getViewHolder(int itemType) {
                return new MyHolder();
            }

            @Override
            public int getView(int itemType) {
                return R.layout.text_view_rv;
            }
        };
        textView.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                viewPager.setCurrentItem(position, false);
                ((Data) textView.getItem(position)).select = true;
                ((Data) textView.getItem(index)).select = false;
                textView.notifyItemChanged(position);
                textView.notifyItemChanged(index);
                index = position;
            }
        });
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                List<Title> list = platform.titles(url);
                List<TextViewData> datas = new ArrayList<>();
                List<Fragment> fragments = new ArrayList<>(list.size());
                for (Title title : list) {
                    Data data = new Data();
                    data.name = title.title;
                    data.url = title.url;
                    datas.add(data);
                    fragments.add(new GridFragment().setUrl(title.url).setPlatform(platform));
                }
                if (datas.size() > 0)
                    ((Data) datas.get(0)).select = true;
                PoolManager.runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.addItem(datas);
                        adapter = new FragmentAdapter(ChildFragment.this, fragments);
                        viewPager.setAdapter(adapter);
                    }
                });
            }
        });

    }

    @Override
    protected void visible() {
        super.visible();

    }


    @Override
    public int layout() {
        return R.layout.child_fragment;
    }

    private static class Data extends TextViewData {
        boolean select = false;
        String url;
    }

    private class MyHolder extends TextViewAdapter.Holder {
        @Override
        public void setContext(TextViewData textViewData) {
            super.setContext(textViewData);
            if (((Data) textViewData).select) {
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.RED);
            } else {
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.WHITE);
            }
        }
    }

}
