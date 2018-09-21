package com.saiyi.gymequipment.home.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.home.model.request.GetEquipmentTypeService;
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
 * Created by JingNing on 2018-07-24 17:28
 */
public class BodybuildingModel extends ModelImpl {

    public void getEquipmentType(String mac, String port, BaseResponseListener<EquipmentPortType> listener) {
        GetEquipmentTypeService getEquipmentType = createRetorfitService(GetEquipmentTypeService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("emac", mac);
            result.put("epnumber", port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        getEquipmentType.getEquipmentType(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<EquipmentPortType>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<EquipmentPortType> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
