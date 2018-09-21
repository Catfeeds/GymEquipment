package com.saiyi.gymequipment.equipment.model;

import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.tools.GsonUtil;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.equipment.model.request.AddEditFitnessCenterService;
import com.saiyi.gymequipment.equipment.model.request.GetEquipmentTypesService;
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

public class AddFitnessCenterModel extends ModelImpl {

    public void addNewFitnessCenter(BaseResponseListener<String> listener) {
        AddEditFitnessCenterService fitnessCenterService = createRetorfitService(AddEditFitnessCenterService.class);
        String json = GsonUtil.GsonString(FitnessCenterHelper.instance().getFitnessCenter());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        fitnessCenterService.addNewFitnessCenter(UserHelper.instance().getToken(), body)
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

    public void getEquipmentTypes(BaseResponseListener<List<EquipmentPortType>> listener) {
        GetEquipmentTypesService getEquipmentTypes = createRetorfitService(GetEquipmentTypesService.class);
        getEquipmentTypes.getFitnessCenters(UserHelper.instance().getToken())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<EquipmentPortType>>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<List<EquipmentPortType>> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                            FitnessCenterHelper.instance().setEquipmentPortTypeList(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }

    public void updateFitnessCenter(BaseResponseListener<String> listener) {
        AddEditFitnessCenterService fitnessCenterService = createRetorfitService(AddEditFitnessCenterService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("fcaddress", FitnessCenterHelper.instance().getGetFitnessCenter().getFcaddress());
            result.put("fcdefinition",  FitnessCenterHelper.instance().getGetFitnessCenter().getFcdefinition());
            result.put("fclatitude",  FitnessCenterHelper.instance().getGetFitnessCenter().getFclatitude());
            result.put("fclongitude",  FitnessCenterHelper.instance().getGetFitnessCenter().getFclongitude());
            result.put("idFitnessCenter",  FitnessCenterHelper.instance().getGetFitnessCenter().getIdFitnessCenter());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        fitnessCenterService.updateFitnessCenter(UserHelper.instance().getToken(), body)
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
