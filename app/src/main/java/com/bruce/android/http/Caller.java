package com.bruce.android.http;

import com.bruce.android.utils.LogUtil;
import com.scret.utils.SafaUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Administrator on 2017/6/11.
 * 接口
 */

public class Caller  {

    /**
     Picasso.with(mContext).load("http://pic.nipic.com/2008-07-11/20087119630716_2.jpg").resize(DeviceUtil.dp2px(mContext,73), DeviceUtil.dp2px(mContext,73)).placeholder(R.drawable.default_image).into(mIssueImgOne);
     */
    // IP & PORT
    //9064  正式环境（苏州服务器）
    //9099  陈颖鸽
    //9088  焦健俊   1193
//	public static String BASE_IP = "http://222.190.120.106:9064/zzskp/zpdk.yd";
    public static String BASE_IP = "http://222.190.120.106:9064/zzskp/zpdk.yd";
    public static String SEND_MESSAGE_URL = "http://222.190.120.106:9064/dxfs/sendMessage.api";//suzhou  http://219.83.163.147:8099/hospital/
    public static String MESSAGE_VERI_URL = "http://222.190.120.106:9064/dxfs/messageVerification.api";//suzhou  http://219.83.163.147:8099/hospital/


    /**
     * 注册
     * @param regisPhone
     * @param regisPaw
     * @param idCard
     * @return
     */
    public static String getRegisterParams(String regisPhone,String regisPaw,String idCard,String userRemark){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "register.api");
        map.put("user_phone", regisPhone);
        map.put("user_pass", regisPaw);
        map.put("user_card", idCard);
        map.put("user_state", "1");
        map.put("user_remark ", userRemark);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====注册数据====", json.toString());
        return secret;
    }

    /**
     * 身份验证
     * @param regisPhone
     * @param idCard
     * @return
     */
    public static String getIdentityIDParams(String regisPhone,String idCard){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "identity.api");
        map.put("user_phone", regisPhone);
        map.put("user_card", idCard);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====身份验证数据====", json.toString());
        return secret;
    }

    /**
     * 短信验证
     * @param regisPhone
     * @return
     */
    public static String getMsgVerifiParams(String regisPhone){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "messageVerification.api");
        map.put("vcode", regisPhone);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====短信验证====", json.toString());
        return secret;
    }

    /**
     * 短信验证
     * @param regisCode
     * @return
     */
    public static String getSendMsgParams(String regisCode){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "sendMessage.api");
        map.put("user_phone", regisCode);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====申请发票数据====", json.toString());
        return secret;
    }

    /**
     * 登录参数
     * @param userName 用户名
     * @param passWord 密码
     * @param userRemark 0公司 1 个人
     * @return
     */
    public static String getLoginParams(String userName,String passWord,String userRemark){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "login.api");
        map.put("user_name", userName);
        map.put("user_password", passWord);
        map.put("user_remark", userRemark);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====登录数据====", json.toString());
        return secret;
    }



    /**
     * FIXME : 个人票-获取个人信息
     * @param IdCard
     * @return
     */
    public static String getApplyPeopleParams(String IdCard){
        Map<String,String> map = new HashMap<String,String>();
        // FIXME : 个人票-获取个人信息 参数 修改
        map.put("type", "perSellInfo.api");
        map.put("card_number", IdCard);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====个人票-获取个人信息====", json.toString());
        return secret;
    }

    /**
     * FIXME : 个人票-获取开票项目及项目代码
     * @param type 项目类别
     * @return
     */
    public static String getProductCodeParams(String type){
        Map<String,String> map = new HashMap<String,String>();
        // FIXME : 个人票-获取开票项目及项目代码 参数 修改
        map.put("type", "login.api");
        map.put("type", type);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====登录数据====", json.toString());
        return secret;
    }

    /**
     * 获取公司列表
     * @param idCard 身份证号码
     * @return
     */
    public static String getCompListParams(String idCard){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "showSeller.api");
        map.put("fdcard_number", idCard);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====获取公司列表数据====", json.toString());
        return secret;
    }

    /**
     * 获取历史购方列表
     * @param userName 登录用户名
     * @return
     */
    public static String getHisBuyersListParams(String userName){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "check_buyer.api");
        map.put("user_name", userName);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====获取历史购方列表数据====", json.toString());
        return secret;
    }


    /**
     * 查询购方信息
     * @param taxNum 税号
     * @return
     */
    public static String getBuyerInfosParams(String taxNum){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "buyer.api");
        map.put("buyer_taxnum", taxNum);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====查询购方信息====", json.toString());
        return secret;
    }

    /**
     * 纳税人资格类型：判断是否小规模
     * @param idCard 身份证号码
     * djxh 登记序号
     * @return
     */
    public static String getApplyTaxParams(String idCard,String djxh){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "nsrzglx.api");
        map.put("card_number", idCard);
        map.put("DJXH", djxh);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====纳税人资格类型：判断是否小规模====", json.toString());
        return secret;
    }


    /**
     * 纳税人资格类型：判断是否可以申请发票
     * @param xhf_djxh  登记序号
     * @param pro_type  货物或加工修理修配劳务
     * @param invo_type 专票
     * @return
     */
    public static String getApplyParams(String xhf_djxh,String pro_type,String invo_type){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "checkIsCanApply.api");
        map.put("xhf_djxh", xhf_djxh);
        map.put("pro_type", pro_type);
        map.put("invo_type", invo_type);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====纳税人资格类型：判断是否可以申请发票====", json.toString());
        return secret;
    }


    /**获取公司信息
     * @param idCard
     * @param cardNum
     * @return
     */
    public static String getCompInfoParams(String DJXH,String cardNum){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "getSeller.api");
        map.put("DJXH", DJXH);
        map.put("card_number", cardNum);
        JSONObject json = new JSONObject(map);
        LogUtil.i("====获取公司信息数据====", json.toString());
        String secret = SafaUtils.encrypt(json.toString());
        return secret;
    }

    /**
     * 提价申请发票数据
     * @param map
     * @return
     */
    public static String getAddProParams(Map<String,String> map){
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====申请发票数据====", json.toString());
        return secret;
    }


    /**
     * 检测版本更新
     * @param version  版本号
     * @return
     */
    public static String getUpdateParams(String version){
        Map<String,String> map = new HashMap<String,String>();
        map.put("type", "version.api");
        map.put("os_type", "1");//移动系统类型：android：1  ios:0
        map.put("version", version);
        JSONObject json = new JSONObject(map);
        String secret = SafaUtils.encrypt(json.toString());
        LogUtil.i("====版本更新====", json.toString());
        return secret;
    }
}
