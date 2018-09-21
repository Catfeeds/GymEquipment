package com.saiyi.gymequipment.equipment.model.bean;

import android.text.TextUtils;

import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.logger.Logger;

import java.io.Serializable;

public class RankingUser implements Serializable {
    private Number distance;
    private Number frduration;
    private int rank;
    private int uid;
    private String uimg;
    private String unickname;

    public Number getDistance() {
        return distance;
    }

    public void setDistance(Number distance) {
        this.distance = distance;
    }

    public Number getFrduration() {
        if (frduration == null) return 0;
        return frduration;
    }

    public void setFrduration(Number frduration) {
        this.frduration = frduration;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUimg() {
        if (!TextUtils.isEmpty(uimg) && !uimg.startsWith("http") && !uimg.contains("wx.qlogo.cn")) {
            uimg = ((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + uimg;
        }
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    @Override
    public String toString() {
        return "RankingUser{" +
                "distance=" + distance +
                ", frduration=" + frduration +
                ", rank=" + rank +
                ", uid=" + uid +
                ", uimg='" + uimg + '\'' +
                ", unickname='" + unickname + '\'' +
                '}';
    }
}
