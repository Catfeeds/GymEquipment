package com.saiyi.gymequipment.me.presenter;

import android.content.Context;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.gymequipment.me.model.HealthDataModel;
import com.saiyi.gymequipment.me.ui.HealthDataActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

/**
 * Created on 2018/4/24.
 */

public class HealthDataPresenter extends PresenterImpl<HealthDataActivity, HealthDataModel> {
    public HealthDataPresenter(Context context) {
        super(context);
    }

    @Override
    public HealthDataModel initModel() {
        return new HealthDataModel();
    }

    public void getExerciseVolume() {
        getView().showGetDataLoading();
        getModel().getExerciseVolume(new BaseResponseListener<ExerciseVolume>() {
            @Override
            public void onResponse(ExerciseVolume data) {
                super.onResponse(data);
                getView().getExerciseVolumeSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getExerciseVolumeFailed(e.msg);
            }
        });
    }
}
