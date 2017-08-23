package com.bruce.android.ui;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;

import com.bruce.android.R;

public class ProgressBarDialog extends Dialog{

	
	private Window window = null;
	public TextProgressBar textProgressBar = null;
	public Button textview = null;
	public ProgressBarDialog(Context context) {
		super(context);
	}

	public ProgressBarDialog(Context context, int theme) 
	{
		super(context, theme);
	}
	
	public void setDialog(int layoutResID) {
		setContentView(layoutResID);
		textProgressBar = (TextProgressBar) findViewById(R.id.updateProgressbar);
		textview = (Button)findViewById(R.id.dialog_progressbar_cancel);

		window = getWindow(); // 得到对话框
		window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画

		// 设置触摸对话框意外的地方不能取消对话框
		setCanceledOnTouchOutside(false);
		// 阻止返回键响应
		setCancelable(false);
	}

	// 如果自定义的Dialog声明为局部变量，就可以调用下面两个方法进行显示和消失，全局变量则无所谓
	/**
	 * 显示dialog
	 */
	public void showDialog() {
		show();
	}

	/**
	 * 关闭dialog
	 */
	public void Closedialog() {
		dismiss();
	}

}
