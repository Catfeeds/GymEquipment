package com.saiyi.gymequipment.run.bean;

import com.saiyi.gymequipment.run.event.BroadcastEvent;

import java.util.List;

public class AddFootpathBean {
    private List<BroadcastEvent> broadcastPackets;
    private String tname;

    public AddFootpathBean(List<BroadcastEvent>broadcastBeans,String tname){
        this.broadcastPackets = broadcastBeans;
        this.tname = tname;
    }

    public List<BroadcastEvent> getBroadcastPackets() {
        return broadcastPackets;
    }

    public void setBroadcastPackets(List<BroadcastEvent> broadcastPackets) {
        this.broadcastPackets = broadcastPackets;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
}
