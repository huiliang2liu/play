package com.xh.play.fragments;

import android.content.Intent;
import android.view.View;

import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.widget.RecyclerView;
import com.xh.paser.IPlatform;
import com.xh.play.PlayApplication;
import com.xh.play.R;
import com.xh.play.activitys.PlatformActivity;
import com.xh.play.adapters.TextViewAdapter;
import com.xh.play.entities.TextViewData;

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
        List<TextViewData> datas = new ArrayList();
        if (app.platforms == null)
            return;
        for (IPlatform platforms : app.platforms) {
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
        Intent intent = new Intent(getContext(), PlatformActivity.class);
        intent.putExtra(PlatformActivity.PLATFORM, application.platforms.indexOf(((Data) adapter.getItem(position)).platform));
        startActivity(intent);
    }

    private class Data extends TextViewData {
        IPlatform platform;
    }
}
