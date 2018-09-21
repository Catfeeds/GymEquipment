package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.me.model.bean.EquipmentInfo;
import com.saiyi.gymequipment.me.presenter.EquipmentInfoPresenter;
import com.saiyi.libfast.utils.DateUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JingNing on 2018-08-23 14:23
 */
public class EquipmentInfoActivity extends BKMVPActivity<EquipmentInfoPresenter> {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_expiry)
    TextView tvExpiry;
    @BindView(R.id.tv_guide_video)
    TextView tvGuideVideo;
    @BindView(R.id.tv_equipment_repair)
    TextView tvEquipmentRepair;

    private String mac;
    private EquipmentInfo equipmentInfo;

    @Override
    public EquipmentInfoPresenter initPresenter(Context context) {
        return new EquipmentInfoPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.equipment_info);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mac = bundle.getString(Constant.BUNDLE_KEY_DATA);
            getPresenter().getEquipmentInfo(mac);
        } else {
            showError();
        }
    }

    public void showError() {
        toast(R.string.qr_code_error);
        back();
    }

    public void showGetDataDialog() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void showNotDevice() {
        toast(R.string.not_device);
        back();
    }

    public void showEquipmentInfo(EquipmentInfo equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
        Picasso.with(this).load(equipmentInfo.getHgimg()).into(ivImg);
        tvName.setText(equipmentInfo.getEtname());
        tvId.setText(equipmentInfo.getEmac());
        tvAddress.setText(equipmentInfo.getFcaddress());
        tvExpiry.setText(DateUtils.formatYMD(equipmentInfo.getExpireddate().longValue()));
    }

    @OnClick({R.id.tv_guide_video, R.id.tv_equipment_repair})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_guide_video:
                bundle.putSerializable(Constant.BUNDLE_KEY_DATA, equipmentInfo);
                openActivity(GuideVideoActivity.class, bundle);
                break;
            case R.id.tv_equipment_repair:
                bundle.putString(Constant.BUNDLE_KEY_DATA, mac);
                openActivity(EquipmentRepairActivity.class, bundle);
                break;
        }
    }
}
