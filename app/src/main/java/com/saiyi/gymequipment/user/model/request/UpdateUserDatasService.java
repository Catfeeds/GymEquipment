package com.saiyi.gymequipment.user.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UpdateUserDatasService {

    @POST("/OutdoorFitness/app/user/doUpdateUserDatas")
    Observable<BaseResponse<String>> updateUser(@Header("token") String token, @Body RequestBody body);
}
