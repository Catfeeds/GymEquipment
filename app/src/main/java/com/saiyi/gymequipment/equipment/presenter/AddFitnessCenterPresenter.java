package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.equipment.model.AddFitnessCenterModel;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.equipment.ui.AddEditFitnessCenterActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AddFitnessCenterPresenter extends PresenterImpl<AddEditFitnessCenterActivity, AddFitnessCenterModel> {


    public AddFitnessCenterPresenter(Context context) {
        super(context);
    }

    @Override
    public AddFitnessCenterModel initModel() {
        return new AddFitnessCenterModel();
    }

    public void addNewFitnessCenter() {
        getView().showAddLoading();
        getModel().addNewFitnessCenter(new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().showSuccessBack();
                EventBus.getDefault().post(Action.ACTION_ADD_FITNESS_CENTER);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().showAddfailure();
            }
        });
    }

    public void getEquipmentTypes(){
        getModel().getEquipmentTypes(new BaseResponseListener<List<EquipmentPortType>>() {
            @Override
            public void onResponse(List<EquipmentPortType> data) {
                super.onResponse(data);
                getView().dismissLoading();

            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });
    }

    public void updateFitnessCenter() {
        getView().showEditLoading();
        getModel().updateFitnessCenter(new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().dismissLoading();
                getView().showSuccessBack();
                EventBus.getDefault().post(Action.ACTION_EDIT_FITNESS_CENTER);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().showfailureMsg(e.msg);
            }
        });
    }
}
