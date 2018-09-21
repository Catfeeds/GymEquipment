package com.saiyi.gymequipment.home.presenter;

import android.content.Context;

import com.saiyi.gymequipment.home.model.MyModel;
import com.saiyi.gymequipment.home.ui.MyFragement;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class MyPresenter extends PresenterImpl<MyFragement, MyModel> {

    public MyPresenter(Context context) {
        super(context);
    }

    @Override
    public MyModel initModel() {
        return new MyModel();
    }

    public void getAppUserData(){
        getModel().getAppUserData(new BaseResponseListener<User>() {
            @Override
            public void onResponse(User data) {
                super.onResponse(data);
                getView().showUserData(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });
    }
}
