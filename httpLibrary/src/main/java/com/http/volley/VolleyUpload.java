package com.http.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.http.http.request.UploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * User：liuhuiliang
 * Date：2020-01-13
 * Time：11:45
 * Descripotion：volley只适合下载小文件
 */
public class VolleyUpload extends Request<byte[]> {
    private static final String TAG = "VolleyDown";
    public static final String PREFIX = "--";
    public static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data;boundary=%s";
    public static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    private String url;
    private Map<String, String> mParams = new HashMap<>();
    private Map<String, String> mHeard = new HashMap<>();
    protected Map<String, String> responseHeard = new HashMap<>();
    protected int code;
    private byte[] raw;
    private String type;
    private Object lock = new Object();
    private boolean loaded = false;
    private boolean success = false;
    private File uploadFile;
    private byte[] mResponse;
    private String fileKey;


    public VolleyUpload(String url, Map<String, String> params, Map<String, String> heard, String fileKey, File uploadFile) {
        super(Method.POST, url, null);
        setShouldCache(false);
        if (heard != null && heard.size() > 0)
            mHeard.putAll(heard);
        mHeard.put("Content-Type", String.format(CONTENT_TYPE, BOUNDARY));
        if (params != null && params.size() > 0)
            mParams.putAll(params);
        this.uploadFile = uploadFile;
        this.fileKey = fileKey;
        setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void setRaw(byte[] raw, String type) {
        this.type = type;
        this.raw = raw;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeard;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringBuffer sb = new StringBuffer();
        /***
         * 以下是用于上传参数
         */
        if (mParams != null && mParams.size() > 0) {
            Iterator<String> it = mParams.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = mParams.get(key).toString();
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
                + "\"; filename=\"" + uploadFile.getName() + "\"" + LINE_END);
        sb.append("Content-Type:image/pjpeg,image/png,image/bmp,image/bmp,image/x-png"
                + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
        sb.append(LINE_END);
        try {
            bos.write(sb.toString().getBytes("utf-8"));
        } catch (Exception e) {
        }
        try {
            FileInputStream fis = new FileInputStream(uploadFile);
            int len = -1;
            byte[] buff = new byte[1024 * 1024];
            while ((len = fis.read(buff)) > 0)
                bos.write(buff, 0, len);
            bos.write(UploadRequest.LINE_END.getBytes());
            byte[] end_data = (UploadRequest.PREFIX + UploadRequest.BOUNDARY
                    + UploadRequest.PREFIX + UploadRequest.LINE_END).getBytes();
            bos.write(end_data);
        } catch (Exception e) {
        }
        return bos.toByteArray();
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.i(TAG, "deliverError");
        loaded = true;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public String getBodyContentType() {
        if (type != null && !type.isEmpty())
            return String.format("application/%s; charset=" + getParamsEncoding(), type);
        return super.getBodyContentType();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeard = response.headers;
        code = response.statusCode;
        return Response.success(response.data,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mResponse = response;
        Log.i(TAG, "deliverResponse");
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    byte[] getResponse() {
        if (!loaded)
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return mResponse;
    }
}
