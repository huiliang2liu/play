package com.dlna.action.renderingControl;

public class SetBlueVideoBlackLevel extends RenderingControlAction {
    public SetBlueVideoBlackLevel() {
        super("SetBlueVideoBlackLevel");
    }

    public SetBlueVideoBlackLevel setDesiredBlueVideoBlackLevel(String desiredBlueVideoBlackLevel) {
        addInput("DesiredBlueVideoBlackLevel", desiredBlueVideoBlackLevel);
        return this;
    }
}
