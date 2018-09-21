package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.FitnessNewsModel;
import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.saiyi.gymequipment.me.ui.FitnessGuidanceListActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class FitnessNewsPresenter extends PresenterImpl<FitnessGuidanceListActivity, FitnessNewsModel> {

    public FitnessNewsPresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessNewsModel initModel() {
        return new FitnessNewsModel();
    }

    public void getHealthyGuidances(int pageNum, int pageSize) {
//        getView().showGetDataLoading();
        getModel().getHealthyGuidances(pageNum, pageSize, new BaseResponseListener<HealthyGuidances>() {

            @Override
            public void onResponse(HealthyGuidances data) {
                super.onResponse(data);
                getView().getHealthyGuidancesSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getHealthyGuidancesFailed(e.msg);
            }
        });
    }
}
