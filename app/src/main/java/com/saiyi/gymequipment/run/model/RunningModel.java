package com.saiyi.gymequipment.run.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.model.request.RunningDataService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RunningModel extends ModelImpl {

    /**
     * 添加跑步数据
     *
     * @param reconsume    跑步消耗能量
     * @param redistance   跑步距离
     * @param reduration   跑步消耗时间 【单位秒】
     * @param respeed      速度
     * @param restepNumber 跑步步数
     * @param listener     结果回调
     * @param tid 步道ID
     */
    public void addRunningData(double reconsume, double redistance, double reduration, double respeed, double restepNumber,Number tid, @NonNull ResponseListener<String> listener) {
        RunningDataService datasService = createRetorfitService(RunningDataService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("reconsume", reconsume);
            result.put("redistance", redistance);
            result.put("reduration", reduration);
            result.put("respeed", respeed);
            result.put("restepNumber", restepNumber);
            result.put("tid", tid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.runningData(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
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
                           }
                );
    }
}
