package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetEquipmentTypesService {

    @POST("OutdoorFitness/app/user/fitness/authorize/getEquipmentTypes")
    Observable<BaseResponse<List<EquipmentPortType>>> getFitnessCenters(@Header("token") String token);
}
