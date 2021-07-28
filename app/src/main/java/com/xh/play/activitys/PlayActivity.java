package com.xh.play.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.xh.play.media.MediaListener;
import com.xh.play.media.widget.VideoView;
import com.xh.play.platforms.IPlatform;
import com.xh.play.thread.PoolManager;
import com.xh.play.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends Activity implements View.OnClickListener {
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
    @BindView(R.id.activity_play_state)
    LinearLayout stateLL;
    @BindView(R.id.activity_play_play_time)
    TextView playTime;
    @BindView(R.id.activity_play_duration_time)
    TextView durationTime;
    @BindView(R.id.activity_play_sb)
    SeekBar playSb;
    @BindView(R.id.activity_play__tv)
    TextView tv;
    @BindView(R.id.activity_play_speed_ll)
    LinearLayout speedLl;
    @BindView(R.id.activity_play_speed_05)
    TextView speed05;
    @BindView(R.id.activity_play_speed_10)
    TextView speed10;
    @BindView(R.id.activity_play_speed_15)
    TextView speed15;
    @BindView(R.id.activity_play_speed_20)
    TextView speed20;
    TextView speed;
    TabAdapter tabAdapter;
    IPlatform platform;
    Detial detial;
    private int index = 0;
    private long duration;
    private Detial.DetailPlayUrl playUrl;
    private boolean show = true;
    private float moveHeight = 300;
    ViewEmbellish stateLLEmbllish;
    ViewEmbellish rvEmbellish;
    ViewEmbellish speedLlEmbellish;
    private Future future;
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            PoolManager.runUiThread(new Runnable() {
                @Override
                public void run() {
                    if (duration <= 0)
                        return;
                    long position = videoView.getCurrentPosition();
                    int progress = (int) (position * 100 / duration);
                    playSb.setProgress(progress);
                    playTime.setText(timeFormat(position));
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager
                .LayoutParams.FLAG_HARDWARE_ACCELERATED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        speed05.setOnClickListener(this);
        speed10.setOnClickListener(this);
        speed15.setOnClickListener(this);
        speed20.setOnClickListener(this);
        speed = speed10;
        speed.setBackgroundColor(Color.WHITE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detial.playUrls == null || detial.playUrls.size() <= 0)
                    getPlayList();
                else
                    getUrl();
                tv.setVisibility(View.GONE);
            }
        });
        stateLLEmbllish = new ViewEmbellish(stateLL);
        rvEmbellish = new ViewEmbellish(recyclerView);
        speedLlEmbellish = new ViewEmbellish(speedLl);
        findViewById(R.id.activity_play_cling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ClingActivity.class);
                intent.putExtra(ClingActivity.PLAY_URL, videoView.getPath());
                startActivity(intent);
            }
        });

        playUrl = getIntent().getParcelableExtra(DETAIL_PLAY_URL);
        if (playUrl != null) {
            textView.setText(playUrl.title);
            videoView.play(playUrl.href);
            stateLL.setVisibility(View.GONE);
        } else {
            stateLL.setVisibility(View.VISIBLE);
            detial = getIntent().getParcelableExtra(DETAIL);
            textView.setText(detial.name);
            platform = (IPlatform) getIntent().getSerializableExtra(PLATFORMS);
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
                    playSb.setProgress(0);
                    durationTime.setText("00:00");
                    playTime.setText("00:00");
                    getUrl();
                }
            });
            getPlayList();
        }
        videoView.setMediaListener(new MediaListener() {
            @Override
            public void onCompletion() {
            }

            @Override
            public void onError(int what, int extra) {

            }

            @Override
            public void onPrepared() {

            }

            @Override
            public boolean onInfo(int what, int extra) {
                Log.e(TAG, String.format("what:%s,extra:%s", what, extra));
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START && platform != null) {
                    duration = videoView.getDuration();
                    Log.e(TAG, "duration:" + duration);
                    PoolManager.runUiThread(new Runnable() {
                        @Override
                        public void run() {
                            durationTime.setText(timeFormat(duration));
                        }
                    });
                }
                return false;
            }

            @Override
            public void onBufferingUpdate(int percent) {
                if (percent > playSb.getProgress())
                    playSb.setSecondaryProgress(percent);

            }

            @Override
            public void onVideoSizeChanged(int width, int height) {

            }
        });
        playSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                long playTime = seekBar.getProgress();
                long seek = duration * playTime / 100;
                Log.e(TAG, "playTime:" + playTime + ",seek:" + seek);
                videoView.seekTo(seek);
            }
        });
    }

    private void getUrl() {
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                final String url = platform.play(((MyTap) tabAdapter.getItem(index)).url);
                if (url == null || url.isEmpty()) {
                    Log.e(TAG, "play error");
                    Toast.makeText(getApplication(), "播放地址错误", Toast.LENGTH_SHORT).show();
                    tv.setVisibility(View.VISIBLE);
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

    private void getPlayList() {
        PoolManager.io(new Runnable() {
            @Override
            public void run() {
                if (platform.playDetail(detial)) {
                    if (detial.playUrls.size() < 0) {
                        Log.e(TAG, "no url");
                        Toast.makeText(getApplication(), "没有播放地址", Toast.LENGTH_SHORT).show();
                        tv.setVisibility(View.VISIBLE);
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
                    final String url = platform.play(detial.playUrls.get(0));
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (platform != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                show();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
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
            show = false;
            List<ObjectAnimator> objectAnimators = new ArrayList<>();
            ObjectAnimator objectAnimator = AnimatorFactory.translationY(rvEmbellish, 300, 0, -moveHeight);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animatorStart = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimators.add(objectAnimator);
            objectAnimators.add(AnimatorFactory.translationX(speedLlEmbellish, 300, 0, moveHeight));
            if (platform != null)
                objectAnimators.add(AnimatorFactory.translationY(stateLLEmbllish, 300, 0, moveHeight));
            AnimatorFactory.startAnimation(objectAnimators);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            future.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PoolManager.scheduledRemove(timeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.destroy();
    }

    private void show() {
        if (show)
            return;
        if (animatorStart)
            return;
        animatorStart = true;
        show = true;
        List<ObjectAnimator> objectAnimators = new ArrayList<>();
        ObjectAnimator objectAnimator = AnimatorFactory.translationY(rvEmbellish, 300, -moveHeight, 0);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animatorStart = false;
//                hide();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimators.add(objectAnimator);
        objectAnimators.add(AnimatorFactory.translationX(speedLlEmbellish, 300, moveHeight, 0));
        if (platform != null)
            objectAnimators.add(AnimatorFactory.translationY(stateLLEmbllish, 300, moveHeight, 0));
        AnimatorFactory.startAnimation(objectAnimators);
    }

    private void hide() {
        PoolManager.removeUi(hideRannable);
        PoolManager.runUiThread(hideRannable, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN);
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                         |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                         |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//    );
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        future = PoolManager.scheduled(timeRunnable, 1, 1);

    }

    private class MyTap extends Tap {
        Detial.DetailPlayUrl url;
    }

    private String timeFormat(long time) {
        long sec = time / 1000;
        long min = sec / 60;
        sec = sec % 60;
        StringBuilder sb = new StringBuilder();
        if (min > 9)
            sb.append(min);
        else if (min > 0)
            sb.append("0").append(min);
        else
            sb.append("00");
        sb.append(":");
        if (sec > 9)
            sb.append(sec);
        else if (sec > 0)
            sb.append("0").append(sec);
        else
            sb.append("00");
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        speed.setBackgroundColor(Color.TRANSPARENT);
        speed = (TextView) v;
        speed.setBackgroundColor(Color.WHITE);
        videoView.setSpeed(Float.valueOf(speed.getText().toString()));
    }
}
