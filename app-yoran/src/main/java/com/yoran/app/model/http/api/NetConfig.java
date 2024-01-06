package com.yoran.app.model.http.api;

public class NetConfig {
    public static class Default {
        public static final String BUSINESS_URL = "https://www.zigoomo.com/";
        public static final String BUSINESS_PROJECT = "KWoBox";
    }

    public static class Action {
    }

    public static class Url {
        public static String getRootServer() {
            return "http://www.zigoomo.com";
        }

        public static String getZgmApiHttps() {
            return "https://zgmapi.zigoomo.com";
        }

        public static String getZgmApiHttp() {
            return "http://zgmapi.zigoomo.com";
        }
    }

    public static class Response {
        public static final String SUCCESS = "success";
        public static final String FAIL = "fail";
    }

    public static class UploadType {
        public static final String SERVER = "server";
        public static final String OSS = "oss";
    }

    public class Constants {
        public static final String HTTP_HEAD = "http://";
        public static final String HTTPS_HEAD = "https://";
    }
}
