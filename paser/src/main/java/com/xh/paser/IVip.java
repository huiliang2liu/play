package com.xh.paser;

import java.io.Serializable;

public interface IVip extends Serializable {
    void parse(String url, VipParsListener listener);
}
