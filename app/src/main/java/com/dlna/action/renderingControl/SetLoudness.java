package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class SetLoudness extends RenderingControlAction {
    public SetLoudness() {
        super("SetLoudness");
        setChannel(Channel.Master.toString());
    }

    public SetLoudness setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }

    public SetLoudness setDesiredLoudness(String desiredLoudness) {
        addInput("DesiredLoudness", desiredLoudness);
        return this;
    }
}
