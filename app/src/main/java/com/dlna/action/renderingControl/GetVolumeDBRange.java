package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class GetVolumeDBRange extends RenderingControlAction {
    public GetVolumeDBRange() {
        super("GetVolumeDBRange");
        setChannel(Channel.Master.toString());
    }

    public GetVolumeDBRange setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }
}
