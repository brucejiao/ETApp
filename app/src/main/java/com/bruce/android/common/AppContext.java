package com.bruce.android.common;

import android.app.Activity;
import android.app.Application;

import com.bruce.android.R;
import com.bruce.android.utils.LogUtil;
import com.ganxin.library.LoadDataLayout;
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
        //获取网络状态并给出相应的界面
//        getNetStatus();

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

    /**
     * 获取网络状态并给出相应的界面
     */
    private void getNetStatus(){
        LoadDataLayout.getBuilder()
                .setLoadingText("正在加载中,请稍后...")//getString(R.string.custom_loading_text)
                .setLoadingTextSize(16)
                .setLoadingTextColor(R.color.colorPrimary)
                //.setLoadingViewLayoutId(R.layout.custom_loading_view) //如果设置了自定义loading视图,上面三个方法失效
                .setEmptyImgId(R.drawable.ic_empty)
                .setErrorImgId(R.drawable.ic_error)
                .setNoNetWorkImgId(R.drawable.ic_no_network)
                .setEmptyImageVisible(true)
                .setErrorImageVisible(true)
                .setNoNetWorkImageVisible(true)
                .setEmptyText("暂无数据")//getString(R.string.custom_empty_text)
                .setErrorText("数据加载错误,请重试...")//getString(R.string.custom_error_text)
                .setNoNetWorkText("请连接网络后重试...")//getString(R.string.custom_nonetwork_text)
                .setAllTipTextSize(16)
                .setAllTipTextColor(R.color.text_color_child)
                .setAllPageBackgroundColor(R.color.pageBackground)
                .setReloadBtnText("点击重新加载...")//getString(R.string.custom_reloadBtn_text)
                .setReloadBtnTextSize(16)
                .setReloadBtnTextColor(R.color.colorPrimary)
                .setReloadBtnBackgroundResource(R.drawable.selector_btn_normal)
                .setReloadBtnVisible(true)
                .setReloadClickArea(LoadDataLayout.FULL);
    }

}