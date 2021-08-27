package com.http.http.request;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * threadPool com.xh.http.request 2018 2018-4-28 上午11:36:35 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class URIUploadRequest extends UploadRequest {
	private URI mUri;

	public URIUploadRequest(URI uri) {
		// TODO Auto-generated constructor stub
		mUri = uri;
	}

	public URIUploadRequest(String uri) {
		// TODO Auto-generated constructor stub
		try {
			mUri = new URI(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		if (mUri == null)
			return null;
		try {
			return mUri.toURL().openConnection().getInputStream();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
