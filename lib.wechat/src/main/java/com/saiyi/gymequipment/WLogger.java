package com.saiyi.gymequipment;

import android.util.Log;

/**
 * Created by JingNing on 2018-07-13 17:11
 */
public class WLogger {

    //设为false关闭日志
    private static boolean LOG_ENABLE = true;

    public static void i(String tag, String msg){
        if (LOG_ENABLE){
            Log.i(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (LOG_ENABLE){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (LOG_ENABLE){
            Log.d(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (LOG_ENABLE){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (LOG_ENABLE){
            Log.e(tag, msg);
        }
    }

    public static void isDebug(boolean isDebug){
        LOG_ENABLE = isDebug;
    }
}
