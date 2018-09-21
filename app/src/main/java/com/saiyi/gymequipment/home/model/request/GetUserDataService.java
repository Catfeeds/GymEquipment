package com.saiyi.gymequipment.home.model.request;

import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetUserDataService {

    @POST("OutdoorFitness/app/user/getAppUserData")
    Observable<BaseResponse<User>> getUserData(@Header("token") String token);
}
