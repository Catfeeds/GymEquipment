package com.saiyi.gymequipment.user.model;

import android.text.TextUtils;

import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.user.model.request.GetIdentifyService;
import com.saiyi.gymequipment.user.model.request.RegisterService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.error.http.MsgError;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;
import com.saiyi.libfast.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterModel extends ModelImpl {

    static String TAG = "RegisterModel ";

    /**
     * 获取短信验证码
     */
    public void getValidCode(String value, final ResponseListener<String> listener) {
        GetIdentifyService getIdentifyService = createRetorfitService(GetIdentifyService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("phone", value);
            result.put("type", RequestConstans.GET_IDENTIFY_TYPE_REGISTER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        getIdentifyService.getIdentify(body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<String> response) {
                                   if (response.isSuccess()) {
                                       String msg = response.getMessage();
                                       dispatchListenerResponse(msg);
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }

    /**
     * 用户注册
     */
    public void register(String value, String pwd, String identifyCode, @NonNull ResponseListener<String> listener) {
        RegisterService registerService = createRetorfitService(RegisterService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("code", identifyCode);
            result.put("uphone", value);
            result.put("upwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        registerService.registered(body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createRegisterObserver(getCompositeDisposable(), listener));
    }

    private BaseHttpObserver<String> createRegisterObserver(CompositeDisposable compositeDisposable, ResponseListener<String> listener) {
        return new BaseHttpObserver<String>(compositeDisposable, listener) {
            @Override
            public void onResponse(BaseResponse<String> response) {
                if (response.isSuccess()) {
                    getResponseListener().onResponse(response.getMessage());
                } else {
                    getResponseListener().onFailed(ErrorEngine.handleServiceResultError(response.getMessage()));
                }
            }
        };
    }
}
