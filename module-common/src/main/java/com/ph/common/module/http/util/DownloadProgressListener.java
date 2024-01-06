package com.ph.common.module.http.util;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
