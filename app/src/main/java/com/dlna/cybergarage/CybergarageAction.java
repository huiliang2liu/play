package com.dlna.cybergarage;

import com.dlna.action.DlnaAction;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Service;

import java.util.HashMap;
import java.util.Map;

public abstract class CybergarageAction implements Runnable {
    private DlnaAction dlnaAction;
    private Service service;

    public CybergarageAction(Service service, DlnaAction action) {
        this.dlnaAction = action;
        this.service = service;
    }


    @Override
    public void run() {
        Action action = service.getAction(dlnaAction.getAction());
        if (action != null) {
            Map<String, Object> params = dlnaAction.getParams();
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    action.setArgumentValue(key, String.valueOf(params.get(key)));
                }
            }
            if (action.postControlAction())
                success();
            else
                failure();
        } else
            failure();
    }

    public abstract void success();

    public abstract void failure();
}
