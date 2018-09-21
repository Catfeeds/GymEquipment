package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.EquipmentModel;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.ui.EquipmentActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

public class EquipmentPresenter extends PresenterImpl<EquipmentActivity, EquipmentModel> {

    public EquipmentPresenter(Context context) {
        super(context);
    }

    public void getEquipments() {
        getView().showLoading();
        getModel().getEquipments(new BaseResponseListener<List<Equipment>>() {
            @Override
            public void onResponse(List<Equipment> data) {
                super.onResponse(data);
                getView().getEquipmentsSuccess(data);
                getView().dismissLoading();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().onFailed(e.msg);
                getView().dismissLoading();
            }
        });
    }

    public void deleteEquipment(String eMac) {
        getView().showLoading();
        getModel().deleteEquipment(eMac, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().deleteEquipmentSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().onFailed(e.msg);
            }
        });
    }

    @Override
    public EquipmentModel initModel() {
        return new EquipmentModel();
    }
}
