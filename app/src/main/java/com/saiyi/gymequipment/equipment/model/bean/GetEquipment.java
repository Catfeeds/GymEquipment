package com.saiyi.gymequipment.equipment.model.bean;

import java.util.List;

/**
 * Created by JingNing on 2018-08-17 17:46
 */
public class GetEquipment {
    private Number ecreatetime;
    private String eMac;
    private Number fcid;
    private Number idEquipment;
    private List<Equipment> equipment;

    public Number getEcreatetime() {
        return ecreatetime;
    }

    public void setEcreatetime(Number ecreatetime) {
        this.ecreatetime = ecreatetime;
    }

    public String geteMac() {
        return eMac;
    }

    public void seteMac(String eMac) {
        this.eMac = eMac;
    }

    public Number getFcid() {
        return fcid;
    }

    public void setFcid(Number fcid) {
        this.fcid = fcid;
    }

    public Number getIdEquipment() {
        return idEquipment;
    }

    public void setIdEquipment(Number idEquipment) {
        this.idEquipment = idEquipment;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "GetEquipment{" +
                "ecreatetime=" + ecreatetime +
                ", eMac='" + eMac + '\'' +
                ", fcid=" + fcid +
                ", idEquipment=" + idEquipment +
                ", equipment=" + equipment +
                '}';
    }
}
