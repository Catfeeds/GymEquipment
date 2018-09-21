package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.SettingCenterNameModel;
import com.saiyi.gymequipment.equipment.ui.SettingCenterNameActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class SettingCenterNamePresenter extends PresenterImpl<SettingCenterNameActivity, SettingCenterNameModel> {

    public SettingCenterNamePresenter(Context context) {
        super(context);
    }

    @Override
    public SettingCenterNameModel initModel() {
        return new SettingCenterNameModel();
    }
}
