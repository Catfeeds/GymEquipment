package com.saiyi.gymequipment.run.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.activity.LocationActivity;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.run.event.BroadcastEvent;
import com.saiyi.libfast.activity.BaseActivity;
import com.saiyi.libfast.activity.view.NavBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加广播包
 */
public class AddBroadcastActivity extends BaseActivity {

    @BindView(R.id.tv_broadcast_mac)
    TextView tv_broadcast_mac;

    @BindView(R.id.tv_broadcast_location)
    TextView tv_broadcast_location;

    @BindView(R.id.tv_broadcast_distance)
    TextView tv_broadcast_distance;

    public static final int BROADCAST_DISTANCE_SETTING = 3;
    private double distance = 0;//广播包距离
    private String mac;//广播包mac
    private String location;//广播包定位位置
    private double latitude;//纬度
    private double longitude;//经度

    public final static int GET_LOCATION_BY_LOCATION = 787;
    private RemindDialog mRemindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_broadcast);

        ButterKnife.bind(this);
        //注册成为订阅者
        EventBus.getDefault().register(this);

        mTitleBar.setTitle(R.string.add_broadcast_info);
        mTitleBar.setClickListener(navBarOnClickListener);

        if (getIntent() != null) {
            mac = getIntent().getStringExtra(ScanByQRActivity.BUNDLE_KEY_SCAN_RESULT);
            if (!TextUtils.isEmpty(mac)) {
                tv_broadcast_mac.setText(mac);
            }
        }

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.rl_broadcast_location, R.id.rl_broadcast_distance, R.id.tv_finish_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_broadcast_location:
                openActivityForResult(LocationActivity.class, GET_LOCATION_BY_LOCATION);
                break;
            case R.id.rl_broadcast_distance:
                Intent intent = new Intent(AddBroadcastActivity.this, FootPathSettingActivity.class);
                intent.putExtra(AddFootpathActivity.INFO_TYPE_FOR_EDIT, BROADCAST_DISTANCE_SETTING);
                startActivityForResult(intent, BROADCAST_DISTANCE_SETTING);
                break;
            case R.id.tv_finish_add:
                addBroadcast();
                break;
            default:
                break;
        }
    }

    private void addBroadcast() {
        if (TextUtils.isEmpty(mac)) {
            Toast.makeText(this, getString(R.string.please_add_mac), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, getString(R.string.please_set_location), Toast.LENGTH_SHORT).show();
            return;
        }

        if (distance == 0) {
            Toast.makeText(this, getString(R.string.please_set_distance), Toast.LENGTH_SHORT).show();
            return;
        }

        EventBus.getDefault().post(new BroadcastEvent(location, distance, latitude, longitude, mac));
        finish();

    }

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
            if (distance == 0 || !TextUtils.isEmpty(mac) || !TextUtils.isEmpty(location)) {
                showCancelSetting();
            } else {
                back();
            }
        }

        @Override
        public void onLeftSenIconClick(View view) {
        }

        @Override
        public void onLeftTxtClick(View view) {

        }

        @Override
        public void onRightIconClick(View view) {
            openActivity(AddFootpathActivity.class);
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 5) {
            String value = data.getStringExtra(FootPathSettingActivity.FOOT_PATH_INFORMATION);
            if (!TextUtils.isEmpty(value)){
                distance = Double.parseDouble(value);
                if (distance != 0) {
                    tv_broadcast_distance.setText(distance + getString(R.string.rice));
                }
            }
        }

        if (resultCode == LocationActivity.RESULT_LOCATION_REQUEST) {
            location = data.getStringExtra(LocationActivity.BUNDLE_KEY_LOCATION_RESULT);
            tv_broadcast_location.setText(location);
            latitude = data.getDoubleExtra(LocationActivity.BUNDLE_KEY_LATITUDE_RESULT, -100);
            longitude = data.getDoubleExtra(LocationActivity.BUNDLE_KEY_LONGITUDE_RESULT, -100);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (distance == 0 || !TextUtils.isEmpty(mac) || !TextUtils.isEmpty(location)) {
                showCancelSetting();
            } else {
                back();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 是否放弃此次编辑
     */
    private void showCancelSetting() {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(getString(R.string.give_up_edit));
        dialog.setMsgText(getString(R.string.content_is_not_empty));
        dialog.setComplateTextColorRes(R.color.colorAccent).setComplateText(getString(R.string.confirm));
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mOpenSettingDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    back();
                }
            }
        }
    };

    //弹窗消失
    private void dismissRemindDialog() {
        if (mRemindDialog != null && mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
    }
}
