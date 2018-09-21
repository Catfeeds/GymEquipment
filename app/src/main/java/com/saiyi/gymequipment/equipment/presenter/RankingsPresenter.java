package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.RankingsModel;
import com.saiyi.gymequipment.equipment.model.bean.RankingUser;
import com.saiyi.gymequipment.equipment.ui.RankingsActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

public class RankingsPresenter extends PresenterImpl<RankingsActivity, RankingsModel> {
    public RankingsPresenter(Context context) {
        super(context);
    }

    @Override
    public RankingsModel initModel() {
        return new RankingsModel();
    }

    public void getUserRankings() {
        getView().showGetDataLoading();
        getModel().getUserRankings(new BaseResponseListener<List<RankingUser>>() {
            @Override
            public void onResponse(List<RankingUser> data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().getRankingsSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().getRankingsFaild(e.msg);
            }
        });
    }


}
