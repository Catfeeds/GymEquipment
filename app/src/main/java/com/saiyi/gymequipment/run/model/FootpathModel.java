package com.saiyi.gymequipment.run.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.model.request.DeleteFootpathService;
import com.saiyi.gymequipment.run.model.request.FootpathService;
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

/**
 * 描述：
 * 创建作者：ask
 * 创建时间：2018/5/2 14:00
 */

public class FootpathModel extends ModelImpl {

    /**
     * 请求步道列表
     *
     * @param clatitude  当前纬度
     * @param clongitude 当前经度
     * @param tname      步道名称【查询时用】
     * @param listener   结果回调
     */
    public void getFootPathInformation(double clatitude, double clongitude, String tname, @NonNull ResponseListener<List<FootpathBean>> listener) {
        FootpathService datasService = createRetorfitService(FootpathService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("clatitude", clatitude);
            result.put("clongitude", clongitude);
            result.put("tname", tname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.footPathInfo(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<FootpathBean>>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<List<FootpathBean>> response) {
                                   if (response.isSuccess()) {
                                       dispatchListenerResponse(response.getData());
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }


    /**
     * 删除步道
     *
     * @param footpathNumber 步道id
     * @param listener
     */
    public void deleteFootpath(Number footpathNumber, @NonNull ResponseListener<String> listener) {
        DeleteFootpathService datasService = createRetorfitService(DeleteFootpathService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("idTrail", String.valueOf(footpathNumber));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.deleteFootpath(UserHelper.instance().getToken(), body)
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
