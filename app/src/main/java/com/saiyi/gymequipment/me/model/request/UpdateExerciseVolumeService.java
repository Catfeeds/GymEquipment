package com.saiyi.gymequipment.me.model.request;

import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UpdateExerciseVolumeService {

    @POST("OutdoorFitness/app/user/updateExerciseVolume")
    Observable<BaseResponse<String>> updateExerciseVolumeService(@Header("token") String token, @Body RequestBody body);
}
