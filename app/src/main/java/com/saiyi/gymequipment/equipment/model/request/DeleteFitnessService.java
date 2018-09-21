package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeleteFitnessService {

    @POST("OutdoorFitness/app/user/fitness/authorize/deleteFitness")
    Observable<BaseResponse<String>> deleteFitnessCenters(@Header("token") String token, @Body RequestBody body);
}
