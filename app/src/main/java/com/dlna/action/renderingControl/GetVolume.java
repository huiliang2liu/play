package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class GetVolume extends RenderingControlAction {
    public GetVolume() {
        super("GetVolume");
        setChannel(Channel.Master.toString());
    }

    public GetVolume setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }
}
