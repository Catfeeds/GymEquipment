package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.FitnessCenterMapModel;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.gymequipment.equipment.ui.FitnessCenterMapActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;



import java.util.List;

public class FitnessCenterMapPresenter extends PresenterImpl<FitnessCenterMapActivity, FitnessCenterMapModel> {

    public FitnessCenterMapPresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessCenterMapModel initModel() {
        return new FitnessCenterMapModel();
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
}
