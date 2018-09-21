package com.saiyi.gymequipment.user.model;

import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.user.model.request.GetIdentifyService;
import com.saiyi.gymequipment.user.model.request.ResetPwdService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ForgetPasswordModel extends ModelImpl{

    /**
     * 获取短信验证码
     */
    public void getValidCode(String value, final ResponseListener<String> listener) {
        GetIdentifyService getIdentifyService = createRetorfitService(GetIdentifyService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("phone", value);
            result.put("type", RequestConstans.GET_IDENTIFY_TYPE_FIND_BACK_PWD);
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

    public void backPwd(String uphone, String pwd,String identifyCode,final ResponseListener<String> listener){
        ResetPwdService pwdService = createRetorfitService(ResetPwdService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("code", identifyCode);
            result.put("uphone", uphone);
            result.put("upwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        pwdService.resetPwd(body)
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
                });
    }
}
