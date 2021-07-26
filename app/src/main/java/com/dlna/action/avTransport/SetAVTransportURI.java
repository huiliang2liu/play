package com.dlna.action.avTransport;

public class SetAVTransportURI extends AVTransportAction {
    public SetAVTransportURI() {
        super("SetAVTransportURI");
    }

    public SetAVTransportURI setCurrentURI(String currentURI) {
        addInput("CurrentURI", currentURI);
        return this;
    }

    public SetAVTransportURI setCurrentURIMetaData(String currentURIMetaData) {
        addInput("CurrentURIMetaData", currentURIMetaData);
        return this;
    }
}
