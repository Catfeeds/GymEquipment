package com.saiyi.gymequipment.run.bean;

public class BroadcastBean {
    private String tbpaddress;//步道包文字地址
    private Number tbpdistance;//步道包距离
    private Number tbplatitude;//步道包纬度
    private Number tbplongitude;//步道包经度
    private String tbpmac;//步道包名称

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
}
