package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.bean.EquipmentInfo;
import com.saiyi.gymequipment.me.model.request.GetEquipmentInfoService;
import com.saiyi.gymequipment.me.presenter.EquipmentInfoPresenter;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by JingNing on 2018-08-28 09:58
 */
public class EquipmentInfoModel extends ModelImpl{

    public void getEquipmentInfo(String mac, BaseResponseListener<List<EquipmentInfo>> listener) {
        GetEquipmentInfoService service = createRetorfitService(GetEquipmentInfoService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("emac", mac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        service.getEquipmentInfo(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<EquipmentInfo>>(getCompositeDisposable(), listener) {

                    @Override
                    public void onResponse(BaseResponse<List<EquipmentInfo>> response) {
                        if(response.isSuccess()){
                            dispatchListenerResponse(response.getData());
                        }else{
                         dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
