package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddEditFitnessCenterService {

    @POST("OutdoorFitness/app/user/fitness/authorize/addNewFitness")
    Observable<BaseResponse<String>> addNewFitnessCenter(@Header("token") String token, @Body RequestBody body);

    @POST("OutdoorFitness/app/user/fitness/authorize/updateFitnessNameOrAddress")
    Observable<BaseResponse<String>> updateFitnessCenter(@Header("token") String token, @Body RequestBody body);
}
