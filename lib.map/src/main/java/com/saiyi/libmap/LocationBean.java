package com.saiyi.libmap;

import java.io.Serializable;

public class LocationBean implements Serializable {

    private double latitude;//获取纬度
    private double longitude;//获取经度
    private int status;//获取GPS的当前状态
    private double altitude;//获取海拔
    private long time;//定位时间
    private String address;//定位的地址信息

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
