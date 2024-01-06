package com.ph.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.ph.common.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WifiUtils {
    /**
     * wifi是否打开
     *
     * @return
     */
    public static boolean isWifiEnable() {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isEnable = false;
        if (wifiManager != null) {
            if (wifiManager.isWifiEnabled()) {
                isEnable = true;
            }
        }
        return isEnable;
    }

    /**
     * 打开WiFi
     */
    public static void openWifi() {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && !isWifiEnable()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WiFi
     */
    public static void closeWifi() {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && isWifiEnable()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取WiFi列表
     *
     * @return
     */
    public static List<ScanResult> getWifiList() {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> resultList = new ArrayList<>();
        if (wifiManager != null && isWifiEnable()) {
            resultList.addAll(wifiManager.getScanResults());
        }
        return resultList;
    }

    /**
     * 有密码连接
     *
     * @param ssid
     * @param pws
     */
    public static void connectWifiPws(String ssid, String pws) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                        .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
                int netId = wifiManager.addNetwork(getWifiConfig(ssid, pws, true));
                wifiManager.enableNetwork(netId, true);
                executorService.shutdown();
            }
        }, 10, TimeUnit.SECONDS);
    }

    /**
     * 无密码连接
     *
     * @param ssid
     */
    public static void connectWifiNoPws(String ssid) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                        .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
                int netId = wifiManager.addNetwork(getWifiConfig(ssid, "", false));
                wifiManager.enableNetwork(netId, true);
                executorService.shutdown();
            }
        }, 10, TimeUnit.SECONDS);
    }

    /**
     * wifi设置
     *
     * @param ssid
     * @param pws
     * @param isHasPws
     */
    private static WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws) {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        WifiConfiguration tempConfig = isExist(ssid);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }
        if (isHasPws) {
            config.preSharedKey = "\"" + pws + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /**
     * 得到配置好的网络连接
     *
     * @param ssid
     * @return
     */
    private static WifiConfiguration isExist(String ssid) {
        WifiManager wifiManager = (WifiManager) BaseApplication.getInstance()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }
}
