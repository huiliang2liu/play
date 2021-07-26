package com.dlna.iface;

import com.dlna.listener.ControlListener;

public interface IPlayControl {
    void play(String url, ControlListener listener);

    void play(ControlListener listener);

    void pause(ControlListener listener);

    void seek(int pos, ControlListener listener);

    void volume(int pos, ControlListener listener);

    void mute(boolean desiredMute, ControlListener listener);

    void stop(ControlListener listener);

    void url(String url, ControlListener listener);
}
