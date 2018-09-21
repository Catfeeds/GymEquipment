package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidance;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidanceContent;
import com.saiyi.gymequipment.me.model.request.GetHealthyGuidanceService;
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

/**
 * Created by JingNing on 2018-07-17 11:37
 */
public class FitnessGuidanceModel extends ModelImpl {

    public void getHealthyGuidance(int eqtid, BaseResponseListener<FitnessGuidance> listener) {
        GetHealthyGuidanceService guidancesService = createRetorfitService(GetHealthyGuidanceService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("eptid", eqtid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        guidancesService.getHealthyGuidance(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<FitnessGuidance>(getCompositeDisposable(), listener) {

                    @Override
                    public void onResponse(BaseResponse<FitnessGuidance> response) {
                        if(response.isSuccess()){
                            dispatchListenerResponse(response.getData());
                        }else{
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }


    public void getHealthyGuidanceContent(int idHealthyGuidance, BaseResponseListener<FitnessGuidanceContent> listener) {
        GetHealthyGuidanceService guidancesService = createRetorfitService(GetHealthyGuidanceService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("idHealthyGuidance", idHealthyGuidance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        guidancesService.getHealthyGuidanceContent(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<FitnessGuidanceContent>(getCompositeDisposable(), listener) {

                    @Override
                    public void onResponse(BaseResponse<FitnessGuidanceContent> response) {
                        if(response.isSuccess()){
                            dispatchListenerResponse(response.getData());
                        }else{
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }

}
