package com.xh.play.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.dlna.cling.ClingDeviceManagerImpl;
import com.dlna.listener.DeviceChangeListener;
import com.dlna.util.DeviceManager;
import com.xh.play.R;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.entities.Tap;
import com.xh.play.widget.RecyclerView;

import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClingActivity extends Activity {
    @BindView(R.id.activity_cling_rv)
    RecyclerView recyclerView;
    TabAdapter tabAdapter;
    DeviceManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cling);
        ButterKnife.bind(this);
        tabAdapter=new TabAdapter(recyclerView);
        manager =new DeviceManager(this);
        manager.search(new DeviceChangeListener() {
            @Override
            public void onChange(List<String> strings) {
                Log.e("onChange","onChange");
                if(strings!=null&&strings.size()>0){
                    for (String s:strings) {
                        Tap tap = new Tap();
                        tap.title = s;
                        tabAdapter.addItem(tap);
                    }
                }

            }
        });
    }
}
