package com.ph.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathUtils {
    public static boolean section(int base, int weight, int offset) {
        return (base + offset >= weight) && (base - offset <= weight);
    }

    public static boolean section(int base, int weight, float offset) {
        return (base * (1 - offset) <= weight) && (base * (1 + offset) >= weight);
    }

    public static String random(int n) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < n; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

    //计算标准差
    public static int StandardDiviation(int[] x, int average) {
        int m = x.length;
        long dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.pow((x[i] - average), 2);
        }
        return (int) Math.sqrt(dVar / m);
    }

    public static float StandardDiviation(float[] x) {
        int m = x.length;
        float dAve = getAverage(x);
        double dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.pow((x[i] - dAve), 2);
        }
        return (float) Math.sqrt(dVar / m);
    }

    public static int StandardDiviation(int[] x) {
        int m = x.length;
        int dAve = getAverage(x);
        long dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.pow((x[i] - dAve), 2);
        }
        return (int) Math.sqrt(dVar / m);
    }

    //计算方差
    public static double Variance(float[] x) {
        int m = x.length;
        double dAve = getTotal(x) / m;
        double dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.pow((x[i] - dAve), 2);
        }
        return dVar / m;
    }

    public static long Variance(int[] x) {
        int m = x.length;
        long dAve = getTotal(x) / m;
        long dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.pow((x[i] - dAve), 2);
        }
        return dVar / m;
    }

    //均差
    public static int MeanDiviation(int[] x) {
        int m = x.length;
        int dAve = getAverage(x);
        long dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.abs(x[i] - dAve);
        }
        return (int) (dVar / m);
    }

    public static int MeanDiviation(int[] x, int average) {
        int m = x.length;
        //int dAve = getAverage(x);
        long dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.abs(x[i] - average);
        }
        return (int) (dVar / m);
    }

    public static float MeanDiviation(float[] x) {
        int m = x.length;
        float dAve = getAverage(x);
        double dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += Math.abs(x[i] - dAve);
        }
        return (float) (dVar / m);
    }

    //二维数组行列交换
    public static float[][] arrayRowToColumn(float[][] datas) {
        int row = datas.length;
        int column = datas[0].length;
        float[][] tempdatas = new float[column][row];
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                tempdatas[j][i] = datas[i][j];
            }
        }
        return tempdatas;
    }

    public static int[][] arrayRowToColumn(int[][] datas) {
        int row = datas.length;
        int column = datas[0].length;
        int[][] tempdatas = new int[column][row];
        for (int i = 0; i < datas.length; i++) {
            for (int j = 0; j < datas[i].length; j++) {
                tempdatas[j][i] = datas[i][j];
            }
        }
        return tempdatas;
    }

    //List转化为数组
    public static int[][] intListToArray(List<int[]> data_list) {

        if (data_list.size() > 0) {
            int item_length = data_list.size();
            int item_num = data_list.get(0).length;
            int[][] tempdatas = new int[item_num][item_length];

            for (int i = 0; i < item_length; i++) {
                for (int j = 0; j < item_num; j++) {
                    tempdatas[j][i] = data_list.get(i)[j];
                }
            }
            return tempdatas;
        } else {
            return null;
        }

    }

    public static float[][] floatListToArray(List<float[]> data_list) {

        if (data_list.size() > 0) {
            int item_length = data_list.size();
            int item_num = data_list.get(0).length;
            float[][] tempdatas = new float[item_num][item_length];

            for (int i = 0; i < item_length; i++) {
                for (int j = 0; j < item_num; j++) {
                    tempdatas[j][i] = data_list.get(i)[j];
                }
            }
            return tempdatas;
        } else {
            return null;
        }

    }

    //求和
    public static long getTotal(int[] datas) {
        long total = 0;
        for (int item : datas) {
            total += item;
        }
        return total;
    }

    //求和
    public static long getTotal(long[] datas) {
        long total = 0;
        for (long item : datas) {
            total += item;
        }
        return total;
    }

    public static double getTotal(float[] datas) {
        double total = 0;
        for (float item : datas) {
            total += item;
        }
        return total;
    }

    //求均值
    public static int getAverage(int[] data_array) {
        if (data_array.length > 0) {
            return (int) (getTotal(data_array) / data_array.length);
        }
        return 0;
    }

    public static float getAverage(float[] data_array) {
        if (data_array.length > 0) {
            return (float) (getTotal(data_array) / data_array.length);
        }
        return 0.0f;
    }

    //更新数组最后一位的值
    public static int[] remapDataArray(int[] data_array, int new_data) {
        int length = data_array.length;
        for (int i = 0; i < length - 1; i++) {
            data_array[i] = data_array[i + 1];
        }
        data_array[length - 1] = new_data;
        return data_array;
    }

    public static int[] initNegativeArray(int len) {
        int[] ints = new int[len];
        for (int i = 0; i < len; i++) {
            ints[i] = -1;
        }
        return ints;
    }

    /**
     * 24位二进制有效数据
     *
     * @param data
     * @param len
     * @return
     */
    public static boolean isDataEqualTo2N(int data, int len) {
        if (data == 0 || data == 1 || len == 0) {
            return true;
        }
        int dataH = data >> len;
        int dataL = data - (dataH << len);

        if (dataH > 0 && dataL > 0) {
            return false;
        }
        if ((dataH + dataL) == 1) {
            return true;
        }
        int newLen = len / 2;
        return (isDataEqualTo2N(dataH, newLen) && isDataEqualTo2N(dataL, len - newLen));
    }

    // 快速排序 降序排列
    public static void QuickSort(int arr[], int start, int end) {
        if (start >= end)
            return;
        int i = start;
        int j = end;
        // 基准数
        int baseval = arr[start];
        while (i < j) {
            // 从右向左找比基准数小的数
            while (i < j && arr[j] >= baseval) {
                j--;
            }
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }
            // 从左向右找比基准数大的数
            while (i < j && arr[i] < baseval) {
                i++;
            }
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }
        // 把基准数放到i的位置
        arr[i] = baseval;
        // 递归
        QuickSort(arr, start, i - 1);
        QuickSort(arr, i + 1, end);
    }

    //获取数组后len长度的子数组
    public static int[] getSubArray(int arr[], int len) {
        int arrLen = arr.length;
        if (arrLen > len) {
            int[] subArr = new int[len];
            for (int i = 0; i < len; i++) {
                subArr[i] = arr[i];
            }
            return subArr;
        }
        return arr;
    }

    public static int[] moveArray(int array[], int move) {
        int len = array.length;
        int[] tempArray = new int[len];
        for (int i = move; i < len; i++) {
            tempArray[i - move] = array[i];
        }
        return tempArray;
    }

    //获取最小公倍数
    public static int getMinMultiple(int[] array) {
        int max = getMax(array);
        boolean stopFlag = false;
        while (!stopFlag) {
            stopFlag = true;
            for (int i = 0; i < array.length; i++) {
                stopFlag = stopFlag && (max % array[i] == 0);
                if (!stopFlag) {
                    int divisor = getMaxDivisor(max, array[i]);
                    max = max * array[i] / divisor;
                    break;
                }
            }
        }
        return max;
    }


    public static int getMax(int[] array) {
        int maxCount = 0;
        for (int i = 0; i < array.length; i++) {
            maxCount = Math.max(maxCount, array[i]);
        }
        return maxCount;
    }

    //获取最大公约数
    public static int getMaxDivisor(int a, int b) {
        int divisor = 2;
        boolean stopFlag = false;
        while (!stopFlag) {
            stopFlag = true;
            if (a % divisor == 0 && b % divisor == 0) {
                stopFlag = false;
                divisor++;
            }
        }
        return Math.max(1, divisor - 1);
    }

    /**
     * Integer list转int数组
     *
     * @param list
     * @return
     */
    public static int[] listToArr(List<Integer> list) {
        int[] res = new int[list.size()];
        int i = 0;
        for (Integer num : list) {
            res[i++] = num;
        }
        return res;
    }

    /**
     * Integer int数组转 integer list
     *
     * @param arr
     * @return
     */
    public static List<Integer> arrToList(int[] arr) {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            res.add(arr[i]);
        }
        return res;
    }
}
