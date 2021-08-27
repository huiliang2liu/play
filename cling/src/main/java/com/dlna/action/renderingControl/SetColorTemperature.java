package com.dlna.action.renderingControl;

public class SetColorTemperature extends RenderingControlAction {
    public SetColorTemperature() {
        super("SetColorTemperature");
    }
    public SetColorTemperature setDesiredColorTemperature(String desiredColorTemperature){
        addInput("DesiredColorTemperature",desiredColorTemperature);
        return this;
    }
}
