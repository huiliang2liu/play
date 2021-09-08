package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.thread.PoolManager;
import com.xh.base.widget.RecyclerView;
import com.xh.paser.Detial;
import com.xh.paser.IPlatform;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapters.ChildAdapter;


import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment {
    IPlatform platform;
    @BindView(R.id.fragment_search_ev)
    EditText editText;
    @BindView(R.id.fragment_search_tv)
    TextView textView;
    @BindView(R.id.grid4_view_rv)
    RecyclerView recyclerView;
    ChildAdapter adapter;

    public SearchFragment setPlatform(IPlatform platform) {
        this.platform = platform;
        return this;
    }

    private static class Search implements Runnable {
        String text;
        IPlatform platform;
        WeakReference<ChildAdapter> weakReference;

        Search(ChildAdapter adapter, IPlatform platform, String text) {
            weakReference = new WeakReference<>(adapter);
            this.platform = platform;
            this.text = text;
        }

        @Override
        public void run() {
            List<Detial> detials = platform.search(text);
            if (detials == null || detials.size() <= 0)
                return;
            ChildAdapter adapter = weakReference.get();
            if (adapter == null)
                return;
            PoolManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.clean();
                    adapter.addItem(detials);
                }
            });
        }
    }

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        adapter = new ChildAdapter(recyclerView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PoolManager.io(new Search(adapter, platform, editText.getText().toString()));
//                PoolManager.io(new Runnable() {
//                    @Override
//                    public void run() {
//                        List<Detial> detials = platform.search(editText.getText().toString());
//                        PoolManager.runUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.clean();
//                                adapter.addItem(detials);
//                            }
//                        });
//                    }
//                });
            }
        });
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra(PlayActivity.PLATFORMS, application.platforms.indexOf(platform));
                intent.putExtra(PlayActivity.DETAIL, adapter.getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public int layout() {
        return R.layout.fragment_search;
    }
}
