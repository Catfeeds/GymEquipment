package com.saiyi.gymequipment.me.model;

import android.support.annotation.NonNull;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.request.UploadUserImgService;
import com.saiyi.gymequipment.user.model.request.UpdateUserDatasService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;
import com.saiyi.libfast.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInforMationModel extends ModelImpl {

    /**
     * 上传头像
     */
    public void uploadUserImg(File imgFile, @NonNull ResponseListener<String> listener) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

        UploadUserImgService uploadUserImgService = createRetorfitService(UploadUserImgService.class);
        uploadUserImgService.uploadUserImg(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<String> response) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                UserHelper.instance().updateUserImg(response.getData());
                            }
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getCode()));
                        }
                    }
                });
    }

    public void updateUserDate(String nickName, String sex, String brithDay, int height, int weight, String sosPhone, @io.reactivex.annotations.NonNull ResponseListener<String> listener) {
        UpdateUserDatasService datasService = createRetorfitService(UpdateUserDatasService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("unickname", nickName);
            result.put("ugender", sex);
            result.put("ubirthday", brithDay);
            result.put("uheight", height);
            result.put("uweight", weight);
            if(StringUtils.isMobileNum(UserHelper.instance().getAccount().getUname())){
                result.put("uphone", UserHelper.instance().getAccount().getUname());
            }else{
                result.put("uopenid", UserHelper.instance().getAccount().getUname());
            }
            result.put("usosPhone", sosPhone);

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
