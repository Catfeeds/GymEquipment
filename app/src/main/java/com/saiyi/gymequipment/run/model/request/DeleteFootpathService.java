package com.saiyi.gymequipment.run.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeleteFootpathService {

    @POST("/OutdoorFitness/app/user/running/authorize/deleteTrail")
    Observable<BaseResponse<String>> deleteFootpath(@Header("token") String token, @Body RequestBody body);
}
