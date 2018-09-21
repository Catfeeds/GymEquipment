package com.saiyi.gymequipment.home.model.bean;

import java.io.Serializable;

public class FitnessData{

    private Number consume;

    private Number duration;

    private Number times;

    public Number getConsume() {
        if(consume == null) return 0;
        return consume;
    }

    public void setConsume(Number consume) {
        this.consume = consume;
    }

    public Number getDuration() {
        if(duration == null) return 0;
        return duration;
    }

    public void setDuration(Number duration) {
        this.duration = duration;
    }

    public Number getTimes() {
        if(times == null) return 0;
        return times;
    }

    public void setTimes(Number times) {
        this.times = times;
    }
}
