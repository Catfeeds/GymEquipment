package com.saiyi.gymequipment.run.bean;

public class RankingBean {
    private Number distance;//APP用户跑步总距离
    private String frduration;//所花总时间 【为null】
    private Number rank;//排名
    private String uimg;//APP用头像地址
    private String unickname;//用户名称

    public Number getDistance() {
        return distance;
    }

    public void setDistance(Number distance) {
        this.distance = distance;
    }

    public String getFrduration() {
        return frduration;
    }

    public void setFrduration(String frduration) {
        this.frduration = frduration;
    }

    public Number getRank() {
        return rank;
    }

    public void setRank(Number rank) {
        this.rank = rank;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }
}
