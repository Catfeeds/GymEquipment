package com.saiyi.gymequipment.home.model.request;

import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RunService {

    @POST("/OutdoorFitness/app/user/running/getRunningManTimes")
    Observable<BaseResponse<RunInfoBean>> getRunInformation(@Header("token") String token, @Body RequestBody body);
}
