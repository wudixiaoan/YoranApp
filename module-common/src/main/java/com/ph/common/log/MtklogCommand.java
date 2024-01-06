package com.ph.common.log;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

public class MtklogCommand {
    private static final String TAG =  MtklogCommand.class.getSimpleName();

    private static final String SEND_BROADCAST = "am broadcast -a com.mediatek.mtklogger.ADB_CMD";
    private static final String cmdName = " -e cmd_name ";
    private static final String cmdTarget = " --ei cmd_target ";

    private enum Cmd{
        start,
        stop,
        clear_all_logs,
        switch_taglog,
        set_auto_start_0,
        set_auto_start_1,
        set_log_size_
    };

    public static void sendShowMtkloggerCmd() {
        String cmd = "am start -n com.mediatek.mtklogger/com.mediatek.mtklogger.MainActivity --user '0'";
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendShowMtkloggerCmd.result:" +  result);
    }

    public static void sendStartMtkloggerCmd(int logLevel) {
        String cmd = SEND_BROADCAST + cmdName + Cmd.start + cmdTarget + logLevel;
        XLog.i(TAG, "sendStartMtkloggerCmd.cmd:" + cmd);
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendStartMtkloggerCmd.result:" +  result);
    }

    public static void sendStopMtkloggerCmd(int logLevel) {
        String cmd = SEND_BROADCAST + cmdName + Cmd.stop + cmdTarget + logLevel;
        XLog.i(TAG, "sendStopMtkloggerCmd.cmd:" + cmd);
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendStopMtkloggerCmd.result:" +  result);
    }

    public static void sendClearMtkloggerCmd() {
        String cmd = "rm -rf storage/emulated/0/mtklog";
        XLog.i(TAG, "sendClearMtkloggerCmd.cmd:" + cmd);
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendClearMtkloggerCmd.result:" +  result);
    }

    public static void sendAutoStartCmd(int on, int logLevel){
        String cmd = SEND_BROADCAST;
        if(on > 0){
            cmd +=cmdName + Cmd.set_auto_start_1 + cmdTarget + logLevel;
        }else{
            cmd +=cmdName + Cmd.set_auto_start_0 + cmdTarget + logLevel;
        }
        XLog.i(TAG, "sendAutoStartCmd.cmd:" + cmd);
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendAutoStartCmd.result:" +  result);
    }

    public static void sendSetMtkloggerSizeCmd(int size, int logLevel){
        String cmd = SEND_BROADCAST;
        if(size > 300){
            cmd +=cmdName + Cmd.set_log_size_ + size + cmdTarget + logLevel;
        }else{
            cmd +=cmdName + Cmd.set_log_size_ + 300 + cmdTarget + logLevel;
        }
        XLog.i(TAG, "sendAutoStartCmd.cmd:" + cmd);
        String result = execCmd(cmd, null);
        Log.d(TAG, "sendAutoStartCmd.result:" +  result);
    }

    public static String execCmd(String command, File dir) {
        String[] cmd = new String[] { "sh", "-c", command};
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                XLog.i(TAG, bufrIn.readLine());
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                XLog.i(TAG, bufrError.readLine());
                result.append(line).append('\n');
            }
        } catch (Exception e) {
            Log.d(TAG, "execCmd.e:" +  e);
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            if (process != null) {
                process.destroy();
            }
        }

        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }
}
