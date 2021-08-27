package com.dlna.action.renderingControl;

public class SetHorizontalKeystone extends RenderingControlAction {
    public SetHorizontalKeystone() {
        super("SetHorizontalKeystone");
    }

    public SetHorizontalKeystone setDesiredHorizontalKeystone(String desiredHorizontalKeystone) {
        addInput("DesiredHorizontalKeystone", desiredHorizontalKeystone);
        return this;
    }
}
