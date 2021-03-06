package com.bruce.android.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bruce.android.common.AppContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 工具类，封装常用的操作方法
 */
public class CommUtil {
	private static Boolean isExit = false;

	/**
	 * Alert提示框
	 * 
	 * @param msg
	 *            需要提示的消息
	 * @param context
	 *            当前Activity的Context对象
	 */
	public static void showAlert(String msg, final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg).setCancelable(false).setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Alert提示框
	 * 
	 * @param msg
	 *            需要提示的消息
	 * @param context
	 *            当前Activity的Context对象
	 */
	public static void showAlert(String msg, final Context context, OnClickListener confirClick) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg).setCancelable(false).setPositiveButton("确定", confirClick);

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Alert提示框
	 * 
	 * @param msg
	 *            需要提示的消息
	 * @param context
	 *            当前Activity的Context对象
	 *
	 */
	public static void showAlert(String msg, final Context context, OnClickListener confirClick,
			OnClickListener cancelClick) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg).setCancelable(false).setPositiveButton("确定", confirClick).setNegativeButton("取消",
				cancelClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 退出应用，带提示
	 * 
	 * @param context
	 */
	public static void quitApp(final Context context) {

		new AlertDialog.Builder(context).setTitle("退出移动办公").setMessage("确认是否立即退出移动办公吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						quitAppDirect(context);
					}
				}).setNegativeButton("返回", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作

					}
				}).show();
	}

	/**
	 * 直接退出应用，无提示
	 * 
	 * @param context
	 */
	public static void quitAppDirect(final Context context) {

		// if(CommUtil.isServiceRunning(context,"com.aisino.mobioaclient.page.pedometer.StepService")){
		//
		// System.out.println("最小化");
		// Intent home = new Intent(Intent.ACTION_MAIN);
		// home.addCategory(Intent.CATEGORY_HOME);
		// context.startActivity(home);
		//
		// }else{
		 AppContext.getInstance().exit();
		// }
	}

	public static boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {

			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 消息提示
	 * 
	 * @param msg
	 *            提示的消息
	 * @param context
	 *            当前Activity的Context对象
	 */
	public static void showToast(String msg, final Context context) {
		Toast toast = new Toast(context);
		toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();// 显示
	}

	/**
	 * 获取AndroidManifest.xml中App meteData
	 * 
	 * @param metaDataName
	 * @param context
	 * @return
	 */
	public static String getAppMetaData(String metaDataName, final Context context) {
		ApplicationInfo info;
		String strData = "";
		try {
			String a = context.getPackageName();
			info = context.getPackageManager().getApplicationInfo(a, PackageManager.GET_META_DATA);
			strData = info.metaData.getString(metaDataName);
			System.out.println(strData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						Log.v("网络状态", "连通状态...........");

						return true;
					}
				}
			}
		} catch (Exception e) {
			Log.v("error", e.toString());
		}
		Log.v("网络状态", "网络不通...........");

		return false;
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNullOrBlank(Object value) {
		if (null == value || value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNullOrBlank(List value) {
		if (null == value || value.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String objectString(Object value) {

		if (value == null || "".equals(value)) {
			return "";
		} else {
			return value.toString();
		}

	}

	// public static boolean isDevMode() {
	// return 0 == Constant.DEV_MODE;
	// }



	public static String DateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}





	/**
	 * 读取assest下文件
	 * 
	 * @param mContext
	 * @param fileName
	 * @return
	 */
	public static String getFromAssets(Context mContext, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			LogUtil.i("vvvvvvvvv\t\t", line);
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 自定义listView每个Itenview的高度
		public static void setListViewHeightBasedOnChildren(ListView listView, BaseAdapter adapter) {
			// 获取listview的adapter
			if (adapter == null) {
				return;
			}
			// 固定列宽，有多少列
			int col = 1;// listView.getNumColumns();
			int totalHeight = 0;
			// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
			// listAdapter.getCount()小于等于8时计算两次高度相加
			for (int i = 0; i < adapter.getCount(); i += col) {
				// 获取listview的每一个item
				View listItem = adapter.getView(i, null, listView);
				listItem.measure(0, 0);
				// 获取item的高度和
				totalHeight += listItem.getMeasuredHeight();
			}

			// 获取listview的布局参数
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			// 设置高度
			params.height = totalHeight;
			// 设置margin
			((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
			// 设置参数
			listView.setLayoutParams(params);
		}

	/**
	 * 加载进度条
	 * @param activity
	 * @param hintText
	 * @return
	 * 用法
	 * private ProgressDialog progress;
	 *  progress = CommUtil.showProgress(mContext, "正在加载数据，请稍候...");
	 * if (progress != null)
	{
	progress.dismiss();
	}
	 */
	public static ProgressDialog showProgress(Activity activity, String hintText) {
		Activity mActivity = null;
		if (activity.getParent() != null) {
			mActivity = activity.getParent();
			if (mActivity.getParent() != null) {
				mActivity = mActivity.getParent();
			}
		} else {
			mActivity = activity;
		}
		final Activity finalActivity = mActivity;
		ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
		window.getWindow().setGravity(Gravity.CENTER);

		window.setCancelable(false);
		return window;
	}
}
