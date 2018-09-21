package com.saiyi.gymequipment.run.model.request;

import com.saiyi.gymequipment.run.bean.RunHistoryBean;
import com.saiyi.libfast.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RunHistoryService {

    @POST("/OutdoorFitness/app/user/running/getRunningManRecords")
    Observable<BaseResponse<List<RunHistoryBean>>> runHistoryData(@Header("token") String token, @Body RequestBody body);
}
