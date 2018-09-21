package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeleteEquipmentService {

    @POST("OutdoorFitness/app/user/fitness/authorize/deleteFitnessCenterEquipment")
    Observable<BaseResponse<String>> deleteEquipment(@Header("token") String token, @Body RequestBody body);
}
