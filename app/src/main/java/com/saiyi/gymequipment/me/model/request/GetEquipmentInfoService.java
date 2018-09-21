package com.saiyi.gymequipment.me.model.request;

import com.saiyi.gymequipment.me.model.bean.EquipmentInfo;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetEquipmentInfoService {

    @POST("OutdoorFitness/app/user/userfeedback/getEquipmentDetailsByEmac")
    Observable<BaseResponse<List<EquipmentInfo>>> getEquipmentInfo(@Header("token") String token, @Body RequestBody body);
}
