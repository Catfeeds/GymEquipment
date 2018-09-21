package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.request.UpdateExerciseVolumeService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ExerciseTimeSettingModel extends ModelImpl {


    public void updateExerciseVolume(int htfitness, int run, BaseResponseListener<String> listener) {
        UpdateExerciseVolumeService volumeService = createRetorfitService(UpdateExerciseVolumeService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("htfitness", htfitness);
            result.put("htrun", run);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        volumeService.updateExerciseVolumeService(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<String> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getMessage());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
