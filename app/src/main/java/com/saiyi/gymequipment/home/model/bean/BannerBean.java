package com.saiyi.gymequipment.home.model.bean;

import java.util.List;

/**
 * Created by JingNing on 2018-07-05 16:07
 */
public class BannerBean {
    private List<BannerImgBean> carouselImgs;
    private Number idPictureCarousel;
    private Number pctime;

    public List<BannerImgBean> getCarouselImgs() {
        return carouselImgs;
    }

    public void setCarouselImgs(List<BannerImgBean> carouselImgs) {
        this.carouselImgs = carouselImgs;
    }

    public Number getIdPictureCarousel() {
        return idPictureCarousel;
    }

    public void setIdPictureCarousel(Number idPictureCarousel) {
        this.idPictureCarousel = idPictureCarousel;
    }

    public Number getPctime() {
        return pctime;
    }

    public void setPctime(Number pctime) {
        this.pctime = pctime;
    }
}
