package com.dlna.action.renderingControl;

public class SetBlueVideoGain extends RenderingControlAction {
    public SetBlueVideoGain() {
        super("SetBlueVideoGain");
    }

    public SetBlueVideoGain setDesiredBlueVideoGain(String desiredBlueVideoGain) {
        addInput("DesiredBlueVideoGain", desiredBlueVideoGain);
        return this;
    }
}
