package com.dlna.action.renderingControl;

public class SetVerticalKeystone extends RenderingControlAction {
    public SetVerticalKeystone() {
        super("SetVerticalKeystone");
    }

    public SetVerticalKeystone setDesiredVerticalKeystone(String desiredVerticalKeystone) {
        addInput("DesiredVerticalKeystone", desiredVerticalKeystone);
        return this;
    }
}
