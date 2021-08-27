package com.dlna.action.renderingControl;

public class SetBrightness extends RenderingControlAction {
    public SetBrightness() {
        super("SetBrightness");
    }

    public SetBrightness setDesiredBrightness(String desiredBrightness) {
        addInput("DesiredBrightness", desiredBrightness);
        return this;
    }
}
