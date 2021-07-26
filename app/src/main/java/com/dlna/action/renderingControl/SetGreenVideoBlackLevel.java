package com.dlna.action.renderingControl;

public class SetGreenVideoBlackLevel extends RenderingControlAction {
    public SetGreenVideoBlackLevel() {
        super("SetGreenVideoBlackLevel");
    }

    public SetGreenVideoBlackLevel setDesiredGreenVideoBlackLevel(String desiredGreenVideoBlackLevel) {
        addInput("DesiredGreenVideoBlackLevel", desiredGreenVideoBlackLevel);
        return this;
    }
}
