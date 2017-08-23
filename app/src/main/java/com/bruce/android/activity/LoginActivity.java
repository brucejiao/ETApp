package com.bruce.android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.bruce.android.R;
import com.bruce.android.common.AppContext;
import com.bruce.android.http.Caller;
import com.bruce.android.http.HttpClient;
import com.bruce.android.http.HttpResponseHandler;
import com.bruce.android.http.RestApiResponse;
import com.bruce.android.model.LoginModel;
import com.bruce.android.ui.UIHelper;
import com.bruce.android.ui.swipebacklayout.SwipeBackActivity;
import com.bruce.android.utils.CommUtil;
import com.bruce.android.utils.SharedPreferences;
import com.bruce.android.utils.UpdateVersionService;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

import static com.bruce.android.utils.Contents.CARDNUMBER;
import static com.bruce.android.utils.Contents.REMARK;
import static com.bruce.android.utils.Contents.SHARE_LOGIN_ISLOGIN;


/**
 login
 */
public class LoginActivity extends SwipeBackActivity {
    private LoginActivity mContext;
    @Bind(R.id.phone)
    EditText mUserName;
    @Bind(R.id.code)
    EditText mCode;
    @Bind(R.id.btnSure)
    Button mBtnSure;
    @Bind(R.id.btnClose)
    Button mBtnClose;
    @Bind(R.id.btnForgetPaw)
    Button mBtnForgetPaw;//忘记密码
    @Bind(R.id.btnRegister)
    Button mBtnRegister;//注册
    private boolean isLogin = false;//登录状态
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tax);
        ButterKnife.bind(this);
        AppContext.getInstance();
        mContext = this;
        mUserName.setText("12345678902");
        mCode.setText("123123");
        // 检测版本是否更新
        UpdateVersionService updateService = new UpdateVersionService(LoginActivity.this);
        updateService.checkUpdate();
    }

    @OnClick(R.id.btnSure)
    public void setmBtnSure(View view){
        progress = CommUtil.showProgress(LoginActivity.this, "正在加载数据，请稍候...");
        isLogin = false;
        String userName = mUserName.getText().toString();
        String passWord = mCode.getText().toString();
        SharedPreferences.getInstance().putBoolean(SHARE_LOGIN_ISLOGIN,isLogin);
        HashMap<String, String> params = new HashMap<>();
        params.put("secret", Caller.getLoginParams(userName, passWord,"0"));
        HttpClient.get(Caller.BASE_IP,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                LoginModel loginModel = JSON.parseObject(value, LoginModel.class);
                String cardNumber = loginModel.getCard_number();
                String remark = loginModel.getRemark();
                SharedPreferences.getInstance().putString(CARDNUMBER,cardNumber);
                SharedPreferences.getInstance().putString(REMARK,remark);
                CommUtil.showToast(message,mContext);
                UIHelper.showHome(LoginActivity.this);
                if (progress != null)
                {
                    progress.dismiss();
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                if (progress != null)
                {
                    progress.dismiss();
                }
                CommUtil.showToast("登录失败",mContext);
            }
        });
    }

    @OnClick({R.id.btnClose,R.id.btnForgetPaw,R.id.btnRegister})
    public void setmBtnClose(View view){
        switch (view.getId())
        {
            //关闭
            case R.id.btnClose:
                finish();
                break;
            //忘记密码
            case R.id.btnForgetPaw:
                CommUtil.showToast("忘记密码",mContext);
                break;
            //注册
            case R.id.btnRegister:
                UIHelper.showRegisterActivity(mContext);
                break;

            default:break;
        }

    }
}
