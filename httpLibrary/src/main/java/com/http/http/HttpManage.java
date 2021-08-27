package com.http.http;

import com.http.http.listen.ProgressListen;
import com.http.http.request.Request;
import com.http.http.response.Response;
import com.http.http.task.ATask;
import com.http.http.task.InputStreamTask;

import java.io.File;


/**
 * threadPool com.xh.http 2018 2018-4-28 上午10:15:20 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class HttpManage {
	public static Response response(Request request) {
		return response(new InputStreamTask(), request);
	}

	public static Down down(String path, int threadSize, File saveFile,
			ProgressListen listen) {
		Down down = new Down(path, threadSize, saveFile);
		down.setListen(listen);
		down.connection();
		return down;
	}

	private static Response response(ATask task, Request request) {
		task.setRequest(request);
		return task.connection();
	}
}
