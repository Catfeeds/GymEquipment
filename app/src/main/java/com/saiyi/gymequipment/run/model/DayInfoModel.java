package com.saiyi.gymequipment.run.model;

import android.util.Log;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.home.model.request.RunService;
import com.saiyi.gymequipment.run.bean.RunHistoryBean;
import com.saiyi.gymequipment.run.model.request.RunHistoryService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class DayInfoModel extends ModelImpl {

    private String TAG = DayInfoModel.class.getSimpleName();

    /**
     * 获取跑步详细信息
     *
     * @param dayType      1-日,2-周， 3-月
     * @param recreatetime 查询的日期 2018-04-23 10:59:48
     * @param listener
     */
    public void getRunInformation(int dayType, String recreatetime, @NonNull ResponseListener<List<RunHistoryBean>> listener) {
        RunHistoryService datasService = createRetorfitService(RunHistoryService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", dayType);
            result.put("recreatetime", recreatetime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.runHistoryData(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<RunHistoryBean>>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<List<RunHistoryBean>> response) {
                                   if (response.isSuccess()) {
                                       Logger.d(TAG, "回复：" + response);
                                       dispatchListenerResponse(response.getData());
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }

    /**
     * 获取跑步信息总记录
     *
     * @param dayType      1-日,2-周， 3-月， 4-总
     * @param recreatetime 查询的日期 2018-04-23 10:59:48
     * @param listener
     */
    public void getRunTotalInformation(int dayType, String recreatetime, @NonNull ResponseListener<RunInfoBean> listener) {
        RunService datasService = createRetorfitService(RunService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", dayType);
            result.put("recreatetime", recreatetime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("------->", result.toString());
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
