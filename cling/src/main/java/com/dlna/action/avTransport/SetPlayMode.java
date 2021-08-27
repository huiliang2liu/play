package com.dlna.action.avTransport;

public class SetPlayMode extends AVTransportAction {
    public SetPlayMode() {
        super("SetPlayMode");
    }

    public SetPlayMode setNewPlayMode(String newPlayMode) {
        addInput("NewPlayMode", newPlayMode);
        return this;
    }
}
