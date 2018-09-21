package com.saiyi.gymequipment.equipment.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 健身中心实体
 */
public class FitnessCenter implements Serializable {

    private String fcaddress;
    private String fcdefinition;
    private double fclatitude;
    private double fclongitude;
    private List<Equipment> equipment;

    public FitnessCenter() {
        equipment = new ArrayList<Equipment>();
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public String getFcaddress() {
        return fcaddress;
    }

    public void setFcaddress(String fcaddress) {
        this.fcaddress = fcaddress;
    }

    public String getFcdefinition() {
        return fcdefinition;
    }

    public void setFcdefinition(String fcdefinition) {
        this.fcdefinition = fcdefinition;
    }

    public double getFclatitude() {
        return fclatitude;
    }

    public void setFclatitude(double fclatitude) {
        this.fclatitude = fclatitude;
    }

    public double getFclongitude() {
        return fclongitude;
    }

    public void setFclongitude(double fclongitude) {
        this.fclongitude = fclongitude;
    }

    @Override
    public String toString() {
        return "FitnessCenter{" +
                "fcaddress='" + fcaddress + '\'' +
                ", fcdefinition='" + fcdefinition + '\'' +
                ", fclatitude=" + fclatitude +
                ", fclongitude=" + fclongitude +
                ", equipment=" + equipment +
                '}';
    }
}
