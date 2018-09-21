package com.saiyi.gymequipment.home.model.request;

import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetTimesTotalService {

    @POST("OutdoorFitness/app/user/fitness/getTimesTotalRecords")
    Observable<BaseResponse<FitnessData>> getTimesTotal(@Header("token") String token, @Body RequestBody body);
}
