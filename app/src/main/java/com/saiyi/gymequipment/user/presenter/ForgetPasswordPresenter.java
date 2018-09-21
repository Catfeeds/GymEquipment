package com.saiyi.gymequipment.user.presenter;

import android.content.Context;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.user.model.ForgetPasswordModel;
import com.saiyi.gymequipment.user.ui.ForgetPasswordActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.StringUtils;

public class ForgetPasswordPresenter extends PresenterImpl<ForgetPasswordActivity, ForgetPasswordModel> {

    private boolean bVCode = false;

    public ForgetPasswordPresenter(Context context) {
        super(context);
    }

    @Override
    public ForgetPasswordModel initModel() {
        return new ForgetPasswordModel();
    }

    public boolean getValidCode(String value) {
        if (!StringUtils.isEmailAddress(value) && !StringUtils.isMobileNum(value)) {
            getView().showErrorMsg(getContext().getString(R.string.input_user_name));
            return false;
        }
        getModel().getValidCode(value, new BaseResponseListener<String>() {

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                Logger.d("ForgetPasswordPresenter onFailed msg:" + e.msg);
                getView().getValidCodeFaild(e.msg);
            }

            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                Logger.d("ForgetPasswordPresenter OnResponse msg:" + data);
                bVCode = true;
                getView().getValidCodeSuccess(data);
            }

        });
        return true;
    }

    public void backPwd(String value, String pwd, String prePwd, String identifyCode) {
        //点击按钮
        if (!StringUtils.isEmailAddress(value) && !StringUtils.isMobileNum(value)) {
            getView().showErrorMsg(getContext().getString(R.string.input_user_name));
            return;
        } else if (!pwd.equals(prePwd)) {
            getView().showErrorMsg(getContext().getString(R.string.pwd_inconsistencies));
            return;
        }else if(pwd.length() < 6){
            getView().showErrorMsg(getContext().getString(R.string.pwd_number_little));
            return;
        }
        getView().showLoadingDialog();
        getModel().backPwd(value, pwd, identifyCode, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().onBackPwdSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().onBackPwdFaild(e.msg);
            }
        });
    }
}
