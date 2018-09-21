package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetFitnessCentersService {

    @POST("OutdoorFitness/app/user/fitness/getFitnessCenters")
    Observable<BaseResponse<List<GetFitnessCenter>>> getFitnessCenters(@Header("token") String token, @Body RequestBody body);
}
