package com.saiyi.gymequipment.home.presenter;


import android.content.Context;
import com.saiyi.gymequipment.home.model.HomeModel;
import com.saiyi.gymequipment.home.ui.HomeActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class HomePresenter extends PresenterImpl<HomeActivity, HomeModel>{


    public HomePresenter(Context context) {
        super(context);
    }

    @Override
    public HomeModel initModel() {
        return new HomeModel();
    }



}
