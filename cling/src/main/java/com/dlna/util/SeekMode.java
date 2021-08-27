package com.dlna.util;

/*
    ui4 (ABS_COUNT, REL_COUNT, TRACK_NR, TAPE-INDEX, FRAME)
    time (ABS_TIME, REL_TIME)
    float (CHANNEL_FREQ, in Hz)
    */
public enum SeekMode {

    TRACK_NR("TRACK_NR"),
    ABS_TIME("ABS_TIME"),
    REL_TIME("REL_TIME"),
    ABS_COUNT("ABS_COUNT"),
    REL_COUNT("REL_COUNT"),
    CHANNEL_FREQ("CHANNEL_FREQ"),
    TAPE_INDEX("TAPE-INDEX"),
    FRAME("FRAME");

    private String protocolString;

    SeekMode(String protocolString) {
        this.protocolString = protocolString;
    }

    @Override
    public String toString() {
        return protocolString;
    }

    public static SeekMode valueOrExceptionOf(String s) throws IllegalArgumentException {
        for (SeekMode seekMode : values()) {
            if (seekMode.protocolString.equals(s)) {
                return seekMode;
            }
        }
        throw new IllegalArgumentException("Invalid seek mode string: " + s);
    }
}