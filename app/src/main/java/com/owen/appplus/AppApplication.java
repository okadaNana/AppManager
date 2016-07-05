package com.owen.appplus;

import android.app.Application;

import com.owen.appplus.bean.AppDataEngine;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Owen on 2016/7/1.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
        AppDataEngine.init(this);
    }
}
