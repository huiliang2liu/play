package com.dlna.action.renderingControl;

public class SetSharpness extends RenderingControlAction {
    public SetSharpness() {
        super("SetSharpness");
    }

    public SetSharpness setDesiredSharpness(String desiredSharpness) {
        addInput("DesiredSharpness", desiredSharpness);
        return this;
    }
}
