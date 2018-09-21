package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddNewFitnessService {

    @POST("OutdoorFitness/app/user/fitness/authorize/addNewFitness")
    Observable<BaseResponse<String>> addNewFitness(@Header("token") String token, @Body RequestBody body);
}
