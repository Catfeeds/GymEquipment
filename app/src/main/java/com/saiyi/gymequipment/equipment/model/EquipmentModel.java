package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.model.request.DeleteEquipmentService;
import com.saiyi.gymequipment.equipment.model.request.GetEquipmentsService;
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

public class EquipmentModel extends ModelImpl{

    public void getEquipments(BaseResponseListener<List<Equipment>> listener) {
        GetEquipmentsService getEquipments = createRetorfitService(GetEquipmentsService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("fcid", FitnessCenterHelper.instance().getGetFitnessCenter().getIdFitnessCenter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        getEquipments.getEquipments(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<Equipment>>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<List<Equipment>> response) {
                        if (response.isSuccess()){
                            dispatchListenerResponse(response.getData());
                        }else{
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }

    public void deleteEquipment(String eMac, BaseResponseListener<String> listener) {
        DeleteEquipmentService service = createRetorfitService(DeleteEquipmentService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("emac", eMac);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        service.deleteEquipment(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<String> response) {
                        if (response.isSuccess()){
                            dispatchListenerResponse(response.getData());
                        }else{
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
