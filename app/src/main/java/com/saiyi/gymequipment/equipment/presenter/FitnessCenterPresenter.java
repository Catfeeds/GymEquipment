package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.FitnessCenterModel;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.gymequipment.equipment.ui.FitnessCenterActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

public class FitnessCenterPresenter extends PresenterImpl<FitnessCenterActivity, FitnessCenterModel> {

    public FitnessCenterPresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessCenterModel initModel() {
        return new FitnessCenterModel();
    }

    public void getFitnessCenterInfo(double clatitude, double clongitude, String tname) {
        getModel().getNearFitnessCenter(clatitude, clongitude, tname, new BaseResponseListener<List<GetFitnessCenter>>() {
            @Override
            public void onResponse(List<GetFitnessCenter> data) {
                super.onResponse(data);
                getView().getFitnessCenterInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getFitnessCenterInfo(null);
            }
        });
    }

    public void deleteFitness(int idFitnessCenter) {
        getView().showDeleteFCing();
        getModel().deleteFitness(idFitnessCenter, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().deleteFitnessCenterSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().deleteFitnessCenterFaild(e.msg);
            }
        });
    }

    @Override
    protected void onRelease() {
        super.onRelease();
    }
}
