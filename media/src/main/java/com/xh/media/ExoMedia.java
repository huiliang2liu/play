package com.xh.media;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class ExoMedia implements IMedia, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener {
    private static final String TAG = "IjkMedia";
    private Context mContext;
    private IjkExoMediaPlayer mPlayer;
    private MediaListener mListener;
    private SurfaceHolder mHolder;

    ExoMedia(Context context) {
        this.mContext = context;
        mPlayer = new IjkExoMediaPlayer(context);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
    }

    @Override
    public void setMedialistener(MediaListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        Log.e(TAG, "holder" + (holder == null));
        mHolder = holder;
        mPlayer.setDisplay(holder);
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public boolean isPlay() {
        return mPlayer.isPlaying();
    }

    @Override
    public void play() {
        mPlayer.start();
    }

    @Override
    public void setPath(String path) {
        Log.e(TAG, "setPath");
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.reset();
        if (mHolder != null)
            mPlayer.setDisplay(mHolder);
        try {
            mPlayer.setDataSource(mContext, Uri.parse(path), null);
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long seek) {
        mPlayer.seekTo(seek);
    }

    @Override
    public void setSpeed(float speed) {
        Log.d(TAG, "not support");
    }

    @Override
    public void destroy() {
        mPlayer.release();
    }

    @Override
    public void stop() {
        mPlayer.stop();
    }

    @Override
    public void reset() {
        mPlayer.reset();

    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if (mListener != null)
            mListener.onBufferingUpdate(i);
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.e(TAG, "onError");
        if (mListener != null)
            mListener.onError(i,i1);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.e(TAG, "onInfo");
        if (mListener != null)
            return mListener.onInfo(i, i1);
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (mListener != null)
            mListener.onPrepared();
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mListener != null)
            mListener.onCompletion();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        if (mListener != null)
            mListener.onVideoSizeChanged(i, i1);
    }
}
