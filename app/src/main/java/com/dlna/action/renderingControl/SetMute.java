package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class SetMute extends RenderingControlAction {
    public SetMute() {
        super("SetMute");
        setChannel(Channel.Master.toString());
    }

    public SetMute setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }

    public SetMute setDesiredMute(String desiredMute) {
        addInput("DesiredMute", desiredMute);
        return this;
    }
}
