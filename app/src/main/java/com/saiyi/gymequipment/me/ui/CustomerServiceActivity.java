package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.home.ui.BodybuildingActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JingNing on 2018-08-23 10:13
 */
public class CustomerServiceActivity extends BKMVPActivity {

    @Override
    public PresenterImpl initPresenter(Context context) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.customer_service);
    }


    @OnClick(R.id.iv_scan)
    public void onViewClicked() {
        //点击扫码
        Bundle bundle = new Bundle();
        bundle.putString(ScanByQRActivity.BUNDLE_KEY_TITLE, getString(R.string.scan_qrcode));
        bundle.putString(ScanByQRActivity.BUNDLE_KEY_MESSAGE, getString(R.string.qrcode_tips));
        openActivityForResult(ScanByQRActivity.class, bundle, ScanByQRActivity.RESULT_SCAN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanByQRActivity.RESULT_SCAN_REQUEST && data != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.BUNDLE_KEY_DATA, data.getStringExtra(ScanByQRActivity.BUNDLE_KEY_SCAN_RESULT));
            openActivity(EquipmentInfoActivity.class, bundle);
        }
    }
}
