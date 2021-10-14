package com.kisen.router;

import android.util.Log;

/**
 * 日志工具类
 * Created by huang on 2017/3/27.
 */
@SuppressWarnings("all")
public class RouteLog {

    private static final String TAG = RouteLog.class.getSimpleName();
    private static boolean DEBUG = BuildConfig.DEBUG;

    @SuppressWarnings("unused")
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    @SuppressWarnings("unused")
    public static void v(String msg) {
        v(TAG, msg);
    }

    @SuppressWarnings("all")
    public static void v(String title, String msg) {
        if (DEBUG)
            Log.v(title, msg);
    }

    @SuppressWarnings("unused")
    public static void d(String msg) {
        d(TAG, msg);
    }

    @SuppressWarnings("all")
    public static void d(String title, String msg) {
        if (DEBUG)
            Log.d(title, msg);
    }

    @SuppressWarnings("unused")
    public static void i(String msg) {
        i(TAG, msg);
    }

    @SuppressWarnings("all")
    public static void i(String title, String msg) {
        if (DEBUG)
            Log.i(title, msg);
    }

    @SuppressWarnings("unused")
    public static void w(String msg) {
        w(TAG, msg);
    }

    @SuppressWarnings("all")
    public static void w(String title, String msg) {
        if (DEBUG)
            Log.w(title, msg);
    }

    @SuppressWarnings("unused")
    public static void e(String msg) {
        e(TAG, msg);
    }

    @SuppressWarnings("all")
    public static void e(String title, String msg) {
        if (DEBUG)
            Log.e(title, msg);
    }
}
