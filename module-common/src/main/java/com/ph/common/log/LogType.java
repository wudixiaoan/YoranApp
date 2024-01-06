package com.ph.common.log;

public enum LogType {
    mtkLog("mtklog", "mtklog"),
    rawDataLog("rawDataLog", "rawData"),
    appLog("appLog", "app"),
    inventoryLog("appLog", "inventory"),
    appShutdownLog("appLog", "app_shutdown"),
    inventoryShutdownLog("appLog", "inventory_shutdown");

    private String type;
    private String head;

    LogType(String type, String head) {
        this.type = type;
        this.head = head;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
