package com.saiyi.gymequipment.run.event;

public class StepEvent {

    private int  stepNubmer;

    public StepEvent(int stepNubmer) {
        this.stepNubmer = stepNubmer;
    }

    public int getStepNubmer() {
        return stepNubmer;
    }

    public void setStepNubmer(int stepNubmer) {
        this.stepNubmer = stepNubmer;
    }
}
