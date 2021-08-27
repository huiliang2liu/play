package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class GetMute extends RenderingControlAction {
    public GetMute() {
        super("GetMute");
        setChannel(Channel.Master.toString());
    }

    public GetMute setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }
}
