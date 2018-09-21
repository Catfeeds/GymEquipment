package com.saiyi.gymequipment.run.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 步道列表中 广播信息包
 */
public class BroadcastPacketsBean implements Serializable,Comparable<BroadcastPacketsBean>{

    private Number distance;//步道包相距当前经度度距离【单位km】
    private Number idTrailBroadcastPacket;//步道包ID
    private String tbpaddress;//步道包文字地址
    private Number tbpdistance;//步道包距离
    private Number tbplatitude;//步道包纬度
    private Number tbplongitude;//步道包经度
    private String tbpmac;//步道包Mac
    private Number tid;//步道ID

    public Number getDistance() {
        return distance;
    }

    public void setDistance(Number distance) {
        this.distance = distance;
    }

    public Number getIdTrailBroadcastPacket() {
        return idTrailBroadcastPacket;
    }

    public void setIdTrailBroadcastPacket(Number idTrailBroadcastPacket) {
        this.idTrailBroadcastPacket = idTrailBroadcastPacket;
    }

    public String getTbpaddress() {
        return tbpaddress;
    }

    public void setTbpaddress(String tbpaddress) {
        this.tbpaddress = tbpaddress;
    }

    public Number getTbpdistance() {
        return tbpdistance;
    }

    public void setTbpdistance(Number tbpdistance) {
        this.tbpdistance = tbpdistance;
    }

    public Number getTbplatitude() {
        return tbplatitude;
    }

    public void setTbplatitude(Number tbplatitude) {
        this.tbplatitude = tbplatitude;
    }

    public Number getTbplongitude() {
        return tbplongitude;
    }

    public void setTbplongitude(Number tbplongitude) {
        this.tbplongitude = tbplongitude;
    }

    public String getTbpmac() {
        return tbpmac;
    }

    public void setTbpmac(String tbpmac) {
        this.tbpmac = tbpmac;
    }

    public Number getTid() {
        return tid;
    }

    public void setTid(Number tid) {
        this.tid = tid;
    }

    @Override
    public int compareTo(@NonNull BroadcastPacketsBean o) {
        int i = this.getIdTrailBroadcastPacket().intValue() - o.getIdTrailBroadcastPacket().intValue();//先按照年龄排序
        return i;
    }
}
