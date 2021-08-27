package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class SetVolume extends RenderingControlAction {
    public SetVolume() {
        super("SetVolume");
        setChannel(Channel.Master.toString());
    }

    public SetVolume setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }

    public SetVolume setDesiredVolume(String desiredVolume) {
        addInput("DesiredVolume", desiredVolume);
        return this;
    }
}
