package com.xh.play.fragments;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.xh.base.adapter.FragmentAdapter;
import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.RecyclerView;
import com.xh.base.widget.ViewPager;
import com.xh.paser.IPlatform;
import com.xh.paser.Title;
import com.xh.play.R;
import com.xh.play.ViewHolder;
import com.xh.play.adapters.TextViewAdapter;
import com.xh.play.entities.TextViewData;

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
    @BindView(R.id.child_fragment_tv)
    TextView tv;
    IPlatform platform;
    String url;
    int index = 0;
    private FragmentAdapter adapter;

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
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setVisibility(View.GONE);
                getType();
            }
        });
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
        getType();

    }

    private void getType() {
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                List<Title> list = platform.titles(url);
                if (list.size() <= 0) {
                    PoolManager.runUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setVisibility(View.VISIBLE);
                        }
                    });
                    return;
                }
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
                        if (!isAdded())
                            return;
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
