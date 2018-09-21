package com.saiyi.gymequipment.me.model.request;

import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetQuestionsService {

    @POST("OutdoorFitness/app/user/getQuestions")
    Observable<BaseResponse<List<Question>>> getQuestions(@Header("token") String token);
}
