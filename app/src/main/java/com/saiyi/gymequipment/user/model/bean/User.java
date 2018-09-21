package com.saiyi.gymequipment.user.model.bean;

import android.text.TextUtils;

import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.utils.StringUtils;

import java.io.Serializable;

public class User implements Serializable {
    private String uphone;
    private String unickname;
    private String ugender;
    private int uage;
    private long ubirthday;
    private int uheight;  //cm
    private int uweight; //kg
    private String uimg;
    private String uopenid;
    private String utoken;
    private int isAuthorize; //1-授权用户， 2-普通管理员用户， null-失败操作
    private String usosPhone;

    public User(String phone, String utoken, int isAuthorize) {
        this.uphone = phone;
        this.utoken = utoken;
        this.isAuthorize = isAuthorize;
    }

    public String getUtoken() {
        return utoken;
    }

    public void setUtoken(String utoken) {
        this.utoken = utoken;
    }

    public int getIsAuthorize() {
        return isAuthorize;
    }

    /**
     * 是否是管理员
     * 1-授权用户， 2-普通管理员用户
     */
    public boolean isAdmin() {
        if (isAuthorize == 1) {
            return true;
        }
        return false;
    }

    public void setIsAuthorize(int isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public String getPhone() {
        return uphone;
    }

    public void setPhone(String phone) {
        this.uphone = phone;
    }

    public String getPic() {
        if (!TextUtils.isEmpty(uimg) && !uimg.startsWith("http") && !uimg.contains("wx.qlogo.cn")) {
            if (StringUtils.isMobileNum(uphone)) {
                this.uimg = ((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + uimg;
            }
        }
        return uimg;
    }

    public void setPic(String pic) {
        this.uimg = pic;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getUgender() {
        return ugender;
    }

    public void setUgender(String ugender) {
        this.ugender = ugender;
    }

    public int getUage() {
        return uage;
    }

    public void setUage(int uage) {
        this.uage = uage;
    }

    public long getUbirthday() {
        return ubirthday;
    }

    public void setUbirthday(long ubirthday) {
        this.ubirthday = ubirthday;
    }

    public int getHeight() {
        return uheight;
    }

    public void setHeight(int height) {
        this.uheight = height;
    }

    public int getWeight() {
        return uweight;
    }

    public void setWeight(int weight) {
        this.uweight = weight;
    }

    public String getUopenid() {
        return uopenid;
    }

    public void setUopenid(String uopenid) {
        this.uopenid = uopenid;
    }

    public String getSosPhone() {
        return usosPhone;
    }

    public void setSosPhone(String sosPhone) {
        this.usosPhone = sosPhone;
    }

    @Override
    public String toString() {
        return "User{" +
                "uphone='" + uphone + '\'' +
                ", unickname='" + unickname + '\'' +
                ", ugender='" + ugender + '\'' +
                ", uage=" + uage +
                ", ubirthday=" + ubirthday +
                ", uheight=" + uheight +
                ", uweight=" + uweight +
                ", uimg='" + uimg + '\'' +
                ", uopenid='" + uopenid + '\'' +
                ", utoken='" + utoken + '\'' +
                ", isAuthorize=" + isAuthorize +
                ", usosPhone='" + usosPhone + '\'' +
                '}';
    }
}
