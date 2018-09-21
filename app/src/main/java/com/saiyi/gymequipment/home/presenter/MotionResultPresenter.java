package com.saiyi.gymequipment.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.saiyi.gymequipment.home.model.MotionResultModel;
import com.saiyi.gymequipment.home.ui.MotionResultActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class MotionResultPresenter extends PresenterImpl<MotionResultActivity, MotionResultModel> {


    public MotionResultPresenter(Context context) {
        super(context);
    }

    @Override
    public MotionResultModel initModel() {
        return new MotionResultModel();
    }

    public void getEquipmentType(String mac, String port) {
//        getModel().getEquipmentType(mac, port, new BaseResponseListener<String>() {
//            @Override
//            public void onResponse(String data) {
//                super.onResponse(data);
//                getView().getEquipmentTypeSuccess(data);
//            }
//
//            @Override
//            public void onFailed(ErrorStatus e) {
//                super.onFailed(e);
//                getView().showError(e.msg);
//            }
//        });
    }

    public void addRecord(String mac, String dPort, int recoed, int mTime, int mCalorie, int nums, int frec) {
        getModel().addRecord(mac, dPort, recoed, mTime, mCalorie, nums, frec, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });
    }
}
