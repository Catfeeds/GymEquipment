package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.gymequipment.equipment.model.request.GetExerciseVolumeService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2018/4/24.
 */

public class HealthDataModel extends ModelImpl {

    public void getExerciseVolume(BaseResponseListener<ExerciseVolume> listener) {
        GetExerciseVolumeService volumeService = createRetorfitService(GetExerciseVolumeService.class);
        volumeService.getFitnessCenters(UserHelper.instance().getToken())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<ExerciseVolume>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<ExerciseVolume> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
