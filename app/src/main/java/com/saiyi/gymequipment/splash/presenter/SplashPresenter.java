package com.saiyi.gymequipment.splash.presenter;

import android.content.Context;

import com.saiyi.gymequipment.splash.model.SplashModel;
import com.saiyi.gymequipment.splash.ui.SplashActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;

public class SplashPresenter extends PresenterImpl<SplashActivity, SplashModel> {

    private final long SPALISH_WAIT_TIME = 1500;
    private boolean isWaitEnd;//等待时候结束
    private boolean isLoginEnd;//时候登陆结束

    public SplashPresenter(Context context) {
        super(context);
    }

    @Override
    public SplashModel initModel() {
        return new SplashModel();
    }

    public void waitSpalish() {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getModel().hasLogined()) {
                    //已登录跳转到主页
                    getView().goHomeActivity();
                } else {
                    //在等待时间内完成了登陆
                    if (isLoginEnd) {
                        getView().goLoginActivity();
                    } else {
                        //在等待时间内未完成
                        isWaitEnd = true;
                    }
                }
            }
        }, SPALISH_WAIT_TIME);
    }

    public void autoLogin() {
        isLoginEnd = true;
        getModel().autoLogin(new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                isLoginEnd = true;
                if (!isWaitEnd) return;
                //在等待时间内未完成
                getView().goHomeActivity();
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                isLoginEnd = true;
                if (!isWaitEnd) return;
                //在等待时间内未完成
                getView().goLoginActivity();
            }
        });
    }
}
