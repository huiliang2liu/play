package com.xh.play.thread;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * com.thread
 * 2018/9/21 16:40
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class MethodRunnable extends Runnable {
    private WeakReference<Object> mReference;
    private Method mMethod;
    private Object[] mParams;
    private Listener mListener;

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public MethodRunnable(Object object, Method method, Object... params) {
        init(object, method, params);
    }

    public MethodRunnable(Object object, String methodName, Object... params) {
        Method method = null;
        if (object != null && methodName != null && !methodName.isEmpty()) {
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method1 : methods) {
                if (methodName.equals(method1.getName())) {
                    Class[] paramsTypes = method.getParameterTypes();
                    if (paramsTypes.length == params.length) {
                        for (int i = 0; i < params.length; i++) {
                            Object param = params[i];
                            if (param == null)
                                continue;
                            if (param.getClass() != paramsTypes[i]) {
                                break;
                            }

                        }
                    } else
                        continue;
                    method = method1;
                    break;
                }
            }
        }
        init(object, method, params);
    }

    private void init(Object object, Method method, Object... params) {
        mReference = new WeakReference<>(object);
        mMethod = method;
        mParams = params;
    }

    @Override
    public void run() {
        if (mMethod == null)
            return;
        try {
            Object object = mReference.get();
            object = mMethod.invoke(object, mParams);
            if (mListener != null) {
                mListener.success(object);
            }
        } catch (Exception e) {
            if (mListener != null)
                mListener.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public  interface Listener {
        void success(Object object);

        void error(String msg);
    }
}
