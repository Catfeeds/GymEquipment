package com.saiyi.gymequipment.me.model.request;

import com.saiyi.libfast.http.BaseResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddFeedbackImgService {

    @Multipart
    @POST("OutdoorFitness/app/user/addImg")
    Observable<BaseResponse<String>> addImg(@Header("token") String token, @Part MultipartBody.Part file);
}
