package com.ph.common.callback;

public interface OssUploadCallback {
    void onUploadSuccess(String url);

    void onUploadFailure(String errorMsg);
}
