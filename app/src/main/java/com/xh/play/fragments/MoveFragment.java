package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;

import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.activitys.PlatformActivity;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.TextViewAdapter;
import com.xh.play.entities.TextViewData;
import com.xh.play.platforms.IPlatform;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoveFragment extends BaseFragment implements RecyclerViewAdapter.OnItemClickListener {
    @BindView(R.id.fragment_move_rv)
    RecyclerView recyclerView;
    TextViewAdapter adapter;

    @Override
    public void bindView() {
        super.bindView();
        ButterKnife.bind(this, view);
        adapter = new TextViewAdapter(recyclerView);
        adapter.setOnItemClickListener(this);
        PlayApplication app = (PlayApplication) getContext().getApplicationContext();
        List<TextViewData> datas =new ArrayList();
        for (IPlatform platforms : app.platformList) {
            Data data = new Data();
            data.name = platforms.name();
            data.platform = platforms;
            datas.add(data);
        }
        adapter.addItem(datas);
    }

    @Override
    public int layout() {
        return R.layout.fragment_move;
    }

    @Override
    public void onItemClick(View view, int position, long id) {
        Intent intent =new Intent(getContext(), PlatformActivity.class);
        intent.putExtra(PlatformActivity.PLATFORM,((Data)adapter.getItem(position)).platform);
        startActivity(intent);
    }

    private class Data extends TextViewData {
        IPlatform platform;
    }
}
