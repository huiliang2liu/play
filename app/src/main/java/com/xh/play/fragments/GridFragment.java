package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.ChildAdapter;
import com.xh.play.entities.ListMove;
import com.xh.play.platforms.IPlatform;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;

import androidx.annotation.NonNull;

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
                intent.putExtra(PlayActivity.PLATFORMS, platform);
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

    private void load(boolean loadMore) {
        loading = true;
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                ListMove listMove = platform.list(loadMore ? next : url);
                next = listMove.next;
                PoolManager.runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.setEnableLoadMore(next != null && !next.isEmpty());
                        if (loadMore) {
                            smartRefreshLayout.finishLoadMore();
                        } else {
                            if (listMove.detials.size() <= 0)
                                textView.setVisibility(View.VISIBLE);
                            smartRefreshLayout.finishRefresh();
                            adapter.clean();
                        }
                        adapter.addItem(listMove.detials);
                        loading = false;
                    }
                });

            }
        });
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
