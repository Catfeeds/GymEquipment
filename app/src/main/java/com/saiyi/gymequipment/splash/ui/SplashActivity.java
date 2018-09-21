package com.saiyi.gymequipment.splash.ui;

import android.content.Context;
import android.os.Bundle;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.home.ui.HomeActivity;
import com.saiyi.gymequipment.splash.presenter.SplashPresenter;
import com.saiyi.gymequipment.user.ui.LoginActivity;

public class SplashActivity extends BKMVPActivity<SplashPresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public SplashPresenter initPresenter(Context context) {
        return new SplashPresenter(context);
    }

    @Override
    protected void initView() {
        super.initView();
        hiddenTitleBar();
        getPresenter().autoLogin();
        getPresenter().waitSpalish();
    }

    /**
     * 跳转到登录页面
     */
    public void goLoginActivity() {
        openActivity(LoginActivity.class);
        back();
    }

    /**
     * 跳转到主页
     */
    public void goHomeActivity() {
        openActivity(HomeActivity.class);
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
