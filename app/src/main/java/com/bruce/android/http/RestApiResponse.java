package com.bruce.android.http;

/**
 * Created by tiansj on 2016/11/30.
 */

public class RestApiResponse {
//{"msg":"登录成功",
// "success":true,
// "value":{"bsrSfzjHm":"1","bsrYddh":"1","bsrxm":"1",
// "card_number":"342921198701192533","cwfzrSfzjHm":"1",
// "cwfzrYddh":"1","cwfzrxm":"1","email":"1","fddbrSfzjHm":"1",
// "fddbrYddh":"1","fddbrxm":"1","phone_number":"1","remark":"1",
// "state":"1   ","user_name":"12345678902","user_password":"123123",
// "xingming":"1"}}
    private String msg;
    private String success;
    private String value;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
