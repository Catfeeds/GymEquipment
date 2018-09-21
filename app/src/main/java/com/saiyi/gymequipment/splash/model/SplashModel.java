package com.saiyi.gymequipment.splash.model;

import com.saiyi.gymequipment.WeChatManager;
import com.saiyi.gymequipment.bean.AccessToken;
import com.saiyi.gymequipment.bean.WUser;
import com.saiyi.gymequipment.common.error.AppError;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.user.model.bean.Account;
import com.saiyi.gymequipment.user.model.request.LoginService;
import com.saiyi.gymequipment.user.tool.http.LoginHttpObserver;
import com.saiyi.gymequipment.user.tool.http.LoginResponse;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;
import com.saiyi.libfast.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SplashModel extends ModelImpl {

    /**
     * 判断当前用户是否已登录
     */
    public boolean hasLogined() {
        return UserHelper.instance().hasLogin();
    }

    /**自动登陆*/
    public void autoLogin(final ResponseListener<String> listener){
        if(UserHelper.instance().hasAccount()){
            final Account account = UserHelper.instance().getAccount();
            if(StringUtils.isMobileNum(account.getUname())){
                LoginService service = createRetorfitService(LoginService.class);
                JSONObject result = new JSONObject();
                try {
                    result.put("uphone", account.getUname());
                    result.put("upwd", account.getUpwd());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
                service.login(body)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new LoginHttpObserver<String>(getCompositeDisposable(), listener) {

                            @Override
                            public void onResponse(LoginResponse<String> response) {
                                if(response.isSuccess()){
                                    UserHelper.instance().autoLogin(account.getUname(),response.getData(),response.getIsAuthorize());
                                    dispatchListenerResponse(response.getData());
                                }else{
                                    dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getCode()));
                                }
                            }
                        });
            }else{
                WeChatManager.getInstance().setListener(new WeChatManager.AuthListener() {
                    @Override
                    public void onSuccess(AccessToken accessToken, WUser wUser) {
                        LoginService service = createRetorfitService(LoginService.class);
                        JSONObject result = new JSONObject();
                        try {
                            result.put("uimg", wUser.getHeadimgurl());
                            result.put("unickname", wUser.getNickname());
                            result.put("uopenid", wUser.getOpenid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
                        service.login(body)
                                .subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new LoginHttpObserver<String>(getCompositeDisposable(), listener) {

                                    @Override
                                    public void onResponse(LoginResponse<String> response) {
                                        if(response.isSuccess()){
                                            UserHelper.instance().autoLogin(account.getUname(),response.getData(),response.getIsAuthorize());
                                            dispatchListenerResponse(response.getData());
                                        }else{
                                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getCode()));
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onFailed() {

                    }
                });
                WeChatManager.getInstance().refreshAccessToken(account.getUpwd());
            }

        }else{
            if(listener != null){
                listener.onFailed(ErrorEngine.handleCustomError(AppError.USER_NOT_LOGIN));
            }
        }
    }
}
