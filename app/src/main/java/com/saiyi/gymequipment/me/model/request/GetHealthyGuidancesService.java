package com.saiyi.gymequipment.me.model.request;

import com.saiyi.gymequipment.me.model.bean.FitnessGuidance;
import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetHealthyGuidancesService {

    @POST("OutdoorFitness/app/user/getHealthyGuidances")
    Observable<BaseResponse<HealthyGuidances>> getHealthyGuidances(@Header("token") String token, @Body RequestBody body);
}
