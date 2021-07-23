package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.ChildAdapter;
import com.xh.play.entities.Detial;
import com.xh.play.entities.ListMove;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildFragment extends BaseFragment {
    @BindView(R.id.child_fragment_rv)
    RecyclerView recyclerView;
    ChildAdapter adapter;
    String url;
    IPlatforms platforms;
    String next;
    @BindView(R.id.child_fragment_srl)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        adapter = new ChildAdapter(recyclerView);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Detial detial = adapter.getItem(position);
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra(PlayActivity.DETAIL, detial);
                intent.putExtra(PlayActivity.PLATFORMS, platforms);
                startActivity(intent);
            }
        });
        loadDate(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadDate(false);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadDate(true);
            }
        });
    }

    private void loadDate(boolean loadMove) {
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                ListMove listMove = platforms.list(loadMove ? next : url);
                next = listMove.next;
                final List<Detial> detials = listMove.detials;
                PoolManager.runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadMove) {
                            smartRefreshLayout.finishLoadMore();
                        } else{
                            smartRefreshLayout.finishRefresh();
                            adapter.clean();
                        }
                        adapter.addItem(detials);
                        if (next == null || next.isEmpty())
                            smartRefreshLayout.setEnableLoadMore(false);
                    }
                });
            }
        });
    }


    public void setPlatforms(IPlatforms platforms) {
        this.platforms = platforms;
    }

    @Override
    public int layout() {
        return R.layout.child_fragment;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
