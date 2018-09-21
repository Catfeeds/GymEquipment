package com.saiyi.gymequipment.me.model.bean;

import android.text.TextUtils;

import com.saiyi.gymequipment.app.GymBuildConfig;

/**
 * Created by JingNing on 2018-07-17 17:31
 */
public class FitnessGuidance {
    private Number hgcreatetime;
    private String hgimg;
    private String hgtitle;
    private String hgccontent;
    private String hvideo;

    public String getHgccontent() {
        return hgccontent;
    }

    public void setHgccontent(String hgccontent) {
        this.hgccontent = hgccontent;
    }

    public Number getHgcreatetime() {
        return hgcreatetime;
    }

    public void setHgcreatetime(Number hgcreatetime) {
        this.hgcreatetime = hgcreatetime;
    }

    public String getHgimg() {
        return GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + hgimg;
    }

    public void setHgimg(String hgimg) {
        this.hgimg = hgimg;
    }

    public String getHgtitle() {
        return hgtitle;
    }

    public void setHgtitle(String hgtitle) {
        this.hgtitle = hgtitle;
    }

    public String getHvideo() {
        if(TextUtils.isEmpty(hvideo)) return hvideo;
        return  GymBuildConfig.BASE_HTTP_URL_IMAGE_URL + hvideo;
    }

    public void setHvideo(String hvideo) {
        this.hvideo = hvideo;
    }
}
