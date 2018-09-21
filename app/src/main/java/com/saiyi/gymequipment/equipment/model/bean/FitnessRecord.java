package com.saiyi.gymequipment.equipment.model.bean;

public class FitnessRecord {
    private String emac;
    private String epnumber;
    private String etname;
    private Number frconsume;
    private Long frcreatetime;
    private Number frduration;
    private Number frequency;
    private Number frtimes;
    private Number idFitnessRecord;
    private Number uid;

    public Number getFrequency() {
        if(frequency == null) return 0;
        return frequency;
    }

    public void setFrequency(Number frequency) {
        this.frequency = frequency;
    }

    public Number getFrtimes() {
        if(frtimes == null) return 0;
        return frtimes;
    }

    public void setFrtimes(Number frtimes) {
        this.frtimes = frtimes;
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

    public Number getFrconsume() {
        if(frconsume == null) return 0;
        return frconsume;
    }

    public void setFrconsume(Number frconsume) {
        this.frconsume = frconsume;
    }

    public Long getFrcreatetime() {
        return frcreatetime;
    }

    public void setFrcreatetime(Long frcreatetime) {
        this.frcreatetime = frcreatetime;
    }

    public Number getFrduration() {
        if(frduration == null) return 0;
        return frduration;
    }

    public void setFrduration(Number frduration) {
        this.frduration = frduration;
    }

    public Number getIdFitnessRecord() {
        if(idFitnessRecord == null) return 0;
        return idFitnessRecord;
    }

    public void setIdFitnessRecord(Number idFitnessRecord) {
        this.idFitnessRecord = idFitnessRecord;
    }

    public Number getUid() {
        if(uid == null) return 0;
        return uid;
    }

    public void setUid(Number uid) {
        this.uid = uid;
    }
}
