package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.FitnessRecordModel;
import com.saiyi.gymequipment.equipment.ui.FitnessRecordActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class FitnessRecordPresenter extends PresenterImpl<FitnessRecordActivity, FitnessRecordModel> {

    public FitnessRecordPresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessRecordModel initModel() {
        return new FitnessRecordModel();
    }

}
