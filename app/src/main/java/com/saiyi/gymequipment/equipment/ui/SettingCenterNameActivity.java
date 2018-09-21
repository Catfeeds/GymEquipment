package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.equipment.presenter.SettingCenterNamePresenter;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.libfast.activity.view.NavBar;

import butterknife.BindView;

public class SettingCenterNameActivity extends BKMVPActivity<SettingCenterNamePresenter> {

    @BindView(R.id.fc_name_et)
    EditText fcNameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center_name);
    }

    @Override
    public SettingCenterNamePresenter initPresenter(Context context) {
        return new SettingCenterNamePresenter(context);
    }

    @Override
    protected void initView() {
        initTitle();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            String fcName = bundle.getString(Constant.BUNDLE_KEY_DATA);
            if(!getString(R.string.go_setting).equals(fcName)){
                fcNameEt.setText(fcName);
                fcNameEt.setSelection(fcNameEt.getText().length());
            }
        }
    }

    private void initTitle() {
        mTitleBar.setTitle(R.string.setting_center_name);
        mTitleBar.hiddenLeftIcon();
        mTitleBar.showLeftText();
        mTitleBar.showRightText();
        mTitleBar.setLeftText(R.string.cancel);
        mTitleBar.setRightText(R.string.confirm);
        mTitleBar.setClickListener(navBarOnClickListener);
    }

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
        }

        @Override
        public void onLeftSenIconClick(View view) {
        }

        @Override
        public void onLeftTxtClick(View view) {
            back();
        }

        @Override
        public void onRightIconClick(View view) {
        }

        @Override
        public void onRightTxtClick(View view) {
            onConfirm();
        }
    };

    private void onConfirm() {
        String name = fcNameEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            toast(R.string.input_fc_name);
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(AddEditFitnessCenterActivity.BUNDLE_KEY_FC_NAME, name);
        setResult(AddEditFitnessCenterActivity.SETTING_FC_NAME_REQUEST, intent);
        back();
    }
}
