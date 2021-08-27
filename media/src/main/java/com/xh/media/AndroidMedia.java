package com.xh.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;


class AndroidMedia implements IMedia, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String TAG = "AndroidMedia";
    private MediaPlayer mMediaPlayer;
    private MediaListener mListener;
    private Context mContext;

    {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
    }

    public AndroidMedia(Context context) {
        this.mContext = context;
    }

    @Override
    public void setMedialistener(MediaListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void play() {
        Log.d(TAG, "play");
        mMediaPlayer.start();
    }


    @Override
    public void setPath(String path) {
        try {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mContext, Uri.parse(path));
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取资源失败了");
        }
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
    }

    @Override
    public void reset() {
        mMediaPlayer.reset();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(long seek) {
        if (android.os.Build.VERSION.SDK_INT >= 26)
            mMediaPlayer.seekTo(seek, MediaPlayer.SEEK_PREVIOUS_SYNC);
        else
            mMediaPlayer.seekTo((int) seek);
    }

    @Override
    public void setSpeed(float speed) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            try {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
                mMediaPlayer.pause();
                mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "设置播放速度失败了");
            }
        } else {
            Log.e(TAG, "不能设置播放速度");
        }

    }

    @Override
    public void destroy() {
        if (mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mListener.onCompletion();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("VideoView", String.format("what:%s,extra:%s",what,extra));
        if (mListener != null)
            mListener.onError(what,extra);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mListener != null)
            mListener.onPrepared();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mListener != null)
            mListener.onBufferingUpdate(percent);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (mListener != null)
            return mListener.onInfo(what, extra);
        return true;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (mListener != null)
            mListener.onVideoSizeChanged(width, height);
    }
}
