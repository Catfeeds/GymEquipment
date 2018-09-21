package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.UpdateUserInfomationsModel;
import com.saiyi.gymequipment.me.ui.UpdateUserInfomationsActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class UpdateUserInfomationsPresenter extends PresenterImpl<UpdateUserInfomationsActivity, UpdateUserInfomationsModel> {

    public UpdateUserInfomationsPresenter(Context context) {
        super(context);
    }

    @Override
    public UpdateUserInfomationsModel initModel() {
        return new UpdateUserInfomationsModel();
    }
}
