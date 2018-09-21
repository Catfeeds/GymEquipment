package com.saiyi.gymequipment.user.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.user.model.request.UpdateUserDatasService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SupplementInfoModel extends ModelImpl {

    public void updateUserDate(String nickName, String sex, String brithDay, int height, int weight, String sosPhone,@NonNull ResponseListener<String> listener) {
        UpdateUserDatasService datasService = createRetorfitService(UpdateUserDatasService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("unickname", nickName);
            result.put("ugender", sex);
            result.put("ubirthday", brithDay);
            result.put("uheight", height);
            result.put("uweight", weight);
            result.put("usosPhone", sosPhone);
            result.put("uphone", UserHelper.instance().getAccount().getUname());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        datasService.updateUser(UserHelper.instance().getToken(), body)
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
}
