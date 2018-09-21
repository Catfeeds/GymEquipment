package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.gymequipment.equipment.model.request.GetFitnessCentersService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FitnessCenterMapModel extends ModelImpl {

    public void getNearFitnessCenter(double latitude, double longitude, String fitnessName, BaseResponseListener<List<GetFitnessCenter>> listener) {
        GetFitnessCentersService fitnessCentersService = createRetorfitService(GetFitnessCentersService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("clatitude", latitude);
            result.put("clongitude", longitude);
            result.put("fitnessName", fitnessName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        fitnessCentersService.getFitnessCenters(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<GetFitnessCenter>>(getCompositeDisposable(), listener) {

                    @Override
                    public void onResponse(BaseResponse<List<GetFitnessCenter>> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
