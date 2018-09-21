package com.saiyi.gymequipment.me.model.bean;

import android.text.TextUtils;

import com.saiyi.gymequipment.app.GymBuildConfig;

/**
 * Created by JingNing on 2018-07-17 17:31
 */
public class FitnessGuidanceContent {
    private Number hgId;
    private String hgcContent;
    private String hvideo;
    private String idHealthyGuidanceContent;

    public Number getHgId() {
        return hgId;
    }

    public void setHgId(Number hgId) {
        this.hgId = hgId;
    }

    public String getHgcContent() {
        return hgcContent;
    }

    public void setHgcContent(String hgcContent) {
        this.hgcContent = hgcContent;
    }

    public String getIdHealthyGuidanceContent() {
        return idHealthyGuidanceContent;
    }

    public void setIdHealthyGuidanceContent(String idHealthyGuidanceContent) {
        this.idHealthyGuidanceContent = idHealthyGuidanceContent;
    }

    public String getHvideo() {
        if(TextUtils.isEmpty(hvideo)) return hvideo;
        return  GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + hvideo;
    }

    public void setHvideo(String hvideo) {
        this.hvideo = hvideo;
    }
}
