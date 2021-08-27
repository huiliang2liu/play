package com.dlna.action.renderingControl;

public class SetContrast extends RenderingControlAction {
    public SetContrast() {
        super("SetContrast");
    }

    public SetContrast setDesiredContrast(String contrast) {
        addInput("DesiredContrast", contrast);
        return this;
    }
}
