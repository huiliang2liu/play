package com.dlna.action;

import java.util.HashMap;
import java.util.Map;

public class DlnaAction {
    protected String action;
    protected Map<String, Object> params = new HashMap<>();

    protected DlnaAction addInput(String name, String value) {
        params.put(name, value);
        return this;
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
