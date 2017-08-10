package com.bruce.android.model;

import java.io.Serializable;

/**
 * Created by JiaoJianJun on 2017/8/7.
 * 公司列表
 */

public class CompanyBean implements Serializable {

    private static final long serialVersionUID = -6616241353786638078L;
    private String DJXH;
    private String NSRMC;
    private String NSRSBH;
    private String SJGSDQ;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDJXH() {
        return DJXH;
    }

    public void setDJXH(String DJXH) {
        this.DJXH = DJXH;
    }

    public String getNSRMC() {
        return NSRMC;
    }

    public void setNSRMC(String NSRMC) {
        this.NSRMC = NSRMC;
    }

    public String getNSRSBH() {
        return NSRSBH;
    }

    public void setNSRSBH(String NSRSBH) {
        this.NSRSBH = NSRSBH;
    }

    public String getSJGSDQ() {
        return SJGSDQ;
    }

    public void setSJGSDQ(String SJGSDQ) {
        this.SJGSDQ = SJGSDQ;
    }
}
