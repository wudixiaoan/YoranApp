package com.ph.common.utils;

import java.util.Arrays;

public class ModbusUtils {
    private static final int CRC16_LENGTH = 2;

    /**
     * 计算给定数据的 CRC16 校验码。
     *
     * @param data 要计算的数据。
     * @return 该数据的 CRC16 校验码。
     */
    public static int calculateCRC16(byte[] data) {
        int crc = 0xFFFF;
        for (int i = 0; i < data.length; i++) {
            crc ^= (int) data[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) == 1) {
                    crc = (crc >>> 1) ^ 0xA001;
                } else {
                    crc >>>= 1;
                }
            }
        }

        return crc;
    }

    /**
     * 校验给定数据的 CRC16 校验码是否正确。
     *
     * @param data     要校验的数据。
     * @param checksum 给定的校验码。
     * @return 如果校验码正确，则返回 true；否则返回 false。
     */
    public static boolean checkCRC16(byte[] data, int checksum) {
        return calculateCRC16(data) == checksum;
    }

    /**
     * 校验给定数据的 CRC16 校验码是否正确。
     *
     * @param data
     * @return
     */
    public static boolean checkCRC16(byte[] data) {
        if (data.length <= CRC16_LENGTH) {
            return false;
        }
        byte[] bytes = Arrays.copyOfRange(data, data.length - 2, data.length);
        int checksum = bytes2int(bytes);
        byte[] range = Arrays.copyOfRange(data, 0, data.length - 2);
        return calculateCRC16(range) == checksum;
    }

    /**
     * int转byte数组
     *
     * @param checksum
     * @return
     */
    public static byte[] int2Bytes(int checksum) {
        byte[] crc16bytes = new byte[2];
        crc16bytes[0] = (byte) (checksum & 0xFF);
        crc16bytes[1] = (byte) ((checksum >> 8) & 0xFF);
        return crc16bytes;
    }

    /**
     * byte数组转int
     *
     * @param crc16bytes
     * @return
     */
    public static int bytes2int(byte[] crc16bytes) {
        if (crc16bytes.length < CRC16_LENGTH) {
            return 0;
        }
        return ((crc16bytes[1] & 0xFF) << 8) | (crc16bytes[0] & 0xFF);
    }


    /**
     * 数据增加CRC16校验位
     *
     * @param data
     * @return
     */
    public static byte[] addCRC16(byte[] data) {
        int checksum = calculateCRC16(data);
        byte[] crc16 = int2Bytes(checksum);
        byte[] sendData = new byte[data.length + 2];
        System.arraycopy(data, 0, sendData, 0, data.length);
        System.arraycopy(crc16, 0, sendData, data.length, crc16.length);
        return sendData;
    }

    /**
     * 截取接收数据的CRC校验位
     *
     * @param receive
     * @return
     */
    public static byte[] getCRC16(byte[] receive) {
        if (receive.length < 2) {
            return new byte[2];
        }
        return Arrays.copyOfRange(receive, receive.length - 2, receive.length);
    }
}
