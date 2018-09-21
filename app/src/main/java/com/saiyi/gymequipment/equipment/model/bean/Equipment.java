package com.saiyi.gymequipment.equipment.model.bean;

import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 健身器材实体
 */
public class Equipment implements Serializable {

    private String emac;
    private List<EquipmentPort> equipmentPorts;


    public Equipment(String emac) {
        this.emac = emac;
        equipmentPorts = new ArrayList<EquipmentPort>();
    }

    public String getEmac() {
        return emac;
    }

    public void setEmac(String emac) {
        this.emac = emac;
    }

    public List<EquipmentPort> getEquipmentPorts() {
        return equipmentPorts;
    }

    public void setEquipmentPorts(List<EquipmentPort> equipmentPorts) {
        this.equipmentPorts = equipmentPorts;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "emac='" + emac + '\'' +
                ", equipmentPorts=" + equipmentPorts +
                '}';
    }
}
