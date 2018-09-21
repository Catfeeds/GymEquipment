package com.saiyi.gymequipment.run.model.request;

import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FootpathService {

    @POST("/OutdoorFitness/app/user/running/getTrails")
    Observable<BaseResponse<List<FootpathBean>>> footPathInfo(@Header("token") String token, @Body RequestBody body);
}
