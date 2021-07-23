package com.xh.play.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xh.play.R;
import com.xh.play.adapter.RecyclerViewAdapter;
import com.xh.play.adapters.TabAdapter;
import com.xh.play.animation.AnimatorFactory;
import com.xh.play.animation.ViewEmbellish;
import com.xh.play.entities.Detial;
import com.xh.play.entities.Tap;
import com.xh.play.media.widget.VideoView;
import com.xh.play.platforms.IPlatforms;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends Activity {
    public static final String TAG = "PlayActivity";
    public static final String DETAIL = "detail";
    public static final String DETAIL_PLAY_URL = "detail_play_url";
    public static final String PLATFORMS = "platforms";
    @BindView(R.id.activity_play_vv)
    VideoView videoView;
    @BindView(R.id.activity_play_rv)
    RecyclerView recyclerView;
    @BindView(R.id.activity_play_ll)
    FrameLayout frameLayout;
    @BindView(R.id.activity_play_tv)
    TextView textView;
    TabAdapter tabAdapter;
    IPlatforms platforms;
    Detial detial;
    private int index = 0;
    private Detial.DetailPlayUrl playUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager
                .LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        playUrl = getIntent().getParcelableExtra(DETAIL_PLAY_URL);
        if (playUrl != null) {
            textView.setText(playUrl.title);
            videoView.play(playUrl.href);
            hide();
        }else{
            detial = getIntent().getParcelableExtra(DETAIL);
            textView.setText(detial.name);
            platforms = (IPlatforms) getIntent().getSerializableExtra(PLATFORMS);
            tabAdapter = new TabAdapter(recyclerView);
            tabAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, long id) {
                    if (position == index)
                        return;
                    tabAdapter.getItem(index).select = false;
                    tabAdapter.getItem(position).select = true;
                    tabAdapter.notifyDataSetChanged(index);
                    index = position;
                    tabAdapter.notifyDataSetChanged(index);
                    PoolManager.io(new Runnable() {
                        @Override
                        public void run() {
                            final String url = platforms.play(((MyTap) tabAdapter.getItem(position)).url);
                            if (url == null || url.isEmpty()) {
                                Log.e(TAG, "play error");
                                Toast.makeText(getApplication(), "播放地址错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            PoolManager.runUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    videoView.play(url);
                                }
                            });
                        }
                    });
                }
            });
            PoolManager.io(new Runnable() {
                @Override
                public void run() {
                    if (platforms.playDetail(detial)) {
                        if (detial.playUrls.size() < 0) {
                            Log.e(TAG, "no url");
                            Toast.makeText(getApplication(), "没有播放地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final List<Tap> taps = new ArrayList<>();
                        for (int i = 0; i < detial.playUrls.size(); i++) {
                            MyTap myTap = new MyTap();
                            myTap.url = detial.playUrls.get(i);
                            myTap.title = myTap.url.title;
                            myTap.select = i == index;
                            taps.add(myTap);
                        }
                        final String url = platforms.play(detial.playUrls.get(0));
                        if (url == null || url.isEmpty()) {
                            Log.e(TAG, "play error");
                            Toast.makeText(getApplication(), "播放地址错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        PoolManager.runUiThread(new Runnable() {
                            @Override
                            public void run() {
                                videoView.play(url);
                                tabAdapter.addItem(taps);
                                hide();
                            }
                        });
                    } else
                        Toast.makeText(getApplication(), "解析播放地址失败", Toast.LENGTH_SHORT).show();
                    ;
                }
            });
        }

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE)
                    return;
                show();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!animatorStart && recyclerView.getVisibility() == View.VISIBLE) {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                PoolManager.removeUi(hideRannable);
            else if (event.getAction() == MotionEvent.ACTION_UP)
                hide();
        }
        super.dispatchTouchEvent(event);
        return true;
    }


    private boolean animatorStart = false;
    private Runnable hideRannable = new Runnable() {
        @Override
        public void run() {
            animatorStart = true;
            ViewEmbellish embellish = new ViewEmbellish(frameLayout) {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
//                        recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation, boolean isReverse) {
                    super.onAnimationEnd(animation, isReverse);
//                        recyclerView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    animatorStart = false;
                }
            };
            embellish.translationY(300, recyclerView.getHeight() * 2);
        }
    };

    private void show() {
        if (animatorStart)
            return;
        animatorStart = true;
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setTranslationY(0);
        ViewEmbellish embellish = new ViewEmbellish(frameLayout) {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                super.onAnimationEnd(animation, isReverse);
                animatorStart = false;
                hide();
            }
        };
        embellish.translationY(300, recyclerView.getHeight() * 2, 0);
        Log.e(TAG, "dadada");
    }

    private void hide() {
        PoolManager.runUiThread(hideRannable, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private class MyTap extends Tap {
        Detial.DetailPlayUrl url;
    }
}
