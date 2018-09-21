package com.saiyi.gymequipment.equipment.model.bean;

import java.io.Serializable;

public class GetFitnessCenter implements Serializable {

    private Number distance;
    private String fcaddress;
    private Number fccreatetime;
    private String fcdefinition;
    private Number fclatitude;
    private Number fclongitude;
    private Number idFitnessCenter;

    public Number getDistance() {
        if(distance == null) return 0;
        return distance;
    }

    public void setDistance(Number distance) {
        this.distance = distance;
    }

    public String getFcaddress() {
        return fcaddress;
    }

    public void setFcaddress(String fcaddress) {
        this.fcaddress = fcaddress;
    }

    public Number getFccreatetime() {
        if(fccreatetime == null) return 0;
        return fccreatetime;
    }

    public void setFccreatetime(Number fccreatetime) {
        this.fccreatetime = fccreatetime;
    }

    public String getFcdefinition() {
        return fcdefinition;
    }

    public void setFcdefinition(String fcdefinition) {
        this.fcdefinition = fcdefinition;
    }

    public Number getFclatitude() {
        if(fclatitude == null) return 0;
        return fclatitude;
    }

    public void setFclatitude(Number fclatitude) {
        this.fclatitude = fclatitude;
    }

    public Number getFclongitude() {
        if(fclongitude == null) return 0;
        return fclongitude;
    }

    public void setFclongitude(Number fclongitude) {
        this.fclongitude = fclongitude;
    }

    public Number getIdFitnessCenter() {
        if(idFitnessCenter == null) return 0;
        return idFitnessCenter;
    }

    public void setIdFitnessCenter(Number idFitnessCenter) {
        this.idFitnessCenter = idFitnessCenter;
    }

    @Override
    public String toString() {
        return "GetFitnessCenter{" +
                "distance=" + distance +
                ", fcaddress='" + fcaddress + '\'' +
                ", fccreatetime=" + fccreatetime +
                ", fcdefinition='" + fcdefinition + '\'' +
                ", fclatitude=" + fclatitude +
                ", fclongitude=" + fclongitude +
                ", idFitnessCenter=" + idFitnessCenter +
                '}';
    }
}
