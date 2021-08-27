package com.xh.play.entities;

public class AssetsUrl {
    public String host;
    public AUrl[] urls;
    public String[] packages;

    public static class AUrl {
        public String url;
        public String clazz;
        public boolean isPost = false;
        private String method;

        public String getMethod() {
            if (method == null || method.isEmpty())
                method = "get" + clazz;
            return method;
        }
    }
}
