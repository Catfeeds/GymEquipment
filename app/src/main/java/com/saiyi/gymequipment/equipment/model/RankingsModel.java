package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.RankingUser;
import com.saiyi.gymequipment.equipment.model.request.GetUserRankings;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RankingsModel extends ModelImpl {
    public void getUserRankings(BaseResponseListener<List<RankingUser>> listener) {
        GetUserRankings userRankings = createRetorfitService(GetUserRankings.class);
        userRankings.getUserRankings(UserHelper.instance().getToken())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<RankingUser>>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<List<RankingUser>> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
