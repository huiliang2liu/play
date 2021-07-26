package com.dlna.action.renderingControl;

public class SetRedVideoBlackLevel extends RenderingControlAction {
    public SetRedVideoBlackLevel() {
        super("SetRedVideoBlackLevel");
    }

    public SetRedVideoBlackLevel setDesiredRedVideoBlackLevel(String desiredRedVideoBlackLevel) {
        addInput("DesiredRedVideoBlackLevel", desiredRedVideoBlackLevel);
        return this;
    }
}
