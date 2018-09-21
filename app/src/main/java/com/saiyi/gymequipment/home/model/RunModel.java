package com.saiyi.gymequipment.home.model;

import android.util.Log;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.home.model.request.RunService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RunModel extends ModelImpl {
    private static String TAG = RunModel.class.getSimpleName();

    /**
     * 获取跑步信息总记录
     *
     * @param dayType      1-日,2-周， 3-月， 4-总
     * @param recreatetime 查询的日期 2018-04-23 10:59:48
     * @param listener
     */
    public void getRunInformation(int dayType, String recreatetime, @NonNull ResponseListener<RunInfoBean> listener) {
        RunService datasService = createRetorfitService(RunService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", dayType);
            result.put("recreatetime", recreatetime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.getRunInformation(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<RunInfoBean>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<RunInfoBean> response) {
                                   if (response.isSuccess()) {
                                       Logger.d(TAG, "回复：" + response);
                                       Logger.d(TAG, response.getData().getReconsume() + "  " + response.getData().getRespeed() + "  " + response.getData().getTimes());
                                       dispatchListenerResponse(response.getData());
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }
}
