package com.saiyi.gymequipment.equipment.model.request;

import com.saiyi.gymequipment.equipment.model.bean.RankingUser;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetUserRankings {

    @POST("OutdoorFitness/app/user/fitness/getUserRankings")
    Observable<BaseResponse<List<RankingUser>>> getUserRankings(@Header("token") String token);
}
