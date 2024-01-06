package com.ph.common.utils;

import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import androidx.core.app.ActivityCompat;

import com.ph.common.base.BaseApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class SystemUtils {
    private static final String SETTING_DATE = "lingmao.intent.action.SETTING_DATE";
    private static final String NETWORK_NONE = "NONE"; // 没有网络连接
    private static final String NETWORK_WIFI = "WIFI"; // wifi连接
    private static final String NETWORK_2G = "2G"; // 2G
    private static final String NETWORK_3G = "3G"; // 3G
    private static final String NETWORK_4G = "4G"; // 4G
    private static final String NETWORK_MOBILE = "MOBILE"; // 手机流量
    private static final String NETWORK_ETHERNET = "eth"; // 以太网连接
    private static final String UNKNOWN = "unknown";
    private static final String REBOOT_SYSTEM = "lingmao.intent.action.REBOOT_SYSTEM";
    private static final String SHUTDOWN_SYSTEM = "lingmao.intent.action.SHUTDOWN_SYSTEM";
    private static final String SYSTEM_PROPERTIES = "lingmao.intent.action.SYSTEM_PROPERTIES";
    public static final int POWER_MODE_OFF = 0;
    public static final int POWER_MODE_NORMAL = 2;

    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        //判断是否大于安卓10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "1234567890";
        } else {
            final TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(context.TELEPHONY_SERVICE);
//
//        if (BuildConfig.DEBUG) {
//            return "200002011173915";
//        }
            String imei = telephonyManager.getDeviceId();
            return imei == null ? "1234567890" : imei;//"1234567890";//
        }
    }


    /**
     * 获取sn号
     *
     * @param context
     * @return
     */
    public static String getDeviceSn(Context context) {
        String sn = "1234567890";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                sn = "1234567890";
            } else {
                sn = Build.getSerial();
            }
        } else {
            sn = Build.SERIAL;
        }
        return sn;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {//以太网
                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                    while (interfaces.hasMoreElements()) {
                        NetworkInterface iface = interfaces.nextElement();
                        if (iface.getName().startsWith("eth")) {
                            Enumeration<InetAddress> addresses = iface.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                InetAddress addr = addresses.nextElement();
                                if (addr instanceof Inet4Address) {
                                    return addr.getHostAddress();
                                }
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return NETWORK_NONE;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取当前网络连接的类型
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return NETWORK_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }
        NetworkInfo ethernetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (null != ethernetInfo) {
            NetworkInfo.State state = ethernetInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_ETHERNET;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_4G;
            default:
                return NETWORK_MOBILE;
        }
    }


    /**
     * 获取当前网络连接的类型
     */
    public static int getNetworkStateCode(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) {
            return 0;
        }
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return 0;
        }
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return -1;
                }
            }
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        return networkType;
    }

    public static int getSystemSettingsInt(Context context, String key, int def) {
        return Settings.Global.getInt(context.getContentResolver(), key, def);
    }

    public static String getSystemSettingsString(Context context, String key) {
        return Settings.Global.getString(context.getContentResolver(), key);
    }

    public static long getSystemSettingsLong(Context context, String key, long def) {
        return Settings.Global.getLong(context.getContentResolver(), key, def);
    }

    public static float getSystemSettingsFloat(Context context, String key, float def) {
        return Settings.Global.getFloat(context.getContentResolver(), key, def);
    }

    public static void setSystemSettings(Context context, String key, Object value) {
        if (value instanceof Long) {
            Settings.Global.putLong(context.getContentResolver(), key, (long) value);
        } else if (value instanceof String) {
            Settings.Global.putString(context.getContentResolver(), key, (String) value);
        } else if (value instanceof Integer) {
            Settings.Global.putInt(context.getContentResolver(), key, (int) value);
        } else if (value instanceof Float) {
            Settings.Global.putFloat(context.getContentResolver(), key, (float) value);
        }
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getNowTime(String format) {
        final Date date = new Date(System.currentTimeMillis());
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 获取SD卡可用容量
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        try {
            StatFs stat = new StatFs(FileUtils.getSDCardStoragePath(context));
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return Formatter.formatFileSize(context, blockSize * availableBlocks);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    /**
     * 获取内部存储可用容量
     *
     * @return
     */
    public static String getSystemRomAvailableSize(Context context) {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return Formatter.formatFileSize(context, blockSize * availableBlocks);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static long getSystemRomAvailableSize() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setSystemSettings(Context context, String key, int value) {
        Settings.Global.putInt(context.getContentResolver(), key, value);
    }

    public static void setSystemSettings(Context context, String key, String value) {
        Settings.Global.putString(context.getContentResolver(), key, value);
    }

    public static int getSystemSettings(Context context, String key) {
        return Settings.Global.getInt(context.getContentResolver(), key, 0);
    }

    /**
     * 获取内存总大小
     *
     * @return
     */
    public static long getSystemRomTotalSize() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            int blockCount = stat.getBlockCount();
            return blockSize * blockCount;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取系统可用的内存大小
     *
     * @return
     */
    public static String getSystemAvaialbeMemorySize(Context context) {
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        long memSize = memoryInfo.availMem;
        //字符类型转换
        return Formatter.formatFileSize(context, memSize);
    }

    public static PackageInfo getProgramInfo(String packageName, Context context) {
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            String tempPackageName = packageInfo.packageName;
            if (packageName.equals(tempPackageName)) {
                return packageInfo;
            }
        }
        return null;
    }

    /**
     * 获取sim卡iccId号
     *
     * @return
     */
    public static String getIccid(Context context) {
        //判断是否大于安卓10
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "1234567890";
        } else {
            final TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") final String iccid = telephonyManager.getSimSerialNumber();
            return iccid == null ? "1234567890" : iccid;
        }
    }

    /**
     * 静默安装apk
     *
     * @param path
     * @param isAutoStart
     * @param startClass
     */
    public static void silenceInstallApp(String path, boolean isAutoStart, String startClass, Context context) {
        Intent intent = new Intent("android.intent.action.SILENT_PACKAGE_INSTALL");
        intent.putExtra("silent_install_file", path);
        intent.putExtra("silent_install_auto_start", isAutoStart);
        intent.putExtra("silent_install_start_clazz", startClass);
        context.sendBroadcast(intent);
    }


    /**
     * 获取系统配置
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getSystemProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, "unknown"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }


    /**
     * 设置系统音量
     *
     * @param context
     * @param type
     * @param value
     */
    public static void setVolume(Context context, int type, int value) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int max = mAudioManager.getStreamMaxVolume(type);
        mAudioManager.setStreamVolume(type, value, AudioManager.FLAG_SHOW_UI);
    }

    public static void showAppDetail(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean createAp(Context context, boolean isOpen, String SSID, String pwd) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
            Settings.System.putInt(context.getContentResolver(), "wifi_hotspot_auto_disable", 0);
            Settings.System.putInt(context.getContentResolver(), "wifi_hotspot_max_client_num", 2);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = SSID;
            netConfig.preSharedKey = pwd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(4);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            return (boolean) method.invoke(wifiManager, netConfig, isOpen);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setSystemTime(long currentTime, Context context) {
        Settings.Global.putInt(context.getContentResolver(), "auto_time", 0);
        Intent intent = new Intent(SETTING_DATE);
        intent.putExtra("now_time", currentTime);
        context.sendBroadcast(intent);
    }

    /**
     * 设置数据流量开关
     *
     * @param context
     * @param status
     */
    public static void setMobileData(Context context, boolean status) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, status);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取流量状态
     *
     * @param context
     * @param arg
     * @return
     */
    public static boolean getMobileDataState(Context context, Object[] arg) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }
            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);
            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
            return isOpen;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 关机
     *
     * @param context
     */
    public static void shutdown(Context context) {
        Intent intent = new Intent(SHUTDOWN_SYSTEM);
        context.sendBroadcast(intent);
    }

    /**
     * 重启
     *
     * @param context
     */
    public static void reboot(Context context) {
        Intent intent = new Intent(REBOOT_SYSTEM);
        context.sendBroadcast(intent);
    }


    /**
     * 判断服务是否正在运行
     *
     * @param context
     * @param className 判断的服务名字：包名+类名
     * @return true在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        //获取所有的服务
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (services != null && services.size() > 0) {
            for (ActivityManager.RunningServiceInfo service : services) {
                if (className.equals(service.service.getClassName())) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 获取内部存储可用容量
     *
     * @return
     */
    public static long getSystemRomAvailableSizeLong() {
        try {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        } catch (Exception e) {
            return -1L;
        }
    }

    public static void setSystemProperties(String key, String value, BaseApplication context) {
        Intent intent = new Intent(SYSTEM_PROPERTIES);
        intent.putExtra("system_properties_key", key);
        intent.putExtra("system_properties_value", value);
        context.sendBroadcast(intent);
    }

    public static String storageSizeToStr(long size) {
        return Formatter.formatFileSize(BaseApplication.getInstance(), size);
    }

    /**
     * 获取系统音量
     *
     * @param context
     * @param type
     */
    public static int getVolume(Context context, int type) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int max = mAudioManager.getStreamMaxVolume(type);
        return mAudioManager.getStreamVolume(type);
    }

    /**
     * 亮/灭屏幕
     *
     * @param context
     * @param mode
     */
    public static void setDisplayPowerMode(Context context, int mode) {
        if (mode == POWER_MODE_OFF) {
            Intent intent = new Intent("lingmao.intent.action.SCREEN_OFF");
            context.sendBroadcast(intent);
        } else if (mode == POWER_MODE_NORMAL) {
            Intent intent = new Intent("lingmao.intent.action.SCREEN_ON");
            context.sendBroadcast(intent);
        }
    }
}
