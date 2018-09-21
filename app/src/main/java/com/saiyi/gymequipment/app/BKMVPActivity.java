package com.saiyi.gymequipment.app;


import android.os.Bundle;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.activity.BaseMVPActivity;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/16.
 */

public abstract class BKMVPActivity<P extends PresenterImpl> extends BaseMVPActivity<P> {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //修复ButterKnife框架在分model下开发无法注入的bug
        //https://github.com/JakeWharton/butterknife/issues/1127
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", UserHelper.instance().getUser());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        UserHelper.instance().setmUser((User) savedInstanceState.get("user"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
