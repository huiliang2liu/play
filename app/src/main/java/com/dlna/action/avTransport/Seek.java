package com.dlna.action.avTransport;

import com.dlna.util.SeekMode;

public class Seek extends AVTransportAction {
    public Seek() {
        super("Seek");
        setUnit(SeekMode.REL_TIME.name());
    }

    public Seek setUnit(String unit) {
        addInput("Unit", unit);
        return this;
    }

    public Seek setTarget(String target) {
        addInput("Target", target);
        return this;
    }
}
