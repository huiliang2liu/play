package com.xh.movies;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xh.paser.IVip;
import com.xh.paser.Title;
import com.xh.paser.VipParsListener;

import java.util.ArrayList;
import java.util.List;

public class UrlActivity extends Activity {
    public static final String ACTIVITY_URL = "ACTIVITY_URL";
    private static final String TAG = "UrlActivity";
    RecyclerView recyclerView;
    RVAdapter vpAdapter;
    String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        recyclerView = findViewById(R.id.activity_url_rv);
        LinearLayoutManager llm = new GridLayoutManager(this, 4);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        vpAdapter = new RVAdapter(PlatformsManager.vips);
        recyclerView.setAdapter(vpAdapter);
        url = getIntent().getStringExtra(ACTIVITY_URL);
    }

    private class RVAdapter extends RecyclerView.Adapter {
        List<IVip> titles = new ArrayList<>();

        public RVAdapter(List<IVip> titles) {
            if (titles != null)
                this.titles = titles;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.tap, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).setTitle(titles.get(position), position);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView textView;
        IVip vip;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tap_tv);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    viewPager.setCurrentItem(position, false);
                    vip.parse(url, new VipParsListener() {
                        @Override
                        public void onListener(String url) {
                            Log.e(TAG, "===="+url);
                        }
                    });
                }
            });
        }

        private void setTitle(IVip title, int position) {
            textView.setText(title.name());
            this.vip = title;

        }
    }
}
