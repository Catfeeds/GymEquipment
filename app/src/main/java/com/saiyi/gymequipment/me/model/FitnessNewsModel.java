package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.saiyi.gymequipment.me.model.request.GetHealthyGuidancesService;
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

public class FitnessNewsModel extends ModelImpl {
    public void getHealthyGuidances(int pageNum, int pageSize, BaseResponseListener<HealthyGuidances> listener) {
        GetHealthyGuidancesService guidancesService = createRetorfitService(GetHealthyGuidancesService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        guidancesService.getHealthyGuidances(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<HealthyGuidances>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<HealthyGuidances> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
