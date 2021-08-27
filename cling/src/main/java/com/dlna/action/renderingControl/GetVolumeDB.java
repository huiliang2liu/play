package com.dlna.action.renderingControl;

import com.dlna.util.Channel;

public class GetVolumeDB extends RenderingControlAction {
    public GetVolumeDB() {
        super("GetVolumeDB");
        setChannel(Channel.Master.toString());
    }

    public GetVolumeDB setChannel(String channel) {
        addInput("Channel", channel);
        return this;
    }
}
