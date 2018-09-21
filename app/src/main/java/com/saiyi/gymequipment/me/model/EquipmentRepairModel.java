package com.saiyi.gymequipment.me.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.gymequipment.me.model.request.AddFeedbackImgService;
import com.saiyi.gymequipment.me.model.request.AddFeedbackService;
import com.saiyi.gymequipment.me.model.request.GetQuestionsService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EquipmentRepairModel extends ModelImpl {

    public void getQuestions(BaseResponseListener<List<Question>> listener) {
        GetQuestionsService questionsService = createRetorfitService(GetQuestionsService.class);
        questionsService.getQuestions(UserHelper.instance().getToken())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<List<Question>>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<List<Question>> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }

    public void addFeedback(String mac, String qmsg, int id, String image, BaseResponseListener<String> listener) {
        AddFeedbackService feedbackService = createRetorfitService(AddFeedbackService.class);
        JSONObject result = new JSONObject();
        try {
            result.put("fcontent", qmsg);
            result.put("fimg", image);
            result.put("mac", mac);
            result.put("qid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), result.toString());
        feedbackService.addFeedback(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<String> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }


    public void addFeedbackImg(File imgFile, BaseResponseListener<String> listener) {
        AddFeedbackImgService imgService = createRetorfitService(AddFeedbackImgService.class);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

        imgService.addImg(UserHelper.instance().getToken(), body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<String> response) {
                        if (response.isSuccess()) {
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getCode()));
                        }
                    }
                });
    }
}
