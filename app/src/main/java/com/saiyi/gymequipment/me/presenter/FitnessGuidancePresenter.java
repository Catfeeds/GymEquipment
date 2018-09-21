package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.FitnessGuidanceModel;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidance;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidanceContent;
import com.saiyi.gymequipment.me.ui.FitnessGuidanceActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

/**
 * Created by JingNing on 2018-07-17 11:43
 */
public class FitnessGuidancePresenter extends PresenterImpl<FitnessGuidanceActivity, FitnessGuidanceModel> {

    public FitnessGuidancePresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessGuidanceModel initModel() {
        return new FitnessGuidanceModel();
    }

    public void getHealthyGuidance(int eqtid){
        getView().showGetData();
        getModel().getHealthyGuidance(eqtid, new BaseResponseListener<FitnessGuidance>(){
            @Override
            public void onResponse(FitnessGuidance data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().getHealthyGuidanceSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().getDataFailed(e.msg);
            }
        });
    }

    public void getHealthyGuidanceContent(int idHealthyGuidanc){
        getView().showGetData();
        getModel().getHealthyGuidanceContent(idHealthyGuidanc, new BaseResponseListener<FitnessGuidanceContent>(){
            @Override
            public void onResponse(FitnessGuidanceContent data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().getHealthyGuidanceContentSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().getDataFailed(e.msg);
            }
        });
    }
}
