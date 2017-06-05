package com.yshstudio.originalproduct.tools;

import android.util.Log;

import com.yshstudio.originalproduct.inc.Ini;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class LogUtils {

    // 是否打开日志
    private final static Boolean isLog = Ini.IS_DEBUG;

    public static void logE(String tag, String log) {
        if (isLog) {
            Log.e(tag, log);
        }
    }

    public static void logV(String tag, String log) {
        if (isLog) {
            Log.v(tag, log);
        }
    }

    public static void logD(String tag, String log) {
        if (isLog) {
            Log.d(tag, log);
        }
    }

    public static void logI(String tag, String log) {
        if (isLog) {
            Log.i(tag, log);
        }
    }

    public static void logW(String tag, String log){
        if (isLog) {
            Log.w(tag, log);
        }
    }

    public static void logW(String tag, String log,Throwable tr) {
        if (isLog) {
            Log.w(tag, log, tr);
        }
    }











}
