package com.xh.play.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dlna.iface.IPlayControl;
import com.dlna.listener.ControlListener;
import com.dlna.listener.DeviceChangeListener;
import com.dlna.util.DeviceManager;
import com.xh.base.adapter.RecyclerViewAdapter;
import com.xh.base.widget.RecyclerView;
import com.xh.play.R;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Tap;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClingActivity extends Activity {
    public static final String PLAY_URL = "play_url";
    private static final String TAG = "ClingActivity";
    @BindView(R.id.activity_cling_rv)
    RecyclerView recyclerView;
    TabAdapter tabAdapter;
    DeviceManager manager;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cling);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(PLAY_URL);
        tabAdapter = new TabAdapter(recyclerView);
        tabAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                IPlayControl control = manager.findPlayControl(tabAdapter.getItem(position).title);
                if (control != null)
                    control.play(url, new ControlListener() {
                        @Override
                        public void onControlSuccess() {
                            Log.e(TAG, "onControlSuccess");
                        }

                        @Override
                        public void onControlFailure() {
                            Log.e(TAG, "onControlFailure");
                        }
                    });
            }
        });
        manager = new DeviceManager(this);
        manager.search(new DeviceChangeListener() {
            @Override
            public void onChange(List<String> strings) {
                Log.e("onChange", "onChange");
                Set<String> set = new HashSet<>();
                set.addAll(strings);
                tabAdapter.clean();
                if (strings != null && strings.size() > 0) {
                    for (String s : set) {
                        Tap tap = new Tap();
                        tap.title = s;
                        tabAdapter.addItem(tap);
                    }
                }

            }
        });
    }
}
