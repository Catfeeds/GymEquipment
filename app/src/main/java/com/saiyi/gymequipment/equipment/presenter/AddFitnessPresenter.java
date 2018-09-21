package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.equipment.model.AddFitnessModel;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPort;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.equipment.ui.AddEditEquipmentActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AddFitnessPresenter extends PresenterImpl<AddEditEquipmentActivity, AddFitnessModel> {

    public AddFitnessPresenter(Context context) {
        super(context);
    }

    @Override
    public AddFitnessModel initModel() {
        return new AddFitnessModel();
    }

    //添加健身中心模式，把设备添加健身中心模型，在后面一起提交
    public void AddDone(Equipment equipment) {
        getView().showAddLoading();
        List<Equipment> equipmentList = FitnessCenterHelper.instance().getFitnessCenter().getEquipment();
        for (int i = 0; i < equipmentList.size(); i++) {
            if (equipment.getEmac().equals(equipmentList.get(i).getEmac())) {
                FitnessCenterHelper.instance().getFitnessCenter().getEquipment().set(i, equipment);
                getView().showAddEditSuccess();
                return;
            }
        }
        if (FitnessCenterHelper.instance().getFitnessCenter().getEquipment().add(equipment)) {
            getView().showAddEditSuccess();
            return;
        } else {
            getView().showAddfailure();
            return;
        }
    }

    public void addPort(String port) {
        if (port == null) return;
        List<EquipmentPortType> list = FitnessCenterHelper.instance().getEquipmentPortTypeList();
        for (EquipmentPortType portType : list) {
            if (port.equals(portType.getEtname())) {
                getView().AddPortSuccess(portType);
            }
        }
    }

    //编辑健身中心模式，添加一个设备，发给服务器
    public void addDevice(int fcID, String eMac, List<EquipmentPort> ports) {
        getView().showAddLoading();
        getModel().addDevice(fcID, eMac, ports, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().showAddEditSuccess();
                EventBus.getDefault().post(Action.ACTION_REFRESH_DEVICE_LSIT);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().showAddEditfailure(e.msg);
            }
        });
    }

    public void updateDevice(int fcID, String eMac, List<EquipmentPort> ports) {
        getView().showAddLoading();
        getModel().updateDevice(fcID, eMac, ports, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().showAddEditSuccess();
                EventBus.getDefault().post(Action.ACTION_REFRESH_DEVICE_LSIT);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().showAddEditfailure(e.msg);
            }
        });
    }
}
