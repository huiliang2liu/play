package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.RecyclerView;
import com.xh.paser.IPlatform;
import com.xh.paser.ListMove;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapters.ChildAdapter;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridFragment extends BaseFragment {
    String url;
    IPlatform platform;
    @BindView(R.id.grid4_view_rv)
    RecyclerView recyclerView;
    ChildAdapter adapter;
    @BindView(R.id.fragment_grid_srl)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.fragment_grid_tv)
    TextView textView;
    String next;
    private boolean loading = false;

    public GridFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load(false);
                textView.setVisibility(View.GONE);
            }
        });
        adapter = new ChildAdapter(recyclerView);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra(PlayActivity.PLATFORMS, application.platforms.indexOf(platform));
                intent.putExtra(PlayActivity.DETAIL, adapter.getItem(position));
                startActivity(intent);
            }
        });
        load(false);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                load(true);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                load(false);
            }
        });
    }

    @Override
    protected void visible() {
        super.visible();
        if (adapter.getCount() < 0 && !loading)
            load(false);
    }

    private static class Load implements Runnable {
        WeakReference<GridFragment> weakReference;
        IPlatform platform;
        String url;
        boolean loadMore;

        Load(GridFragment fragment, IPlatform platform, String url, boolean loadMore) {
            weakReference = new WeakReference<>(fragment);
            this.platform = platform;
            this.url = url;
            this.loadMore = loadMore;
        }

        @Override
        public void run() {
            ListMove listMove = platform.list(url);
            GridFragment fragment = weakReference.get();
            if (fragment == null)
                return;
            if (listMove.detials != null && listMove.detials.size() > 0)
                fragment.next = listMove.next;
            PoolManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fragment.smartRefreshLayout == null)
                        return;
                    fragment.smartRefreshLayout.setEnableLoadMore(fragment.next != null && !fragment.next.isEmpty());
                    if (loadMore) {
                        fragment.smartRefreshLayout.finishLoadMore();
                    } else {
                        if (listMove.detials == null || listMove.detials.size() <= 0)
                            fragment.textView.setVisibility(View.VISIBLE);
                        fragment.smartRefreshLayout.finishRefresh();
                        fragment.adapter.clean();
                    }
                    fragment.adapter.addItem(listMove.detials);
                    fragment.loading = false;
                }
            });
        }
    }

    private void load(boolean loadMore) {
        loading = true;
        PoolManager.io(new Load(this, platform, loadMore ? next : url, loadMore));
//        PoolManager.io(new Runnable() {
//            @Override
//            public void run() {
//                ListMove listMove = platform.list(loadMore ? next : url);
//                next = listMove.next;
//                PoolManager.runUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (smartRefreshLayout == null)
//                            return;
//                        smartRefreshLayout.setEnableLoadMore(next != null && !next.isEmpty());
//                        if (loadMore) {
//                            smartRefreshLayout.finishLoadMore();
//                        } else {
//                            if (listMove.detials == null || listMove.detials.size() <= 0)
//                                textView.setVisibility(View.VISIBLE);
//                            smartRefreshLayout.finishRefresh();
//                            adapter.clean();
//                        }
//                        adapter.addItem(listMove.detials);
//                        loading = false;
//                    }
//                });
//
//            }
//        });
    }

    public GridFragment setPlatform(IPlatform platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public int layout() {
        return R.layout.fragment_grid;
    }
}
