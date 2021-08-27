package com.http.http.task;


import com.http.http.request.Request;
import com.http.http.response.Response;

/**
 * threadPool com.xh.http 2018 2018-4-28 上午9:57:36 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public abstract class ATask {
	protected Request mRequest;

	public void setRequest(Request mRequest) {
		this.mRequest = mRequest;
	}

	public abstract Response connection();
}
