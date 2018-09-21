package com.saiyi.gymequipment.equipment.model.bean;

import java.io.Serializable;

public class EquipmentPortType implements Serializable {
    private int idEquipmentPortType;
    private String etname;

    public String getEtname() {
        return etname;
    }

    public void setEtname(String etname) {
        this.etname = etname;
    }

    public EquipmentPortType(int idEquipmentPortType) {
        this.idEquipmentPortType = idEquipmentPortType;
    }

    public int getIdEquipmentPortType() {
        return idEquipmentPortType;
    }

    public void setIdEquipmentPortType(int idEquipmentPortType) {
        this.idEquipmentPortType = idEquipmentPortType;
    }
}
