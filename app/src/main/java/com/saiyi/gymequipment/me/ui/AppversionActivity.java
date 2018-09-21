package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.app.GymBuildConfig;
import com.saiyi.libfast.activity.BaseActivity;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppversionActivity extends BKMVPActivity {


    @BindView(R.id.appversion_tv)
    TextView appversionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appversion);
    }

    @Override
    public PresenterImpl initPresenter(Context context) {
        return null;
    }

    @Override
    protected void initView() {
        getTitleBar().setTitle(getString(R.string.versionn_iformation));
        appversionTv.setText(AppUtil.getVersionName(this));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
