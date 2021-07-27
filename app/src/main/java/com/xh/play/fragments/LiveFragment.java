package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;

import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.activitys.PlayActivity;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.LiveAdapter;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Detial;
import com.xh.play.entities.Live;
import com.xh.play.entities.Tap;
import com.xh.play.platforms.DianYingMao;
import com.xh.play.platforms.IPlatform;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LiveFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.fragment_home_rv)
    RecyclerView recyclerView;
    LiveAdapter adapter;
    @BindView(R.id.fragment_home_yab)
    RecyclerView tabRV;
    TabAdapter tabAdapter;
    DianYingMao dianYingMao;
    int tab = 0;
    private List<Live> lives;


    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        adapter = new LiveAdapter(recyclerView);
        tabAdapter = new TabAdapter(tabRV);
        tabAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                tabAdapter.getItem(tab).select = false;
                tab = position;
                tabAdapter.getItem(tab).select = true;
                tabAdapter.notifyDataSetChanged();
                adapter.clean();
                adapter.addItem(lives.get(tab).detailPlayUrls);
            }
        });
        PlayApplication application = (PlayApplication) getContext().getApplicationContext();
        for (IPlatform platforms : application.platformList)
            if (platforms instanceof DianYingMao) {
                dianYingMao = (DianYingMao) platforms;
                break;
            }
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                Detial.DetailPlayUrl detial = adapter.getItem(position);
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra(PlayActivity.DETAIL_PLAY_URL, detial);
                startActivity(intent);
            }
        });
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                lives = dianYingMao.live();
                PoolManager.runUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < lives.size(); i++) {
                            Live title = lives.get(i);
                            Tap tap = new Tap();
                            tap.select = i == tab;
                            tap.title = title.name;
                            tabAdapter.addItem(tap);
                            if (tap.select) {
                                adapter.clean();
                                adapter.addItem(title.detailPlayUrls);
                            }
                        }
                    }
                });
            }
        });
    }


    @Override
    public int layout() {
        return R.layout.fragment_home;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void event(String tab) {

    }
}
