package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.ExerciseTimeSettingModel;
import com.saiyi.gymequipment.me.ui.ExerciseTimeSettingActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class ExerciseTimeSettingPresenter extends PresenterImpl<ExerciseTimeSettingActivity, ExerciseTimeSettingModel> {
    public ExerciseTimeSettingPresenter(Context context) {
        super(context);
    }

    @Override
    public ExerciseTimeSettingModel initModel() {
        return new ExerciseTimeSettingModel();
    }

    public void updateExerciseVolume(int htfitness, int run) {
        getView().showSetting();
        getModel().updateExerciseVolume(htfitness, run, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().updateExerciseVolumeSuccess();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().updateExerciseVolumeFailed(e.msg);
            }
        });
    }
}
