package com.saiyi.gymequipment.home.model;

import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.home.model.request.AddRecordService;
import com.saiyi.gymequipment.home.model.request.GetEquipmentTypeService;
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

public class MotionResultModel extends ModelImpl {

//    public void getEquipmentType(String mac, String port, BaseResponseListener<String> listener) {
//        GetEquipmentTypeService getEquipmentType = createRetorfitService(GetEquipmentTypeService.class);
//        JSONObject result = new JSONObject();
//        try {
//            result.put("emac", mac);
//            result.put("epnumber", port);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
//        getEquipmentType.getEquipmentType(UserHelper.instance().getToken(), body)
//                .unsubscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
//                    @Override
//                    public void onResponse(BaseResponse<String> response) {
//                        if (response.isSuccess()) {
//                            dispatchListenerResponse(response.getData());
//                        } else {
//                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
//                        }
//                    }
//                });
//    }


    public void addRecord(String mac, String dPort, int recordId, int mTime, double mCalorie, int nums, int frec, BaseResponseListener<String> listener) {
        AddRecordService addRecordService = createRetorfitService(AddRecordService.class);
        JSONObject result = new JSONObject();
        try {
            if (recordId < 0) {
                //硬件没有上传运动数据
                result.put("emac", mac);
                result.put("epnumber", dPort);
                result.put("frconsume", mCalorie / 1000);       //服务器存储单位是千卡，mCalorie单位是卡
                result.put("frduration", mTime);
                result.put("frequency", frec);
                result.put("frtimes", nums);
            } else {
                //硬件已上传运动数据
                result.put("emac", recordId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        addRecordService.addRecord(UserHelper.instance().getToken(), body).unsubscribeOn(Schedulers.io())
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
