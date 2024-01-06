package com.ph.common.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        } else {
            StringBuilder hex = new StringBuilder(bytes.length * 2);
            byte[] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                hex.append(hexDigits[b >> 4 & 15]);
                hex.append(hexDigits[b & 15]);
            }

            return hex.toString();
        }
    }

    public static String md5(File file) throws IOException {
        MessageDigest messagedigest = null;
        FileInputStream in = null;
        FileChannel ch = null;
        Object var4 = null;

        byte[] encodeBytes;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
            messagedigest.update(byteBuffer);
            encodeBytes = messagedigest.digest();
        } catch (NoSuchAlgorithmException var9) {
            throw new RuntimeException(var9);
        } finally {
            closeQuietly(in);
            closeQuietly(ch);
        }

        return toHexString(encodeBytes);
    }

    public static String md5(String string) {
        Object var1 = null;

        byte[] encodeBytes;
        try {
            encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3);
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException(var4);
        }

        return toHexString(encodeBytes);
    }


    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable var2) {
            }
        }
    }
}
