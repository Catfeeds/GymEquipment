package com.saiyi.gymequipment.run.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 步道列表实体类
 */
public class FootpathBean implements Serializable{
    private List<BroadcastPacketsBean> broadcastPackets;
    private Number idTrail;//步道ID
    private Number tcreatetime;//步道创建时间
    private String tname;//步道名称

    public List<BroadcastPacketsBean> getBroadcastPackets() {
        return broadcastPackets;
    }

    public void setBroadcastPackets(List<BroadcastPacketsBean> broadcastPackets) {
        this.broadcastPackets = broadcastPackets;
    }

    public Number getIdTrail() {
        return idTrail;
    }

    public void setIdTrail(Number idTrail) {
        this.idTrail = idTrail;
    }

    public Number getTcreatetime() {
        return tcreatetime;
    }

    public void setTcreatetime(Number tcreatetime) {
        this.tcreatetime = tcreatetime;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
}
