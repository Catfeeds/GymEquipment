package com.saiyi.gymequipment.home.model;

import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.model.request.GetUserDataService;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.BaseHttpObserver;
import com.saiyi.libfast.http.BaseResponse;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.ModelImpl;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyModel extends ModelImpl {

    public void getAppUserData(BaseResponseListener<User> listener) {
        GetUserDataService userDataService = createRetorfitService(GetUserDataService.class);
        userDataService.getUserData(UserHelper.instance().getToken())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseHttpObserver<User>(getCompositeDisposable(), listener) {
                    @Override
                    public void onResponse(BaseResponse<User> response) {
                        if (response.isSuccess()) {
                            UserHelper.instance().setmUser(response.getData());
                            dispatchListenerResponse(response.getData());
                        } else {
                            dispatchListenerFaild(ErrorEngine.handleServiceResultError(response.getMessage()));
                        }
                    }
                });
    }
}
