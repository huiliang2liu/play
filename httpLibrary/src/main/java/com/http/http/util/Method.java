package com.http.http.util;



public enum Method {
	GET("GET"), POST("POST"), HEAD("HEAD"), OPTIONS("OPTIONS"), PUT("PUT"), DELETE(
			"DELETE"), TRACE("TRACE");
	private String mMethod;

	private Method(String method) {
		mMethod = method;
	}

	public String getMethod() {
		return mMethod;
	}

}
