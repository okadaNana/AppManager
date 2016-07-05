package com.owen.appplus.utils;

import android.os.Environment;

/**
 * Created by Owen on 2016/7/5.
 */
public class FileUtil {

    public static boolean isSDCardMounted() {
        String status = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(status);
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }
}
