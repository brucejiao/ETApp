package com.bruce.android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.bruce.android.R;
import com.bruce.android.http.Caller;
import com.bruce.android.http.HttpClient;
import com.bruce.android.http.HttpResponseHandler;
import com.bruce.android.http.RestApiResponse;
import com.bruce.android.model.UpdateURL;
import com.bruce.android.ui.MessageDialog;
import com.bruce.android.ui.ProgressBarDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Request;

public class UpdateVersionService {

	private static final String TAG = "UpdateVersionService";

	private static final int DOWN = 1;// 用于区分正在下载
	private static final int DOWN_FINISH = 0;// 用于区分下载完成
	private HashMap<String, String> hashMap;// 存储跟心版本的xml信息
	private String fileSavePath;// 下载新apk的厨房地点
	private int progress;// 获取新apk的下载数据量,更新下载滚动条
	private boolean cancelUpdate = false;// 是否取消下载
	private Context context;
	private ProgressBarDialog progressBarDialog;
	private MessageDialog updateDialog;
	private String downloadPath = "http://222.190.120.106:9064/appDownload_Android/Android2.0/MobileTax_SVN.apk ";

	private Handler handler = new Handler() {// 更新ui

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch ((Integer) msg.obj) {
			case DOWN:
				progressBarDialog.textProgressBar.setProgress(progress);
				break;
			case DOWN_FINISH:
				CommUtil.showToast("文件下载完成,正在安装更新", context);
				installAPK();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            比较版本的xml文件地址(服务器上的)
	 * @param context
	 *            上下文
	 */
	public UpdateVersionService(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 检测是否可更新
	 * 
	 * @return
	 */
	public void checkUpdate() {

		isUpdateCookie();
	}

	/**
	 * 更新提示框
	 */
	private void showUpdateVersionDialog(final String url, final String flag) {

		updateDialog = new MessageDialog(context, R.style.dialogWindowAnim);
		updateDialog.setDialog(R.layout.dialog_message);
		updateDialog.txt_Title.setText("软件更新");
		updateDialog.txt_content.setText("检测到新版本，是否下载更新");
		updateDialog.dialog_button_details.setText("确定");
		updateDialog.dialog_button_cancel.setText("下次");

		updateDialog.dialog_button_details.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) {
				updateDialog.dismiss();
				// 显示下载对话框
				showDownloadDialog(url);
			}
		});

		if (null != flag && flag.equals("1")) 
		{// 不强制更新
			updateDialog.dialog_button_cancel.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View arg0) {
					updateDialog.dismiss();
				}
			});
		} else if (null != flag && flag.equals("0")) 
		{// 强制更新
			updateDialog.dialog_button_cancel.setVisibility(View.GONE);
		}

		updateDialog.show();
	}

	/**
	 * 下载的提示框
	 */
	protected void showDownloadDialog(final String apkUrl) {
		{
			progressBarDialog = new ProgressBarDialog(context, R.style.dialogWindowAnim);
			progressBarDialog.setDialog(R.layout.dialog_progressbar);
			progressBarDialog.textview.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					progressBarDialog.dismiss();
					// 设置取消状态
					cancelUpdate = true;
				}
			});

			progressBarDialog.show();
			// 下载文件
			downloadApk(apkUrl);
		}

	}

	/**
	 * 下载apk,不能占用主线程.所以另开的线程
	 */
	private void downloadApk(final String apkUrl) {
		new DownloadApkThread(apkUrl).start();

	}

	private int i;

	//将手机参数版本号和等级传给后台
	public void isUpdateCookie() {
		final int versionCode = getVersionCode(context);// 版本号
		String versionName = getAppVersionName(context);// 版本名称
		HashMap<String, String> params = new HashMap<>();
		params.put("secret", Caller.getUpdateParams(String.valueOf(versionCode)));

		HttpClient.get(Caller.BASE_IP,params, new HttpResponseHandler() {
			@Override
			public void onSuccess(RestApiResponse response) {
				String success = response.getSuccess();
				String message = response.getMsg();
				String value = response.getValue();
				// 解密获取的数据
				String isSuccess = "";
					if (success.equals("false")) {// false:提示更新
						UpdateURL updateURL = JSONObject.parseObject(value, UpdateURL.class);
						//更新apk地址url
						// String updateUrl =downloadPath;
						showUpdateVersionDialog(updateURL.getAndrUrl(), updateURL.getAndFlag());
					} else {// true:不需要更新
						return;
					}
			}

			@Override
			public void onFailure(Request request, Exception e) {

			}
		});


	}

	/**
	 * 获取当前版本和服务器版本.如果服务器版本高于本地安装的版本.就更新
	 * 
	 * @param context2
	 * @return
	 */
	private int getVersionCode(Context context2) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.bruce.android", 0).versionCode;
			// Toast.makeText(context, "当前版本是: " + versionCode,
			// Toast.LENGTH_SHORT).show();
			// showToast("当前版本是: " + versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;

	}

	// 获取当前版本号
	private String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo("com.aisino.mobiletaxclient", 0);
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 安装apk文件
	 */
	private void installAPK() {
		File apkfile = new File(fileSavePath, "aisino-tax-version" + ".apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		System.out.println("filepath=" + apkfile.toString() + "  " + apkfile.getPath());
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃

	}

	/**
	 * 卸载应用程序(没有用到)
	 */
	public void uninstallAPK() {
		Uri packageURI = Uri.parse("package:com.bruce.android");// com.example.updateversion
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);

	}

	/**
	 * 下载apk的方法
	 * 
	 * @author rongsheng
	 * 
	 */
	public class DownloadApkThread extends Thread {
		private String apkUrl = null;

		public DownloadApkThread(String url) {
			apkUrl = url;
		}

		@Override
		public void run() {
			super.run();
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					fileSavePath = sdpath + "AisinoTax";
					URL url = new URL(apkUrl);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5 * 1000);// 设置超时时间
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Charser", "GBK,utf-8;q=0.7,*;q=0.3");
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(fileSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(fileSavePath, "aisino-tax-version" + ".apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						Message message = new Message();
						message.obj = DOWN;
						handler.sendMessage(message);
						if (numread <= 0) {
							// 下载完成
							// 取消下载对话框显示
							// downLoadDialog.dismiss();

							progressBarDialog.dismiss();
							Message message2 = new Message();
							message2.obj = DOWN_FINISH;
							handler.sendMessage(message2);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
