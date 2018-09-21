package com.saiyi.gymequipment.home.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.home.model.BodybuildingModel;
import com.saiyi.gymequipment.home.ui.BodybuildingActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;

/**
 * Created by JingNing on 2018-07-24 17:26
 */
public class BodybuildingPresenter extends PresenterImpl<BodybuildingActivity, BodybuildingModel> {

    public BodybuildingPresenter(Context context) {
        super(context);
    }

    @Override
    public BodybuildingModel initModel() {
        return new BodybuildingModel();
    }

    public void getEquipmentType(String mac, String port) {
        getModel().getEquipmentType(mac, port, new BaseResponseListener<EquipmentPortType>() {
            @Override
            public void onResponse(EquipmentPortType data) {
                super.onResponse(data);
                if (data != null) {
                    getView().getEquipmentTypeSuccess(data);
                }
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
//                getView().showError(e.msg);
            }
        });
    }
}
