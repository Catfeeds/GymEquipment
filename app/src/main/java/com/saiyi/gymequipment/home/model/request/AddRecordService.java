package com.saiyi.gymequipment.home.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddRecordService {

    @POST("OutdoorFitness/app/user/fitness/addUserFitnessRecord")
    Observable<BaseResponse<String>> addRecord(@Header("token") String token, @Body RequestBody body);
}
