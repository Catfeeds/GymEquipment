package com.saiyi.gymequipment.me.model.bean;

import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.libfast.logger.Logger;

public class Article {
    private String eptid;       //	设备类型
    private String hgccontent; //文章内容
    private long hgcreatetime; //文章创建时间
    private String hgimg; //文章图片
    private String hgtitle; //	文章标题
    private String hgvideoOrNewsUrl; // 文章第三方链接
    private int idHealthyGuidance; //文章ID

    public String getEptid() {
        return eptid;
    }

    public void setEptid(String eptid) {
        this.eptid = eptid;
    }

    public String getHgccontent() {
        return hgccontent;
    }

    public void setHgccontent(String hgccontent) {
        this.hgccontent = hgccontent;
    }

    public long getHgcreatetime() {
        return hgcreatetime;
    }

    public void setHgcreatetime(long hgcreatetime) {
        this.hgcreatetime = hgcreatetime;
    }

    public String getHgimg() {
        String img = ((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + hgimg;
        return img;
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

    public String getHgvideoOrNewsUrl() {
        return hgvideoOrNewsUrl;
    }

    public void setHgvideoOrNewsUrl(String hgvideoOrNewsUrl) {
        this.hgvideoOrNewsUrl = hgvideoOrNewsUrl;
    }

    public int getIdHealthyGuidance() {
        return idHealthyGuidance;
    }

    public void setIdHealthyGuidance(int idHealthyGuidance) {
        this.idHealthyGuidance = idHealthyGuidance;
    }
}
