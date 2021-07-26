package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class SetVolumeDB extends RenderingControlAction {
    public SetVolumeDB() {
        super("SetVolumeDB");
        setChannel(Channel.Master.toString());
    }

    public SetVolumeDB setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }

    public SetVolumeDB setDesiredVolume(String desiredVolume) {
        addInput("DesiredVolume", desiredVolume);
        return this;
    }
}
