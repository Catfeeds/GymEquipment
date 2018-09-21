package com.saiyi.gymequipment.me.model.request;

import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddFeedbackService {

    @POST("OutdoorFitness/app/user/addFeedback")
    Observable<BaseResponse<String>> addFeedback(@Header("token") String token, @Body RequestBody body);
}
