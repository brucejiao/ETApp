package com.bruce.android.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bruce.android.R;
import com.bruce.android.http.Caller;
import com.bruce.android.http.HttpClient;
import com.bruce.android.http.HttpResponseHandler;
import com.bruce.android.http.RestApiResponse;
import com.bruce.android.utils.CommUtil;
import com.bruce.android.utils.LogUtil;
import com.bruce.android.utils.STRContents;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.idCard_edit)
    EditText mIdCardEdit;//身份证号码
     @Bind(R.id.phoneNumber_edit)
    EditText mPhoneNumberEdit;//手机号码
    @Bind(R.id.verifiCode_btn)
    Button mVerifiCodeBtn;//验证码Btn
    @Bind(R.id.verifiCode_edit)
    EditText mVerifiCodeEdit;//验证码Edit
    @Bind(R.id.mail_edit)
    EditText mMailEdit;//邮箱
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;//用户密码
    @Bind(R.id.confirm_edit)
    EditText mConfirmEdit;//确认密码
    @Bind(R.id.nextStup_btn)
    Button mNextStupBtn;//下一步
     @Bind(R.id.btnBack)
    Button mBackBtn;//返回
    @Bind(R.id.textHeadTitle)
    TextView mHeaderTitle;//标题

    @Bind(R.id.password_layout)
    LinearLayout mPasswordLayout;//用户密码Layout
    @Bind(R.id.confirm_password_layout)
    LinearLayout mConfirmLayout;//确认密码Layout
    @Bind(R.id.mail_layout)
    LinearLayout mMailLayout;//邮箱Layout
    private ProgressDialog progress;

    private RegisterActivity mContext;
    private boolean mIsVerifiCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this ;
        initUI();
    }


    public void initUI() {
        mBackBtn.setVisibility(View.VISIBLE);
        mHeaderTitle.setText(STRContents.REGISTER_TITLE);
    }

    public void initData() {

    }

    //获取验证按按钮
    @OnClick(R.id.verifiCode_btn)
    public void clickConfireCode(View view){
        netVerifiIdCard();
    }

    //返回
    @OnClick(R.id.btnBack)
    public void clickFinish(View view){
        finish();
    }

    //展示密码和确认密码layout
    private void showPassWLayout(){
        mPasswordLayout.setVisibility(View.VISIBLE);
        mConfirmLayout.setVisibility(View.VISIBLE);
        mMailLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 验证是否在金3注册过
     */
    private void netVerifiIdCard(){
        progress = CommUtil.showProgress(mContext, "正在加载数据，请稍候...");
        String idCard = mIdCardEdit.getText().toString();
        String phoneNumber = mPhoneNumberEdit.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("secret", Caller.getIdentityIDParams(idCard, phoneNumber));
        HttpClient.get(Caller.BASE_IP,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                //验证通过:此用户没有注册
                if(!CommUtil.isNullOrBlank(success) && success.equals("true")){
                    Message verifiMessage = new Message();
                    verifiMessage.what = 101;
                    verifiCodeHandler.sendMessage(verifiMessage);
                }
                else{
                    Message verifiMessage = new Message();
                    verifiMessage.what = 101;
                    verifiCodeHandler.sendMessage(verifiMessage);
                    CommUtil.showToast(message,mContext);
                }
                if (progress != null)
                    progress.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                e.printStackTrace();
                if (progress != null)
                    progress.dismiss();
                CommUtil.showToast("验证是否注册失败",mContext);
            }
        });
    }



    /**
     * 验证码输入框失去焦点且不为空
     * 验证 验证码是否正确
     */
     private void isFocusVerifiMsgEdit(){
         mVerifiCodeEdit.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
             @Override
             public void onFocusChange(View v, boolean hasFocus) {
                 if(!hasFocus)
                 {
                     //  此处为失去焦点时的处理内容
                     if(!CommUtil.isNullOrBlank(mVerifiCodeEdit.getText().toString().trim())){
                         messageVerifi();
                     }
                 }
                 else
                 {
                    // 此处为得到焦点时的处理内容

                 }
             }
         });

     }


    /**
     * 发送短信
     *
     * @return
     */
    private void netSendMessage() {
        HashMap<String, String> params = new HashMap<>();
        String regisPhone = mPhoneNumberEdit.getText().toString().trim();
        params.put("user_phone", regisPhone);
        HttpClient.get(Caller.SEND_MESSAGE_URL,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                CommUtil.showToast(message,mContext);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                if (progress != null)
                    progress.dismiss();
                CommUtil.showToast("这儿有一个异常！",mContext);
            }
        });
    }

    /**
     * 短信验证
     *
     * @return
     */
    private void messageVerifi() {
        mIsVerifiCode = false ;
        HashMap<String, String> params = new HashMap<>();
        String regisPhone = mPhoneNumberEdit.getText().toString().trim();
        String regisCode = mVerifiCodeEdit.getText().toString().trim();
        params.put("phoneNumber", regisPhone);
        params.put("vcode", regisCode);
        LogUtil.i("=====regisCode=====", regisCode);

        HttpClient.get(Caller.MESSAGE_VERI_URL,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                //短信验证通过
                if(!CommUtil.isNullOrBlank(success) && success.equals("true")){
                    mIsVerifiCode = true ;
                    CommUtil.showToast(message,mContext);

                }else{
                    mIsVerifiCode = false;
                    CommUtil.showToast(message,mContext);
                    return;
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                if (progress != null)
                    progress.dismiss();
                //java.lang.NumberFormatException: Invalid int: "{""
                e.printStackTrace();
                e.printStackTrace();
                e.printStackTrace();
                CommUtil.showToast("验证码不正确----Exception", mContext);
            }
        });
    }

    /**
     * 提交注册数据
     * @return
     */
    private void netSubData() {
        mIsVerifiCode = false ;
        HashMap<String, String> params = new HashMap<>();
        String regisPhone = mPhoneNumberEdit.getText().toString().trim();
        String regisPass = mPasswordEdit.getText().toString().trim();
        String regisConfirmPass = mConfirmEdit.getText().toString().trim();
        String regisIdCard = mIdCardEdit.getText().toString().trim();
        String regisMail = mMailEdit.getText().toString().trim();

        if (CommUtil.isNullOrBlank(regisPass) || CommUtil.isNullOrBlank(regisConfirmPass) || !regisPass.equals(regisConfirmPass))
        {
            CommUtil.showAlert("两次输入密码不一致",mContext);
            return;
        }

        params.put("secret", Caller.getRegisterParams(regisPhone,regisPass,regisIdCard,regisMail));

        HttpClient.get(Caller.MESSAGE_VERI_URL,params, new HttpResponseHandler() {
            @Override
            public void onSuccess(RestApiResponse response) {
                String success = response.getSuccess();
                String message = response.getMsg();
                String value = response.getValue();
                //注册成功
                if(!CommUtil.isNullOrBlank(success) && success.equals("true")){
                    CommUtil.showToast(message,mContext);

                }
                //注册失败
                else{
                    CommUtil.showToast(message,mContext);
                    return;
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                if (progress != null)
                    progress.dismiss();
                CommUtil.showToast("验证码不正确----Exception", mContext);
            }
        });
    }

    /**
     * 提交注册数据
     */
    @OnClick(R.id.nextStup_btn)
    public void clickSubData()
    {
        netSubData();
    }


    //1.验证手机号和身份证是否注册
    //2.如果没有注册发送验证码
    Handler verifiCodeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //1.没有注册发送验证码
                case 101:
                    showPassWLayout();
                    //TODO 在此调用发送短信验证码接口
                    netSendMessage();
                    isFocusVerifiMsgEdit();
                    break;
//                case 102:
//                    break;
                default:break;

            }

        }
    };

}
