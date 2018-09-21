package com.saiyi.gymequipment.run.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.run.bean.RankingBean;
import com.saiyi.gymequipment.run.model.request.RankingService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RankingModel extends ModelImpl {

    /**
     * 获取排行榜数据
     *
     * @param listener 结果回调
     */
    public void addRunningData(@NonNull ResponseListener<List<RankingBean>> listener) {
        RankingService datasService = createRetorfitService(RankingService.class);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "");
        datasService.rankingData(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<RankingBean>>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<List<RankingBean>> response) {
                                   if (response.isSuccess()) {
                                       dispatchListenerResponse(response.getData());
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }
}
