package com.dlna.action.renderingControl;

public class SetGreenVideoGain extends RenderingControlAction {
    public SetGreenVideoGain() {
        super("SetGreenVideoGain");
    }

    public SetGreenVideoGain setDesiredGreenVideoGain(String desiredGreenVideoGain) {
        addInput("DesiredGreenVideoGain", desiredGreenVideoGain);
        return this;
    }
}
