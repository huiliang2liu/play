package com.dlna.iface;

import com.dlna.action.avTransport.AVTransportAction;
import com.dlna.action.renderingControl.RenderingControlAction;
import com.dlna.listener.ControlListener;

public interface IControl {
    void avTransport(AVTransportAction action, ControlListener controlListener);

    void renderingControl(RenderingControlAction action, ControlListener controlListener);
}
