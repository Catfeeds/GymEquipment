package com.saiyi.gymequipment.user.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 获取验证码接口
 */
public interface GetIdentifyService {

    @POST("OutdoorFitness/app/user/doSendUserCode")
    Observable<BaseResponse<String>> getIdentify(@Body RequestBody body);
}
