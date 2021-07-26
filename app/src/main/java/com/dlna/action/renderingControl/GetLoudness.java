package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class GetLoudness extends RenderingControlAction {
    public GetLoudness() {
        super("GetLoudness");
        setChannel(Channel.Master.toString());
    }

    public GetLoudness setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }
}
