package com.saiyi.gymequipment.equipment.model.bean;

import java.io.Serializable;

public class EquipmentPort implements Serializable {

    private String epnumber;
    private EquipmentPortType equipmentPortType;

    public String getEpnumber() {
        return epnumber;
    }

    public void setEpnumber(String epnumber) {
        this.epnumber = epnumber;
    }

    public EquipmentPortType getEquipmentPortType() {
        return equipmentPortType;
    }

    public void setEquipmentPortType(EquipmentPortType equipmentPortType) {
        this.equipmentPortType = equipmentPortType;
    }
}
