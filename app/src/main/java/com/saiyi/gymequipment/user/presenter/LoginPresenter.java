package com.saiyi.gymequipment.user.presenter;


import android.content.Context;
import android.text.TextUtils;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.bean.AccessToken;
import com.saiyi.gymequipment.bean.WUser;
import com.saiyi.gymequipment.user.model.LoginModel;
import com.saiyi.gymequipment.user.ui.LoginActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.StringUtils;

public class LoginPresenter extends PresenterImpl<LoginActivity, LoginModel> {


    public LoginPresenter(Context context) {
        super(context);
    }

    @Override
    public LoginModel initModel() {
        return new LoginModel();
    }


    public boolean onLogin(String uname, String upwd) {
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(upwd)) {
            getView().showErrorMsg(getContext().getString(R.string.input_uname_pwd));
            return false;
        } else if (!StringUtils.isEmailAddress(uname) && !StringUtils.isMobileNum(uname)) {
            //不为邮箱也不是手机号，显示错误信息
            getView().showErrorMsg(getContext().getString(R.string.uname_format));
            return false;
        }else if(upwd.length() < 6){
            getView().showErrorMsg(getContext().getString(R.string.pwd_number_little));
            return false;
        }
        getView().showLoginLoading();
        getModel().login(uname, upwd, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                //登录成功
                getView().dismissLoading();
                getView().loginSuccess();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().loginFaild(e.msg);
            }
        });
        return true;
    }

    public boolean onWxLogin(AccessToken accessToken, WUser wUser){
        if (accessToken == null || wUser == null) {
            getView().showErrorMsg(getContext().getString(R.string.wx_login_error));
            return false;
        }
        getView().showLoginLoading();
        getModel().wxLogin(accessToken, wUser,new BaseResponseListener<String>() {

            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                //登录成功
                getView().dismissLoading();
                getView().loginSuccess();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
                getView().loginFaild(e.msg);
            }
        });
        return true;
    }
}
