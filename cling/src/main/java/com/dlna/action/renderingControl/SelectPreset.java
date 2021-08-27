package com.dlna.action.renderingControl;

public class SelectPreset extends RenderingControlAction {
    public SelectPreset() {
        super("SelectPreset");
    }

    public SelectPreset setPresetName(String presetName) {
        addInput("PresetName", presetName);
        return this;
    }
}
