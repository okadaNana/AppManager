package com.owen.appplus.bean;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.owen.appplus.utils.BitmapUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by mike on 16/7/1.
 */
public class AppDataEngine {

    private static Context sContext;
    private static PackageManager sPackageManager;

    public static void init(Context context) {
        sContext = context;
        sPackageManager = sContext.getPackageManager();
    }

    public static Observable<List<BeanApp>> getInstalledApp() {
        return Observable.from(sPackageManager.getInstalledPackages(PackageManager.GET_META_DATA))
                .map(new Func1<PackageInfo, BeanApp>() {
                    @Override
                    public BeanApp call(PackageInfo packageInfo) {
                        return wrapBeanApp(packageInfo);
                    }
                })
                .filter(new Func1<BeanApp, Boolean>() {
                    @Override
                    public Boolean call(BeanApp beanAppInfo) {
                        return !isSystemApp(beanAppInfo.getPackageName());
                    }
                })
                .toList();
    }

    private static BeanApp wrapBeanApp(PackageInfo packageInfo) {
        BeanApp bean = new BeanApp();

        ApplicationInfo applicationInfo = packageInfo.applicationInfo;

        bean.setName(sPackageManager.getApplicationLabel(applicationInfo).toString());
        bean.setPackageName(packageInfo.packageName);
        bean.setIcon(BitmapUtil.drawableToBitmap(sPackageManager.getApplicationIcon(applicationInfo)));
        bean.setVersionName(packageInfo.versionName);
        bean.setVersionCode(packageInfo.versionCode);
        bean.setSrcPath(applicationInfo.sourceDir);

        return bean;
    }

    public static Observable<List<BeanApp>> getRunningApp() {
        final ActivityManager activityManager = (ActivityManager) sContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        return Observable.from(activityManager.getRunningAppProcesses())
                .flatMap(new Func1<ActivityManager.RunningAppProcessInfo, Observable<ApplicationInfo>>() {
                    @Override
                    public Observable<ApplicationInfo> call(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
                        ApplicationInfo appInfo;
                        try {
                            appInfo = sPackageManager.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA);
                        } catch (PackageManager.NameNotFoundException e) {
                            appInfo = null;
                        }
                        return Observable.just(appInfo);
                    }
                })
                .filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo applicationInfo) {
                        return applicationInfo != null;
                    }
                })
                .flatMap(new Func1<ApplicationInfo, Observable<PackageInfo>>() {
                    @Override
                    public Observable<PackageInfo> call(ApplicationInfo applicationInfo) {
                        PackageInfo packageInfo;
                        try {
                            packageInfo = sPackageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_META_DATA);
                        } catch (PackageManager.NameNotFoundException e) {
                            packageInfo = null;
                        }
                        return Observable.just(packageInfo);
                    }
                })
                .map(new Func1<PackageInfo, BeanApp>() {
                    @Override
                    public BeanApp call(PackageInfo packageInfo) {
                        return wrapBeanApp(packageInfo);
                    }
                })
                .filter(new Func1<BeanApp, Boolean>() {
                    @Override
                    public Boolean call(BeanApp beanApp) {
                        return !isSystemApp(beanApp.getPackageName());
                    }
                })
                .toList();

    }

    private static boolean isSystemApp(String packageName) {
        try {
            PackageInfo packageInfo = sPackageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo != null && ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void addApp(BeanApp beanApp) {
        // TODO: 16/7/3  
    }

    public static BeanApp getApp() {
        return null;
        // TODO: 16/7/3 x 
    }

    public static void updateApp(BeanApp beanApp) {
        // TODO: 16/7/3  
    }

    public static void deleteApp(BeanApp beanApp) {
        // TODO: 16/7/3  
    }
}
