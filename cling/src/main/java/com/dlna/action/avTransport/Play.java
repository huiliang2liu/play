package com.dlna.action.avTransport;

public class Play extends AVTransportAction {
    public Play() {
        super("Play");
        setSpeed("1");
    }

    public Play setSpeed(String speed) {
        addInput("Speed", speed);
        return this;
    }
}
