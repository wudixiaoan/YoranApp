package com.ph.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    //默认存储路径
    public static final String DEFAULT_DIR =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    //广告存储路径
    public static final String VIDEO_DIR = DEFAULT_DIR + "ad_video/";
    //录像存储路径
    public static final String CAMERA_VIDEO_DIR = DEFAULT_DIR + "Movies/";

    //原始数据保存路径
    public static String RAWDATA_PATH_SDCARD_DIR = DEFAULT_DIR + "rawdata/";
    //购物信息路径
    public static final String SHOP_INFO_DIR = DEFAULT_DIR + "shop_info/";
    //下载路径
    public static final String DOWNLOAD_DIR = DEFAULT_DIR + "down_load/";
    //MTKLOG保存路径
    public static String MTKLOG_PATH_SDCARD_DIR = DEFAULT_DIR + "mtklog/";
    //LOG保存路径
    public static String LOG_PATH_SDCARD_DIR = DEFAULT_DIR + "log/";
    //Memory老化保存路径
    public static String MEMORY_AGING_PATH_SDCARD_DIR = DEFAULT_DIR + "memory_aging/";

    public static final String SHOPPING_VIDEO = "shoppingVideo2";
    public static final String SHOPPING_FILE = "shoppingFile2";

    /**
     * 清理时优先清理目录或文件
     * 越靠前越优先清理
     */
    public static final String[] FILE_BLACK_LIST = {
            FileUtils.MEMORY_AGING_PATH_SDCARD_DIR,
            FileUtils.DEFAULT_DIR + "mtklog",
            FileUtils.DEFAULT_DIR + "mtklog.zip",
            FileUtils.DEFAULT_DIR + "Download",
            FileUtils.DOWNLOAD_DIR,
            FileUtils.LOG_PATH_SDCARD_DIR,
            FileUtils.RAWDATA_PATH_SDCARD_DIR,
            FileUtils.DEFAULT_DIR + "Movies",
    };

    /**
     * 判断是否是白名单文件
     *
     * @param file
     * @return
     */
    public static boolean isBlackFile(String file) {
        for (int i = 0; i < FILE_BLACK_LIST.length; i++) {
            if (file.contains(FILE_BLACK_LIST[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 禁止清理目录或文件
     */
    public static final String[] FILE_WHITE_LIST = {
            FileUtils.DEFAULT_DIR + ".calibrate",//传感器校准文件
            FileUtils.DEFAULT_DIR + ".calibrate_last",//传感器校准文件last
            FileUtils.DEFAULT_DIR + ".calibrate_last_bk",//传感器校准文件last备份
            FileUtils.DEFAULT_DIR + ".c2board_info",//3代价钱小板信息
            FileUtils.DEFAULT_DIR + ".price_tag_calibrate",//价签校准信息
    };

    /**
     * 判断是否是白名单文件
     *
     * @param file
     * @return
     */
    public static boolean isWhiteFile(String file) {
        for (int i = 0; i < FILE_WHITE_LIST.length; i++) {
            if (file.contains(FILE_WHITE_LIST[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 读取raw目录中的文件,并返回为字符串
     */
    public static String getRawFile(Context context, int id) {
        final InputStream is = context.getResources().openRawResource(id);
        final BufferedInputStream bis = new BufferedInputStream(is);
        final InputStreamReader isr = new InputStreamReader(bis);
        final BufferedReader br = new BufferedReader(isr);
        final StringBuilder stringBuilder = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * @param fileName
     * @param write_str
     * @throws IOException
     */
    public static void writeFile(String fileName, String write_str) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String readFile(File file) {
        String content = ""; //文件内容字符串
        if (file.isDirectory()) {
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (Exception e) {
            }
        }
        return content;
    }

    /**
     * 删除目录
     *
     * @param pPath
     */
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWithFile(dir);
    }

    public static void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    /**
     * 读取文件内容
     *
     * @param strFilePath
     * @return
     */
    public static String readSDCardFile(String strFilePath) {
        String path = DEFAULT_DIR + strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }


    /**
     * 获取外部存储路径
     *
     * @param context
     * @return
     */
    public static String getSDCardStoragePath(Context context) {
        String[] volumes = getExternalStoragePath(context);
        if (volumes == null) {
            return DEFAULT_DIR;
        }
        for (int i = 0; i < volumes.length; i++) {
            if (!volumes[i].contains("emulated/0")) {
                return volumes[i] + "/";
            }
        }
        return DEFAULT_DIR;
    }

    private static String[] getExternalStoragePath(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            return (String[]) invoke;
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据后缀获取文件列表
     *
     * @param filePath
     * @param suffix
     * @return
     */
    public static List<File> orderFilesBySuffix(String filePath, String suffix) {
        File from = new File(filePath);
        List<File> resultFiles = new ArrayList<>();
        if (!from.exists()) {
            return resultFiles;
        }
        File[] files = from.listFiles();
        if (files == null) {
            return resultFiles;
        }
        for (File file : files) {
            if (file.getName().endsWith(suffix)) {
                resultFiles.add(file);
            }
        }
        Collections.sort(resultFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        return resultFiles;
    }

    /**
     * 压缩文件
     *
     * @param srcfile
     * @param zipfile
     */
    public static void zipFiles(List<File> srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            //ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.size(); i++) {
                FileInputStream in = new FileInputStream(srcfile.get(i));
                out.putNextEntry(new ZipEntry(srcfile.get(i).getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件目录并排序
     *
     * @param filePath
     * @param prefix
     * @param suffix
     * @return
     */
    public static List<File> orderFilesByPrefixAndSuffix(String filePath, String prefix, String suffix) {
        File from = new File(filePath);
        List<File> resultFiles = new ArrayList<>();
        try {
            if (!from.exists()) {
                return resultFiles;
            }
            File[] files = from.listFiles();
            if (files == null) {
                return resultFiles;
            }
            if (files == null) {
                return resultFiles;
            }
            for (File file : files) {
                if (file.getName().startsWith(prefix) && file.getName().endsWith(suffix)) {
                    resultFiles.add(file);
                }
            }
            Collections.sort(resultFiles, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } catch (Exception e) {
        }
        return resultFiles;
    }

    /**
     * 保存原始数据
     *
     * @param path
     * @param content
     */
    public static void writeRawDataToSdCard(String path, String content) {
        try {
            createDir("rawdata");
            RandomAccessFile randomFile = new RandomAccessFile(RAWDATA_PATH_SDCARD_DIR + path + ".txt", "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(content + "\r\n");
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录
     *
     * @param sdcardDirName
     * @return
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createDir(String sdcardDirName) {
        //拼接成SD卡中完整的dir
        final String dir = DEFAULT_DIR + "/" + sdcardDirName + "/";
        final File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    /**
     * 写入文件
     *
     * @param fileName
     * @param write_str
     * @throws IOException
     */
    public static void writeFileSdcardFile(String fileName, String write_str) throws IOException {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件目录
     *
     * @param dirPath
     * @return
     */
    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }


    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[2 * 1024];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    public static List<File> getFiles(String path) {
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return fileList;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            fileList.add(files[i]);
        }
        return fileList;
    }


    public static List<File> sortFileBySize(List<File> fileList) {
        int len = fileList.size();
        for (int i = 1; i < len; i++) {
            final File temp = fileList.get(i);
            fileList.remove(i);
            int j = i - 1;
            while (j >= 0 && getFileSize(temp) > getFileSize(fileList.get(j))) {
                j--;
            }
            fileList.add(j + 1, temp);
        }
        return fileList;
    }

    public static long getFileSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children == null) {
                    return 0;
                }
                long size = 0;
                for (File f : children)
                    size += getFileSize(f);
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取视频文件播放时长
     *
     * @param file
     * @return
     */
    public static long getVideoDuration(String file) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file);
            String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(timeString) / 1000;
            return duration;
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 获取目录以及目录的子目录所有文件数量
     *
     * @param directory
     * @return
     */
    public static int getFileCount(File directory) {
        int count = 0;

        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        count += getFileCount(file);
                    } else {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * uri转file路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = (returnCursor.getString(nameIndex));
            File file = new File(context.getFilesDir(), name);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            returnCursor.close();
            inputStream.close();
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        return getFileExtension(fileName);
    }

    public static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1);
        } else {
            return "";
        }
    }

    /**
     * 获取磁盘内大文件
     *
     * @param size 文件大小
     * @return
     */
    public static File[] getLargeFiles(long size) {
        File sdCard = Environment.getExternalStorageDirectory();
        File[] files = sdCard.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                if (file.isFile() && file.length() > size) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }


    public static void listFiles(List<File> list, File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        listFiles(list, file); // 递归遍历子目录
                    } else {
                        list.add(file);
                    }
                }
            }
        }
    }
}
