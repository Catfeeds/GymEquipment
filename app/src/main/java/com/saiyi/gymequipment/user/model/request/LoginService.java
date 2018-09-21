package com.saiyi.gymequipment.user.model.request;

import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.gymequipment.user.tool.http.LoginResponse;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    @POST("OutdoorFitness/app/user/doUserLogin")
    Observable<LoginResponse<String>> login(@Body RequestBody body);
}
