package com.http.http.request;

import com.http.http.util.Method;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.UUID;


/**
 * threadPool com.xh.http.request 2018 2018-4-28 上午10:58:19 instructions：
 * author:liuhuiliang email:825378291@qq.com
 **/

public class UploadRequest extends Request {
	public static final String PREFIX = "--";
	public static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	public static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
	private String fileKey;
	private String filename;
	private InputStream inputStream;
	{
		setContentType(CONTENT_TYPE + ";boundary=" + BOUNDARY);
		method = Method.POST;
	}

	@Override
	public UploadRequest setMethod(Method method) {
		// TODO Auto-generated method stub
		return this;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public UploadRequest setFileKey(String fileKey) {
		this.fileKey = fileKey;
		return this;
	}

	public UploadRequest setFilename(String filename) {
		this.filename = filename;
		return this;
	}

	@Override
	public String params2string() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		// String params = "";

		/***
		 * 以下是用于上传参数
		 */
		if (params != null && params.size() > 0) {
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"")
						.append(key).append("\"").append(LINE_END)
						.append(LINE_END);
				sb.append(value).append(LINE_END);
			}
		}
		/**
		 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的
		 * 比如:abc.png
		 */
		sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
		sb.append("Content-Disposition:form-data; name=\"" + fileKey
				+ "\"; filename=\"" + filename + "\"" + LINE_END);
		sb.append("Content-Type:image/pjpeg,image/png,image/bmp,image/bmp,image/x-png"
				+ LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
		sb.append(LINE_END);
		return sb.toString();
	}

	@Override
	public void report(OutputStream os) {
		// TODO Auto-generated method stub
		super.report(os);
		InputStream is = getInputStream();
		if (is == null)
			return;
		try {
			byte[] buff = new byte[1024 * 1024];
			int len = is.read(buff);
			while (len != -1) {
				os.write(buff, 0, len);
				len = is.read(buff);
			}
			try {
				is.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			os.write(UploadRequest.LINE_END.getBytes());
			byte[] end_data = (UploadRequest.PREFIX + UploadRequest.BOUNDARY
					+ UploadRequest.PREFIX + UploadRequest.LINE_END).getBytes();
			os.write(end_data);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
