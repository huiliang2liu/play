package com.xh.play.media;

import android.util.Log;
import android.view.SurfaceHolder;

class MediaImpl implements IMedia, MediaListener {
    private static final String TAG = "MediaImpl";
    private IMedia mMedia;
    private MediaListener mMediaListener;

    public MediaImpl(IMedia media) {
        this.mMedia = media;
        this.mMedia.setMedialistener(this);
    }

    @Override
    public void setMedialistener(MediaListener listener) {
        mMediaListener = listener;
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (mMedia != null)
            mMedia.setDisplay(holder);
    }

    @Override
    public void pause() {
        if (mMedia != null) {
            mMedia.pause();
        }
    }

    @Override
    public boolean isPlay() {
        if (mMedia != null)
            return mMedia.isPlay();
        return false;
    }

    @Override
    public void play() {
        if (mMedia != null) {
            mMedia.play();
        }
    }

    @Override
    public void setPath(String path) {
        if (mMedia != null) {
            mMedia.setPath(path);
        }

    }

    @Override
    public long getDuration() {
        if (mMedia != null)
            return mMedia.getDuration();
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        if (mMedia != null)
            return mMedia.getCurrentPosition();
        return 0;
    }

    @Override
    public void seekTo(long seek) {
        if (mMedia != null && mMedia.isPlay())
            mMedia.seekTo(seek);
    }

    @Override
    public void setSpeed(float speed) {
        if (mMedia != null)
            mMedia.setSpeed(speed);
    }

    @Override
    public void destroy() {
        if (mMedia != null)
            mMedia.destroy();
        mMedia = null;
    }

    @Override
    public void stop() {
        if (mMedia != null)
            mMedia.stop();
    }

    @Override
    public void reset() {
        if (mMedia != null)
            mMedia.reset();
    }

    @Override
    public void onCompletion() {
        Log.d(TAG, "onCompletion");
        if (mMediaListener != null)
            mMediaListener.onCompletion();
    }

    @Override
    public void onError(int what, int extra) {
        Log.d(TAG, "onError");
        if (mMediaListener != null)
            mMediaListener.onError(what, extra);
    }

    @Override
    public void onPrepared() {
        Log.d(TAG, "onPrepared");
        if (mMediaListener != null)
            mMediaListener.onPrepared();
    }

    @Override
    public boolean onInfo(int what, int extra) {
        if (mMediaListener != null)
            return mMediaListener.onInfo(what, extra);
        return false;
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mMediaListener != null)
            mMediaListener.onBufferingUpdate(percent);
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        if (mMediaListener != null)
            mMediaListener.onVideoSizeChanged(width, height);
    }
}
