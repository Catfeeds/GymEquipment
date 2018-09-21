package com.saiyi.gymequipment.home.model.bean;

public class RunInfoBean {

    private Number reconsume;//跑步消耗的运动量  reconsume
    private Number redistance;//跑步距离
    private String reduration;//跑步所花时间 【单位秒】
    private Number respeed;//	跑步均速
    private Number restepNumber;//跑步步数
    private Number times;//	次数

    public Number getReconsume() {
        return reconsume;
    }

    public void setReconsume(Number reconsume) {
        this.reconsume = reconsume;
    }

    public Number getRedistance() {
        return redistance;
    }

    public void setRedistance(Number redistance) {
        this.redistance = redistance;
    }

    public String getReduration() {
        return reduration;
    }

    public void setReduration(String reduration) {
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

    public Number getTimes() {
        return times;
    }

    public void setTimes(Number times) {
        this.times = times;
    }
}
