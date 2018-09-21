package com.saiyi.gymequipment.me.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadUserImgService {

    @Multipart
    @POST("OutdoorFitness/app/user/doUploadUserImg")
    Observable<BaseResponse<String>> uploadUserImg(@Header("token") String token, @Part MultipartBody.Part file);
}
