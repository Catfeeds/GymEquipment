package com.saiyi.gymequipment.home.model.request;

import com.saiyi.gymequipment.home.model.bean.BannerBean;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface GetPictureCarouselCustom {

    @POST("/OutdoorFitness/app/user/getPictureCarouselCustom")
    Observable<BaseResponse<BannerBean>> getBanner();
}
