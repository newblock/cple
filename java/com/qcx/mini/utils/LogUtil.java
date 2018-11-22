package com.qcx.mini.utils;

import android.util.Log;

import com.qcx.mini.BuildConfig;

/**
 * Created by Administrator on 2017/10/19.
 */

public class LogUtil {
    private static final String TAG="dddddd";

    public static void i(String text){
        i(TAG,text);
    }
    public static void e(String text){
        Log.e(TAG,text);
    }

    public static void i(String tag, String msg) {  //信息太长,分段打印
        int max_str_length = 2001 - tag.length();
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        Log.i(tag, msg);
    }
}
