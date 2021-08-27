package com.dlna.cling;

import com.dlna.action.DlnaAction;

import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;

import java.util.Map;

public abstract class ClingAction extends ActionCallback {

    protected ClingAction(Service service, DlnaAction action) {
        super(new ActionInvocation(service.getAction(action.getAction())));
        Map<String, Object> params = action.getParams();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                getActionInvocation().setInput(key, params.get(key));
            }
        }
    }
}
