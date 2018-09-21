package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.FitnessRecord;
import com.saiyi.gymequipment.equipment.model.request.GetFitnessRecordsService;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.gymequipment.home.model.request.GetTimesTotalService;
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

public class FitnessRecordModel extends ModelImpl {

    //获取详细的运动数据
    public void getFitnessRecords(int type, String time, BaseResponseListener<List<FitnessRecord>> listener) {
        GetFitnessRecordsService recordsService = createRetorfitService(GetFitnessRecordsService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", type);
            result.put("frcreatetime", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        recordsService.getFitnessCenters(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<FitnessRecord>>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<List<FitnessRecord>> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }


    //获取总的运动数据
    public void getTotalData(int type, String time, BaseResponseListener<FitnessData> listener) {
        GetTimesTotalService timesTotalServic = createRetorfitService(GetTimesTotalService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", type);
            result.put("frcreatetime", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        timesTotalServic.getTimesTotal(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<FitnessData>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<FitnessData> response) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {//等于空表示没有数据
                                dispatchListenerResponse(response.getData());
                            }
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
