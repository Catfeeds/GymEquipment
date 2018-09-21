package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddEquipmentToOldFitnessService {

    @POST("OutdoorFitness/app/user/fitness/authorize/addEquipmentToOldFitness")
    Observable<BaseResponse<String>> addEquipment(@Header("token") String token, @Body RequestBody body);

    @POST("OutdoorFitness/app/user/fitness/authorize/updateEquipmentToOldFitness")
    Observable<BaseResponse<String>> updateEquipment(@Header("token") String token, @Body RequestBody body);
}
