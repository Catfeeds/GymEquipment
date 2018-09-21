package com.saiyi.gymequipment.user.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.user.model.RegisterModel;
import com.saiyi.gymequipment.user.ui.RegisterActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.StringUtils;

public class RegisterPresenter extends PresenterImpl<RegisterActivity, RegisterModel> {

    static String TAG = "RegisterPresenter ";

    private boolean bVCode = false;

    public RegisterPresenter(Context context) {
        super(context);
    }

    @Override
    public RegisterModel initModel() {
        return new RegisterModel();
    }

    public boolean getValidCode(String value) {
        if (TextUtils.isEmpty(value) || (!StringUtils.isMobileNum(value) && !StringUtils.isEmailAddress(value))) {
            getView().showErrorMsg(getContext().getString(R.string.input_user_name));
            return false;
        }
        getModel().getValidCode(value, new BaseResponseListener<String>() {

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                Logger.d(TAG + "onFailed msg:" + e.msg);
                getView().getValidCodeFaild(e.msg);
            }

            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                Logger.d(TAG + "OnResponse msg:" + data);
                bVCode = true;
                getView().getValidCodeSuccess(data);
            }

        });
        return true;
    }

    public boolean register(String value, String pwd, String prePwd, String identifyCode) {
        //点击注册按钮
        if (!StringUtils.isEmailAddress(value) && !StringUtils.isMobileNum(value)) {
            getView().showErrorMsg(getContext().getString(R.string.input_user_name));
            return false;
        } else if (!bVCode) {
            getView().showErrorMsg(getContext().getString(R.string.not_valid_code));
            return false;
        } else if (TextUtils.isEmpty(identifyCode)) {
            getView().showErrorMsg(getContext().getString(R.string.input_valid_code));
            return false;
        } else if (!StringUtils.equals(pwd, prePwd)) {
            getView().showErrorMsg(getContext().getString(R.string.pwd_inconsistencies));
            return false;
        }else if(pwd.length() < 6){
            getView().showErrorMsg(getContext().getString(R.string.pwd_number_little));
            return false;
        }
        getView().showLoadingDialog();
        getModel().register(value, pwd, identifyCode, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                //注册完成
                getView().onRegisterSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().onRegisteFaild(e.msg);
            }
        });
        return true;
    }
}
