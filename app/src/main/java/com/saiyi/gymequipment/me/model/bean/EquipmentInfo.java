package com.saiyi.gymequipment.me.model.bean;

import com.saiyi.gymequipment.app.GymBuildConfig;

import java.io.Serializable;

/**
 * Created by JingNing on 2018-08-28 16:47
 */
public class EquipmentInfo implements Serializable {
    private String emac;            //设备Mac
    private String epnumber;        //端口号
    private String etname;          //设备名称
    private Number expireddate;     //设备过期日期-在创建日期上加8年
    private String fcaddress;       //健身中心地址
    private String hgimg;           //器材图片
    private String hvideo;         //指导视频
    private Number idFitnessCenter;//健身中心id

    @Override
    public String toString() {
        return "EquipmentInfo{" +
                "emac='" + emac + '\'' +
                ", epnumber='" + epnumber + '\'' +
                ", etname='" + etname + '\'' +
                ", expireddate=" + expireddate +
                ", fcaddress='" + fcaddress + '\'' +
                ", hgimg='" + hgimg + '\'' +
                ", hvideo='" + hvideo + '\'' +
                ", idFitnessCenter=" + idFitnessCenter +
                '}';
    }

    public String getEmac() {
        return emac;
    }

    public void setEmac(String emac) {
        this.emac = emac;
    }

    public String getEpnumber() {
        return epnumber;
    }

    public void setEpnumber(String epnumber) {
        this.epnumber = epnumber;
    }

    public String getEtname() {
        return etname;
    }

    public void setEtname(String etname) {
        this.etname = etname;
    }

    public Number getExpireddate() {
        if(expireddate == null){
            expireddate = 0;
        }
        return expireddate;
    }

    public void setExpireddate(Number expireddate) {
        this.expireddate = expireddate;
    }

    public String getFcaddress() {
        return fcaddress;
    }

    public void setFcaddress(String fcaddress) {
        this.fcaddress = fcaddress;
    }

    public String getHgimg() {
        return GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + hgimg;
    }

    public void setHgimg(String hgimg) {
        this.hgimg = hgimg;
    }

    public String getHvideo() {
        return GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + hvideo;
    }

    public void setHvideo(String hvideo) {
        this.hvideo = hvideo;
    }

    public Number getIdFitnessCenter() {
        return idFitnessCenter;
    }

    public void setIdFitnessCenter(Number idFitnessCenter) {
        this.idFitnessCenter = idFitnessCenter;
    }
}
