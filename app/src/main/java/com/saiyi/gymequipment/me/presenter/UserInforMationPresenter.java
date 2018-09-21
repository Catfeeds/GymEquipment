package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.UserInforMationModel;
import com.saiyi.gymequipment.me.ui.UserInforMationActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.io.File;

public class UserInforMationPresenter extends PresenterImpl<UserInforMationActivity, UserInforMationModel> {

    public UserInforMationPresenter(Context context) {
        super(context);
    }

    @Override
    public UserInforMationModel initModel() {
        return new UserInforMationModel();
    }

    /**修改用户头像*/
    public void uploadUserImg(File file){
        if(file == null)return;
        getModel().uploadUserImg(file, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().setUserImgSuccess();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().setUserImgFaild();
            }
        });
    }

    public void updateUserDate(String nickName, String sex, String brithDay, int height, int weight,String sosPhone) {
        getView().showSetting();
        getModel().updateUserDate(nickName, sex, brithDay, height, weight,sosPhone, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().showSettingSuccess();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
            }
        });
    }
}
