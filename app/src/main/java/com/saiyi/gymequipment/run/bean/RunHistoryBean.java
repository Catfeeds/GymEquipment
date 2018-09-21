package com.saiyi.gymequipment.run.bean;

public class RunHistoryBean {
    private Number idRunningRecord;//跑步记录ID
    private Number reconsume;//跑步所耗能量
    private Number recreatetime;//跑步记录上传时间
    private Number  redistance;//跑步距离
    private Number reduration;//跑步时长
    private Number respeed;//跑步速度
    private Number restepNumber;//跑步步数
    private String uid;//用户ID为null

    public Number getIdRunningRecord() {
        return idRunningRecord;
    }

    public void setIdRunningRecord(Number idRunningRecord) {
        this.idRunningRecord = idRunningRecord;
    }

    public Number getReconsume() {
        return reconsume;
    }

    public void setReconsume(Number reconsume) {
        this.reconsume = reconsume;
    }

    public Number getRecreatetime() {
        return recreatetime;
    }

    public void setRecreatetime(Number recreatetime) {
        this.recreatetime = recreatetime;
    }

    public Number getRedistance() {
        return redistance;
    }

    public void setRedistance(Number redistance) {
        this.redistance = redistance;
    }

    public Number getReduration() {
        return reduration;
    }

    public void setReduration(Number reduration) {
        this.reduration = reduration;
    }

    public Number getRespeed() {
        return respeed;
    }

    public void setRespeed(Number respeed) {
        this.respeed = respeed;
    }

    public Number getRestepNumber() {
        return restepNumber;
    }

    public void setRestepNumber(Number restepNumber) {
        this.restepNumber = restepNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
