package com.bruce.android.common;

import android.app.Activity;
import android.app.Application;

import com.bruce.android.utils.LogUtil;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;

public class AppContext extends Application {

    private static AppContext app;

    private LinkedList<Activity> activitys = null;


    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        activitys = new LinkedList<Activity>();
        //内存泄漏检测
        LeakCanary.install(this);
        registerUncaughtExceptionHandler();



    }


    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activitys != null && activitys.size() > 0) {
            if (!activitys.contains(activity)) {
                activitys.add(activity);
                LogUtil.i("==activity==", activity+"\t<---->length:\t"+activitys.size());
            }
        } else {

            activitys.add(activity);
            LogUtil.i("==activity==", activity+"\t<---->length:\t"+activitys.size());
        }
    }

    // 遍历所有Activity并finish
    public void exitAll() {

        exit();
        System.exit(0);
    }

    public void exit() {

        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }

    }


    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

}