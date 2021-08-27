package com.http.http.request;


import com.http.http.util.Method;

/**
 * threadPool com.xh.http.request 2018 2018-4-28 上午11:18:36 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class DownRequest extends Request {
	private long start;
	private long end;
	{
		setAccept("image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		method = Method.GET;
	}

	@Override
	public Request setPath(String path) {
		// TODO Auto-generated method stub
		setReferer(path);
		return super.setPath(path);
	}

	public DownRequest setStartAndEnd(long startPos, long endPos) {
		start = startPos;
		end = endPos;
		setRange("bytes=" + startPos + "-" + endPos);
		return this;
	}

	@Override
	public DownRequest setMethod(Method method) {
		// TODO Auto-generated method stub
		return this;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

}
