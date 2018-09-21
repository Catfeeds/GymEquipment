package com.saiyi.gymequipment.user.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {

    @POST("OutdoorFitness/app/user/doUserRegister")
    Observable<BaseResponse<String>> registered(@Body RequestBody body);
}
