package com.saiyi.gymequipment.me.model.request;


import com.saiyi.gymequipment.me.model.bean.FitnessGuidance;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidanceContent;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetHealthyGuidanceService {

    @POST("OutdoorFitness/app/user/fitness/getHealthyGuidance")
    Observable<BaseResponse<FitnessGuidance>> getHealthyGuidance(@Header("token") String token, @Body RequestBody body);


    @POST("OutdoorFitness/app/user/getAppHealthyGuidanceContent")
    Observable<BaseResponse<FitnessGuidanceContent>> getHealthyGuidanceContent(@Header("token") String token, @Body RequestBody body);
}
