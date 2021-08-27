package com.xh.movies;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xh.paser.Detial;
import com.xh.paser.IPlatform;
import com.xh.paser.ListMove;

import java.util.ArrayList;
import java.util.List;

public class ChildFragment extends Fragment {
    static final String TAG = "ChildFragment";
    RecyclerView recyclerView;
    IPlatform platform;
    String url;
    Handler handler = new Handler(Looper.getMainLooper());
    RVAdapter adapter;
    private String next;

    public ChildFragment setPlatform(IPlatform platform) {
        this.platform = platform;
        return this;
    }

    public ChildFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, null);
        recyclerView = view.findViewById(R.id.fragment_child_rv);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RVAdapter();
        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.fragment_child_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next == null || next.isEmpty())
                    return;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ListMove listMove = platform.list(next);
                        next = listMove.next;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "" + listMove.detials.size());
                                adapter.addDetial(listMove.detials);
                            }
                        });
                    }
                }.start();
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                ListMove listMove = platform.list(url);
                if (listMove == null)
                    return;
                next = listMove.next;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "" + listMove.detials.size());
                        adapter.addDetial(listMove.detials);
                    }
                });
            }
        }.start();
        return view;
    }

    private class RVAdapter extends RecyclerView.Adapter {
        List<Detial> titles = new ArrayList<>();

        public void addDetial(List<Detial> detials) {
            titles.addAll(detials);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.tap, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((VH) holder).setTitle(titles.get(position));
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView textView;
        Detial detial;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tap_tv);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (platform.playDetail(detial))
                                Log.e(TAG, platform.play(detial.playUrls.get(0)));
                        }
                    }.start();
                }
            });
        }

        private void setTitle(Detial detial) {
            textView.setText(detial.name);
            this.detial = detial;

        }
    }
}
