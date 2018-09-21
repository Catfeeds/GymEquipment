package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetExerciseVolumeService {

    @POST("OutdoorFitness/app/user/getExerciseVolume")
    Observable<BaseResponse<ExerciseVolume>> getFitnessCenters(@Header("token") String token);
}
