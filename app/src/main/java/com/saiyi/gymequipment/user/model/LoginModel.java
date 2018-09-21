package com.saiyi.gymequipment.user.model;


import com.saiyi.gymequipment.bean.AccessToken;
import com.saiyi.gymequipment.bean.WUser;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.user.model.bean.Account;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.gymequipment.user.model.request.LoginService;
import com.saiyi.gymequipment.user.tool.http.LoginHttpObserver;
import com.saiyi.gymequipment.user.tool.http.LoginResponse;
import com.saiyi.libfast.error.DB.DBError;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginModel extends ModelImpl {

    public void login(final String uphone, final String upwd, ResponseListener<String> listener) {
        LoginService loginService = createRetorfitService(LoginService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("uphone", uphone);
            result.put("upwd", upwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        loginService.login(body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoginHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(LoginResponse<String> response) {
                        if (response.isSuccess()) {
                            if (UserHelper.instance().login(new Account(uphone, upwd), new User(uphone, response.getData(), response.getIsAuthorize()))) {
                                dispatchListenerResponse(response.getData());
                            } else {
                                dispatchListenerFaild(ErrorEngine.handleCustomError(DBError.UserSaveError));
                            }
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }


    public void wxLogin(final AccessToken accessToken, final WUser wUser, ResponseListener<String> listener) {
        LoginService loginService = createRetorfitService(LoginService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("uimg", wUser.getHeadimgurl());
            result.put("unickname", wUser.getNickname());
            result.put("uopenid", wUser.getOpenid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        loginService.login(body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LoginHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(LoginResponse<String> response) {
                        if (response.isSuccess()) {
                            if (UserHelper.instance().login(new Account(wUser.getOpenid(), accessToken.getRefresh_token()), new User(wUser.getOpenid(), response.getData(), response.getIsAuthorize()))) {
                                dispatchListenerResponse(response.getData());
                            } else {
                                dispatchListenerFaild(ErrorEngine.handleCustomError(DBError.UserSaveError));
                            }
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }

}
