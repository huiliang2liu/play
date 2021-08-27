package com.http.http.request;

import com.http.http.util.Method;

import java.io.OutputStream;


/**
 * com.xh.http.request author:liuhuiliang email:825378291@qq.com
 * instruction:数据流的形式上传参数 2018-7-6 下午2:40:34
 **/
public class FlowRequest extends Request {
	private String mFlow;
	{
		setMethod(Method.POST);
	}

	public FlowRequest(String flow) {
		// TODO Auto-generated constructor stub
		mFlow = flow;

	}

	@Override
	public void report(OutputStream os) {
		// TODO Auto-generated method stub
		super.report(os);
		if (mFlow == null)
			return;
		try {
			os.write(mFlow.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
