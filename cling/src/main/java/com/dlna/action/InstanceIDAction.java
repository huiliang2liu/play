package com.dlna.action;


public class InstanceIDAction extends DlnaAction {
    public InstanceIDAction(String action) {
        this.action = action;
        setInstanceID("0");
    }

    public InstanceIDAction setInstanceID(String instanceID) {
        addInput("InstanceID", instanceID);
        return this;
    }

}
