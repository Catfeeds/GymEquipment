package com.saiyi.gymequipment.run.event;

import java.io.Serializable;

public class BroadcastEvent implements Serializable {

    private String tbpaddress;//广播包定位位置
    private double tbpdistance;//广播包距离
    private double tbplatitude;//广播包纬度
    private double tbplongitude;//步道包经度
    private String tbpmac;//广播包mac




    public BroadcastEvent(String tbpaddress, double tbpdistance, double tbplatitude,double tbplongitude,String tbpmac) {
        this.tbpaddress = tbpaddress;
        this.tbpdistance = tbpdistance;
        this.tbplatitude = tbplatitude;
        this.tbplongitude = tbplongitude;
        this.tbpmac = tbpmac;
    }

    public String getTbpaddress() {
        return tbpaddress;
    }

    public void setTbpaddress(String tbpaddress) {
        this.tbpaddress = tbpaddress;
    }

    public double getTbpdistance() {
        return tbpdistance;
    }

    public void setTbpdistance(double tbpdistance) {
        this.tbpdistance = tbpdistance;
    }

    public double getTbplatitude() {
        return tbplatitude;
    }

    public void setTbplatitude(double tbplatitude) {
        this.tbplatitude = tbplatitude;
    }

    public double getTbplongitude() {
        return tbplongitude;
    }

    public void setTbplongitude(double tbplongitude) {
        this.tbplongitude = tbplongitude;
    }

    public String getTbpmac() {
        return tbpmac;
    }

    public void setTbpmac(String tbpmac) {
        this.tbpmac = tbpmac;
    }
}
