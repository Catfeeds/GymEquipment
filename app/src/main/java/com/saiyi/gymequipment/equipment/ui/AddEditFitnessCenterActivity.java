package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.activity.LocationActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.equipment.model.bean.FitnessCenter;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.gymequipment.equipment.presenter.AddFitnessCenterPresenter;
import com.saiyi.libfast.event.EventAction;
import com.saiyi.libfast.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加健身中心
 */
public class AddEditFitnessCenterActivity extends BKMVPActivity<AddFitnessCenterPresenter> {

    public static final int SETTING_FC_NAME_REQUEST = 1001;

    public static final String BUNDLE_KEY_FC_NAME = "fc_name";

    @BindView(R.id.fitness_center_name_rl)
    RelativeLayout fitnessCenterNameRl;
    @BindView(R.id.fitness_center_location_rl)
    RelativeLayout fitnessCenterLocationRl;
    @BindView(R.id.fitness_center_dev_rl)
    RelativeLayout fitnessCenterDevRl;
    @BindView(R.id.add_tv)
    TextView addTv;
    @BindView(R.id.add_dev_tv)
    TextView addDevTv;
    @BindView(R.id.fc_name_tv)
    TextView fcNameTv;
    @BindView(R.id.fc_location_tv)
    TextView fcLocationTv;

    private boolean status;

    private GetFitnessCenter getFitnessCenter;

    @Override
    public AddFitnessCenterPresenter initPresenter(Context context) {
        return new AddFitnessCenterPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fitness_center);
    }

    @Override
    protected void initView() {
        super.initView();
        registerEventBus();
        if (FitnessCenterHelper.instance().isEditModel()) {
            mTitleBar.setTitle(R.string.edit_fitness_center);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                getFitnessCenter = (GetFitnessCenter) bundle.get(Constant.BUNDLE_KEY_DATA);
                fcNameTv.setText(getFitnessCenter.getFcdefinition());
                fcLocationTv.setText(getFitnessCenter.getFcaddress());
                addDevTv.setText(R.string.go_to_see);
                addTv.setText(R.string.done);
                addTv.setEnabled(true);
                FitnessCenterHelper.instance().setGetFitnessCenter(getFitnessCenter);
            }
        } else {
            mTitleBar.setTitle(R.string.add_fitness_center);
            FitnessCenterHelper.instance().setAddFitnesscenter(new FitnessCenter());
        }
        getPresenter().getEquipmentTypes();
    }


    @OnClick(R.id.fitness_center_name_rl)
    public void onClickSettingCenterName(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BUNDLE_KEY_DATA, fcNameTv.getText().toString().trim());
        openActivityForResult(SettingCenterNameActivity.class, bundle, SETTING_FC_NAME_REQUEST);
    }

    @OnClick(R.id.fitness_center_location_rl)
    public void onClickSettingCenterLocation(View view) {
        openActivityForResult(LocationActivity.class, LocationActivity.RESULT_LOCATION_REQUEST);
    }

    @OnClick(R.id.fitness_center_dev_rl)
    public void onClickSettingCenterDev(View view) {
        openActivity(EquipmentActivity.class);
    }

    @OnClick(R.id.add_tv)
    public void onClickAddCenter(View view) {
        if (FitnessCenterHelper.instance().isEditModel()) {
            getPresenter().updateFitnessCenter();
        } else {
            getPresenter().addNewFitnessCenter();
        }
    }

    public void showEditLoading() {
        showCustomLoading(getString(R.string.editing));
    }

    public void showAddLoading() {
        showCustomLoading(getString(R.string.adding));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void showSuccessBack() {
        dismissLoading();
        back();
    }

    public void showfailureMsg(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        toast(msg);
    }

    public void showAddfailure() {
        toast(getString(R.string.add_failure));
    }

    private void addTvStatusChange() {
        if (!getString(R.string.go_setting).equals(fcNameTv.getText().toString().trim())
                && !getString(R.string.go_setting).equals(fcLocationTv.getText().toString().trim())
                && status) {
            addTv.setEnabled(true);
        }
    }

    @Override
    public void onMessageEvent(EventAction event) {
        super.onMessageEvent(event);
        if (event == Action.ACTION_ADD_EQUIPMENT) {
            status = true;
            addDevTv.setText(R.string.added);
            addTvStatusChange();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SETTING_FC_NAME_REQUEST && data != null) {
            String fcName = data.getStringExtra(BUNDLE_KEY_FC_NAME);
            if (!TextUtils.isEmpty(fcName)) {
                fcNameTv.setText(fcName);
                if (FitnessCenterHelper.instance().isEditModel()) {
                    FitnessCenterHelper.instance().getGetFitnessCenter().setFcdefinition(fcName);
                } else {
                    FitnessCenterHelper.instance().getFitnessCenter().setFcdefinition(fcName);
                }
            }
        } else if (resultCode == LocationActivity.RESULT_LOCATION_REQUEST) {
            String location = data.getStringExtra(LocationActivity.BUNDLE_KEY_LOCATION_RESULT);
            if (!TextUtils.isEmpty(location)) {
                fcLocationTv.setText(location);
                double longitude = data.getDoubleExtra(LocationActivity.BUNDLE_KEY_LONGITUDE_RESULT, 0);
                double latitude = data.getDoubleExtra(LocationActivity.BUNDLE_KEY_LATITUDE_RESULT, 0);
                if (FitnessCenterHelper.instance().isEditModel()) {
                    FitnessCenterHelper.instance().getGetFitnessCenter().setFcaddress(location);
                    FitnessCenterHelper.instance().getGetFitnessCenter().setFclatitude(latitude);
                    FitnessCenterHelper.instance().getGetFitnessCenter().setFclongitude(longitude);
                } else {
                    FitnessCenterHelper.instance().getFitnessCenter().setFcaddress(location);
                    FitnessCenterHelper.instance().getFitnessCenter().setFclatitude(latitude);
                    FitnessCenterHelper.instance().getFitnessCenter().setFclongitude(longitude);
                }
            }
        }
        addTvStatusChange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FitnessCenterHelper.instance().exitOperation();
    }
}
