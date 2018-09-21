package com.saiyi.gymequipment.me.model.bean;

import java.io.Serializable;

public class Question implements Serializable {
    private int idQuestion;
    private String qdescription;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQdescription() {
        return qdescription;
    }

    public void setQdescription(String qdescription) {
        this.qdescription = qdescription;
    }
}
