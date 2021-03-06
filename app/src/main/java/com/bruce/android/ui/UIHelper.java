package com.bruce.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bruce.android.activity.HouseDetailActivity;
import com.bruce.android.activity.LoginActivity;
import com.bruce.android.activity.MainActivity;
import com.bruce.android.activity.RegisterActivity;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 */
public class UIHelper {

	public final static String TAG = "UIHelper";

	public final static int RESULT_OK = 0x00;
	public final static int REQUEST_CODE = 0x01;

	public static void ToastMessage(Context cont, String msg) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
        if(cont == null || msg <= 0) {
            return;
        }
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
        if(cont == null || msg == null) {
            return;
        }
		Toast.makeText(cont, msg, time).show();
	}

    public static void showHome(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登录界面
     * @param context
     */
    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public static void showHouseDetailActivity(Activity context){
        Intent intent = new Intent(context, HouseDetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 注册界面
     * @param context
     */
    public static void showRegisterActivity(Activity context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

}
