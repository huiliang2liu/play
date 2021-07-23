package com.xh.play.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IjkMedia implements IMedia, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener {
    private static final String TAG = "IjkMedia";
    private Context mContext;
    private IjkMediaPlayer mPlayer;
    private MediaListener mListener;
    private SurfaceHolder mHolder;
    private Settings mSettings;

    IjkMedia(Context context) {
        this.mContext = context;
        mPlayer = new IjkMediaPlayer();
        mPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
        mSettings = new Settings(context);
        if (mSettings.getUsingMediaCodec()) {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            if (mSettings.getUsingMediaCodecAutoRotate()) {
                mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
            } else {
                mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
            }
            if (mSettings.getMediaCodecHandleResolutionChange()) {
                mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            } else {
                mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
            }
        } else {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        }

        if (mSettings.getUsingOpenSLES()) {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        } else {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        }

        String pixelFormat = mSettings.getPixelFormat();
        if (TextUtils.isEmpty(pixelFormat)) {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        } else {
            mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
        }
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
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
        } catch (IOException e) {
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
        mPlayer.setSpeed(speed);
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
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.e(TAG, "onError");
        if (IMediaPlayer.MEDIA_ERROR_SERVER_DIED == what)
            what = MediaPlayer.MEDIA_ERROR_SERVER_DIED;
        else if (IMediaPlayer.MEDIA_ERROR_IO == what) {
            extra = MediaPlayer.MEDIA_ERROR_IO;
            what = MediaPlayer.MEDIA_ERROR_IO;
        } else if (IMediaPlayer.MEDIA_ERROR_MALFORMED == what) {
            extra = MediaPlayer.MEDIA_ERROR_MALFORMED;
            what = MediaPlayer.MEDIA_ERROR_MALFORMED;
        } else if (IMediaPlayer.MEDIA_ERROR_UNSUPPORTED == what) {
            extra = MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
            what = MediaPlayer.MEDIA_ERROR_UNSUPPORTED;
        } else if (IMediaPlayer.MEDIA_ERROR_TIMED_OUT == what) {
            extra = MediaPlayer.MEDIA_ERROR_TIMED_OUT;
            what = MediaPlayer.MEDIA_ERROR_TIMED_OUT;
        } else {
            what = MediaPlayer.MEDIA_ERROR_UNKNOWN;
        }

        if (mListener != null)
            mListener.onError(what, extra);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.e(TAG, "onInfo");
        extra = MediaPlayer.MEDIA_INFO_UNKNOWN;
        if (IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what)
            what = MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
        else if (IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING == what)
            what = MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING;
        else if (IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING == what)
            what = MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING;
        else if (IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE == what)
            what = MediaPlayer.MEDIA_INFO_NOT_SEEKABLE;
        else if (IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT == what)
            what = MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT;
        else if (IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE == what)
            what = MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
        else if (IMediaPlayer.MEDIA_INFO_BUFFERING_START == what)
            what = MediaPlayer.MEDIA_INFO_BUFFERING_START;
        else if (IMediaPlayer.MEDIA_INFO_BUFFERING_END == what)
            what = MediaPlayer.MEDIA_INFO_BUFFERING_END;
        else
            what = MediaPlayer.MEDIA_INFO_UNKNOWN;
        if (mListener != null)
            return mListener.onInfo(what, extra);
        return true;
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
