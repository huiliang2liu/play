package com.xh.play.media;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.media.bind.IMediaListener;
import com.media.bind.IPlayMusic;
import com.xh.play.media.exoplayer.ExoMediaImpl;

public class MusicService extends Service implements MediaListener {
    private IMedia media;
    private IMediaListener listener;
    private IPlayMusic.Stub mBind = new IPlayMusic.Stub() {
        @Override
        public void pause() throws RemoteException {
            media.pause();
        }

        @Override
        public boolean isPlay() throws RemoteException {
            return media.isPlay();
        }

        @Override
        public void play() throws RemoteException {
            media.play();
        }

        @Override
        public void setPath(String path) throws RemoteException {
            media.setPath(path);
        }

        @Override
        public long getDuration() throws RemoteException {
            return media.getDuration();
        }

        @Override
        public long getCurrentPosition() throws RemoteException {
            return media.getCurrentPosition();
        }

        @Override
        public void seekTo(long seek) throws RemoteException {
            media.seekTo(seek);
        }

        @Override
        public void setSpeed(float speed) throws RemoteException {
            media.setSpeed(speed);
        }

        @Override
        public void setMediaListener(IMediaListener listener) throws RemoteException {
            MusicService.this.listener = listener;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        media = new ExoMediaImpl(this);
        media.setMedialistener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        media.pause();
        media.stop();
        media.destroy();
    }

    @Override
    public void onCompletion() {
        try {
            if (listener != null)
                listener.onCompletion();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onError(int what, int extra) {
        try {
            if (listener != null)
                listener.onError(what,extra);
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onPrepared() {
        try {
            if (listener != null)
                listener.onPrepared();
        } catch (RemoteException e) {
        }
    }

    @Override
    public boolean onInfo(int what, int extra) {
        try {
            if (listener != null)
                return listener.onInfo(what,extra);
        } catch (RemoteException e) {
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(int percent) {
        try {
            if (listener != null)
                listener.onBufferingUpdate(percent);
        } catch (RemoteException e) {
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
        try {
            if (listener != null)
                listener.onVideoSizeChanged(width,height);
        } catch (RemoteException e) {
        }
    }
}
