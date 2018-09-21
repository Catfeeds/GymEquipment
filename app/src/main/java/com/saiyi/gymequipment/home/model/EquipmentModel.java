package com.saiyi.gymequipment.home.model;

import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.model.bean.BannerBean;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.gymequipment.home.model.request.GetPictureCarouselCustom;
import com.saiyi.gymequipment.home.model.request.GetTimesTotalService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EquipmentModel extends ModelImpl {

    public void getTotalData(String time, BaseResponseListener<FitnessData> listener) {
        GetTimesTotalService timesTotalServic = createRetorfitService(GetTimesTotalService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("dayType", RequestConstans.GET_TIMESTOTALRECORDS_TYPE_ALL);
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

    public void getBanner(BaseResponseListener<BannerBean> listener) {
        GetPictureCarouselCustom service = createRetorfitService(GetPictureCarouselCustom.class);
        service.getBanner()
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<BannerBean>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<BannerBean> response) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {//等于空表示没有数据
                                dispatchListenerResponse(response.getData());
                            } else {
                                dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                            }
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
