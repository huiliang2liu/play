package com.dlna.action.renderingControl;

public class SetRedVideoGain extends RenderingControlAction {
    public SetRedVideoGain() {
        super("SetRedVideoGain");
    }

    public SetRedVideoGain setDesiredRedVideoGain(String desiredRedVideoGain) {
        addInput("DesiredRedVideoGain", desiredRedVideoGain);
        return this;
    }
}
