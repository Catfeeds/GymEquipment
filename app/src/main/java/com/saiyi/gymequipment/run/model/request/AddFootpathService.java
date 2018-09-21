package com.saiyi.gymequipment.run.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddFootpathService {

    @POST("/OutdoorFitness/app/user/running/authorize/addNewTrail")
    Observable<BaseResponse<String>> addFootPathInfo(@Header("token") String token, @Body RequestBody body);
}
