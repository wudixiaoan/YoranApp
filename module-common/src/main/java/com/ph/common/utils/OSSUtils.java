package com.ph.common.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ph.common.callback.OssUploadCallback;
import com.ph.common.log.XLog;

import java.io.File;

public class OSSUtils {
    private static final String TAG = "OSSUtils";
    public OSS mOss;
    private String mBucket;
    private String mEndpoint;
    private OssUploadCallback mCallback;

    public OSSUtils(Context context, String bucket, String endpoint, String accessKeyId, String accessKeySecret, OssUploadCallback callback) {
        mBucket = bucket;
        mEndpoint = endpoint;
        mCallback = callback;
        OSSCredentialProvider credentialProvider;
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOss = new OSSClient(context, "http://" + endpoint, credentialProvider, conf);
        OSSLog.enableLog();
    }

    public void asyncUploadFile(String target, String localFile) {
        final long start = System.currentTimeMillis();
        if (target.equals("")) {
            XLog.w(TAG, "asyncUploadFile", "参数错误，target为空");
            return;
        }
        File file = new File(localFile);
        if (!file.exists()) {
            XLog.w(TAG, "asyncUploadFile", "文件不存在");
            XLog.w(TAG, "asyncUploadFile", localFile);
            return;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(mBucket, target, localFile);
        put.setCRC64(OSSRequest.CRC64Config.YES);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            }
        });
        OSSAsyncTask task = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                XLog.i(TAG, "asyncUploadFile", "上传成功，request", request);
                XLog.i(TAG, "asyncUploadFile", "上传成功，result", result);
                XLog.i(TAG, "asyncUploadFile", "上传共耗时: " + (System.currentTimeMillis() - start) / 1000f);
                String url = "http://" + mBucket + "." + mEndpoint + "/" + target;
                XLog.i(TAG, "asyncUploadFile", "上传成功，url", url);
                if (mCallback != null) {
                    mCallback.onUploadSuccess(url);
                }
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                String info = "";
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                    if (mCallback != null) {
                        mCallback.onUploadFailure(info);
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    XLog.i(TAG, "asyncUploadFile", "服务异常", serviceException);
                }
            }
        });
    }
}
