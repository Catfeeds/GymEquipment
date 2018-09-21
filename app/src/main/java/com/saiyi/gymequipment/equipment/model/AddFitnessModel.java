package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.tools.GsonUtil;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPort;
import com.saiyi.gymequipment.equipment.model.request.AddEquipmentToOldFitnessService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddFitnessModel extends ModelImpl {

    public void addDevice(int fcID, String eMac, List<EquipmentPort> ports, BaseResponseListener<String> listener) {
        AddEquipmentToOldFitnessService service = createRetorfitService(AddEquipmentToOldFitnessService.class);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"fcid\":"+fcID);
        sb.append(",\"emac\":\""+eMac+"\",\"equipmentPorts\":");
        sb.append(GsonUtil.GsonString(ports)+"}");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), sb.toString());
        service.addEquipment(UserHelper.instance().getToken(), body)
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
                });
    }

    public void updateDevice(int fcID, String eMac, List<EquipmentPort> ports, BaseResponseListener<String> listener) {
        AddEquipmentToOldFitnessService service = createRetorfitService(AddEquipmentToOldFitnessService.class);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"fcid\":"+fcID);
        sb.append(",\"emac\":\""+eMac+"\",\"equipmentPorts\":");
        sb.append(GsonUtil.GsonString(ports)+"}");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), sb.toString());
        service.updateEquipment(UserHelper.instance().getToken(), body)
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
                });
    }
}
