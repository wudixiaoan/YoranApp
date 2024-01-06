package com.ph.common.module.http.util;

import android.util.Log;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SslContextFactory {

    private static final String CLIENT_TRUST_PASSWORD = "*******";//信任证书密码

    private static final String CLIENT_AGREEMENT = "SSL";//使用协议

    private static final String CLIENT_TRUST_MANAGER = "X509";

    private static final String CLIENT_TRUST_KEYSTORE = "BKS";

    SSLContext sslContext = null;

    public SSLContext getSslSocket() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            //取得SSL的SSLContext实例
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            //初始化
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            Log.e("SslContextFactory", e.getMessage());
        }
        return sslContext;
    }
}

