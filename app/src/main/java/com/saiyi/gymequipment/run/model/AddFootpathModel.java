package com.saiyi.gymequipment.run.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.tools.GsonUtil;
import com.saiyi.gymequipment.run.bean.AddFootpathBean;
import com.saiyi.gymequipment.run.event.BroadcastEvent;
import com.saiyi.gymequipment.run.model.request.AddFootpathService;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.mvp.ModelImpl;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddFootpathModel extends ModelImpl {
    public void addFootpath(String name, String distance, ArrayList<BroadcastEvent> eventArrayList, @NonNull ResponseListener<String> listener) {
        AddFootpathService datasService = createRetorfitService(AddFootpathService.class);

        String json = GsonUtil.GsonString(new AddFootpathBean(eventArrayList, name));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        datasService.addFootPathInfo(UserHelper.instance().getToken(), body)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<String>(getCompositeDisposable(), listener) {
                               @Override
                               public void onResponse(BaseResponse<String> response) {
                                   if (response.isSuccess()) {
                                       dispatchListenerResponse(response.getMessage());
                                   } else {
                                       dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                                   }
                               }
                           }
                );
    }
}
