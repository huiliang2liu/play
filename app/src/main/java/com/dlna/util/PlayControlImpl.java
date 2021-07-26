package com.dlna.util;


import com.dlna.action.avTransport.Pause;
import com.dlna.action.avTransport.Play;
import com.dlna.action.avTransport.Seek;
import com.dlna.action.avTransport.SetAVTransportURI;
import com.dlna.action.avTransport.Stop;
import com.dlna.action.renderingControl.SetMute;
import com.dlna.action.renderingControl.SetVolume;
import com.dlna.iface.IControl;
import com.dlna.iface.IPlayControl;
import com.dlna.listener.ControlListener;


public class PlayControlImpl implements IPlayControl {
    private static final String TAG = "PlayControlImpl";

    /**
     * 每次接收 500ms 延迟
     */
    private static final int RECEIVE_DELAY = 500;
    private IControl clingControl;
    /**
     * 上次设置音量时间戳, 防抖动
     */
    private long mVolumeLastTime;

    public PlayControlImpl(IControl control) {
        clingControl = control;
    }

    @Override
    public void play(final String url, final ControlListener listener) {
        stop(new ControlListener() {
            @Override
            public void onControlSuccess() {
                url(url, new ControlListener() {
                    @Override
                    public void onControlSuccess() {
                        play(listener);
                    }

                    @Override
                    public void onControlFailure() {
                        if (listener != null)
                            listener.onControlFailure();
                    }
                });
            }

            @Override
            public void onControlFailure() {
                if (listener != null)
                    listener.onControlFailure();
            }
        });
    }

    @Override
    public void play(final ControlListener listener) {
        clingControl.avTransport(new Play(), listener);
    }

    @Override
    public void pause(final ControlListener controlListener) {
        clingControl.avTransport(new Pause(), controlListener);
    }

    @Override
    public void seek(int pos, final ControlListener listener) {
        clingControl.avTransport(new Seek().setTarget(TimeUtil.getStringTime(pos)), listener);
    }

    @Override
    public void volume(int pos, final ControlListener listener) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > mVolumeLastTime + RECEIVE_DELAY) {
            clingControl.renderingControl(new SetVolume().setDesiredVolume(String.valueOf(pos)), listener);
        }
        mVolumeLastTime = currentTimeMillis;
    }

    @Override
    public void mute(boolean desiredMute, final ControlListener listener) {
        clingControl.renderingControl(new SetMute().setDesiredMute(String.valueOf(desiredMute)), listener);
    }

    @Override
    public void stop(final ControlListener listener) {
        clingControl.avTransport(new Stop(), listener);
    }

    @Override
    public void url(String url, final ControlListener listener) {
        String metadata = MediaUtil.pushMediaToRender(url, "id", "name", "0");
        clingControl.avTransport(new SetAVTransportURI().setCurrentURI(url).setCurrentURIMetaData(metadata), listener);
    }

}
