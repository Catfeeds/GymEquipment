package com.saiyi.gymequipment.me.model.bean;

import java.io.Serializable;

public class ExerciseVolume implements Serializable{
    private Number fitnessTims;
    private Number htbmi;
    private Number htfitness;
    private Number htrun;
    private Number runDistance;

    public Number getFitnessTims() {
        if(fitnessTims == null) return 0;
        return fitnessTims;
    }

    public void setFitnessTims(Number fitnessTims) {
        this.fitnessTims = fitnessTims;
    }

    public Number getHtbmi() {
        if(htbmi == null) return 0;
        return htbmi;
    }

    public void setHtbmi(Number htbmi) {
        this.htbmi = htbmi;
    }

    public Number getHtfitness() {
        if(htfitness == null) return 0;
        return htfitness;
    }

    public void setHtfitness(Number htfitness) {
        this.htfitness = htfitness;
    }

    public Number getHtrun() {
        if(htrun == null) return 0;
        return htrun;
    }

    public void setHtrun(Number htrun) {
        this.htrun = htrun;
    }

    public Number getRunDistance() {
        if(runDistance == null) return 0;
        return runDistance;
    }

    public void setRunDistance(Number runDistance) {
        this.runDistance = runDistance;
    }

    @Override
    public String toString() {
        return "ExerciseVolume{" +
                "fitnessTims=" + fitnessTims +
                ", htbmi=" + htbmi +
                ", htfitness=" + htfitness +
                ", htrun=" + htrun +
                ", runDistance=" + runDistance +
                '}';
    }
}
