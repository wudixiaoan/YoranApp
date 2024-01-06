package com.ph.common.module.http.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class FileDownloadCallback {
    //可以重写，具体可由子类实现
    public void onComplete() {
    }

    //下载成功的回调
    public abstract void onDownLoadSuccess(String filePath);

    //下载失败回调
    public abstract void onDownLoadFail(Throwable e);

    //下载进度监听
    public abstract void onProgress(int progress, int total);

    /**
     * 将文件写入本地
     *
     * @param destFileDir  目标文件夹
     * @param destFileName 目标文件名
     * @return 写入完成的文件
     * @throws IOException IO异常
     */
    public File saveFile(InputStream is, String destFileDir, String destFileName) throws IOException {
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
