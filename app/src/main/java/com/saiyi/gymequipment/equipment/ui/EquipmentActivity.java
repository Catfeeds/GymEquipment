package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.saiyi.gymequipment.app.GymBuildConfig;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.equipment.adapter.EquipmentAdapter;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.model.bean.FitnessCenter;
import com.saiyi.gymequipment.equipment.model.bean.GetEquipment;
import com.saiyi.gymequipment.equipment.presenter.EquipmentPresenter;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.home.adapter.SpaceItemDecoration;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.event.EventAction;
import com.saiyi.libfast.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 设备activity
 */
public class EquipmentActivity extends BKMVPActivity<EquipmentPresenter> {

    @BindView(R.id.dev_list_rv)
    RecyclerView devListRv;
    @BindView(R.id.dev_ll)
    LinearLayout devLl;
    @BindView(R.id.add_dev_tv)
    TextView addDevTv;

    private String devId;
    private EquipmentAdapter equipmentAdapter;
    private List<Equipment> list;
    private RemindDialog mRemindDialog;
    private Equipment delEq;


    @Override
    public EquipmentPresenter initPresenter(Context context) {
        return new EquipmentPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
    }

    @Override
    protected void initView() {
        super.initView();
        registerEventBus();
        mTitleBar.setTitle(R.string.fitness_center_dev);
        mTitleBar.setRightImageResource(R.drawable.nav_add);
        mTitleBar.showRightIcon();
        mTitleBar.setClickListener(navBarOnClickListener);
        if (FitnessCenterHelper.instance().isEditModel()) {
            getPresenter().getEquipments();
        }
        list = new ArrayList<Equipment>();
        equipmentAdapter = new EquipmentAdapter(R.layout.adapter_equipment, list);
        equipmentAdapter.setItemDeleteClick(onItemDeleteClick);
        equipmentAdapter.setOnItemClickListener(onItemClickListener);
        devListRv.setLayoutManager(new LinearLayoutManager(this));
        devListRv.addItemDecoration(new SpaceItemDecoration(20));
        devListRv.setAdapter(equipmentAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (!FitnessCenterHelper.instance().isEditModel()) {
            list = FitnessCenterHelper.instance().getFitnessCenter().getEquipment();
        }
        if (list == null || list.size() == 0) {
            noDataShow();
        } else {
            showData();
        }
    }

    protected EquipmentAdapter.OnItemDeleteClick onItemDeleteClick = new EquipmentAdapter.OnItemDeleteClick() {
        @Override
        public void onDeleteClick(Equipment item) {
            delEq = item;
            showDeleteEquipmentDialog(item);
        }
    };

    //删除设备弹窗
    private void showDeleteEquipmentDialog(Equipment item) {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(String.format(getResources().getString(R.string.delete_dev_tips), item.getEmac()));
        dialog.setMsgText("");
        dialog.setComplateTextColorRes(R.color.red_ff3d3d);
        dialog.setClick(mDeleteDeviceDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mDeleteDeviceDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    if (delEq != null) {
                        getPresenter().deleteEquipment(delEq.getEmac());
                    }
                    delEq = null;
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

    protected BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (UserHelper.instance().getUser().getIsAuthorize() == GymBuildConfig.isAdmin) {
                //跳转至设备详情
                Equipment equipment = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putBoolean(AddEditEquipmentActivity.BUNDLE_KEY_EQUIPMENT_STATUS, AddEditEquipmentActivity.BUNDLE_VALUE_EDIT_EQUIPMENT);
                bundle.putSerializable(AddEditEquipmentActivity.BUNDLE_KEY_EDIT_EQUIPMENT, equipment);
                openActivity(AddEditEquipmentActivity.class, bundle);
            }else{
                toast(R.string.not_admin_edit_tips);
            }
        }
    };

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
            back();
        }

        @Override
        public void onLeftSenIconClick(View view) {

        }

        @Override
        public void onLeftTxtClick(View view) {

        }

        @Override
        public void onRightIconClick(View view) {
            //二维码
            openActivityForResult(ScanByQRActivity.class, ScanByQRActivity.RESULT_SCAN_REQUEST);
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };

    private void noDataShow() {
        addDevTv.setVisibility(View.VISIBLE);
        devLl.setVisibility(View.GONE);
    }

    private void showData() {
        addDevTv.setVisibility(View.GONE);
        devLl.setVisibility(View.VISIBLE);
        equipmentAdapter.setNewData(list);
    }


    public void showLoading() {
        showProgressDialog();
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void getEquipmentsSuccess(List<Equipment> data) {
        list = data;
        refresh();
    }

    public void onFailed(String errMsg) {
        toast(errMsg);
    }

    public void deleteEquipmentSuccess(String msg) {
        toast(msg);
        if (FitnessCenterHelper.instance().isEditModel()) {
            getPresenter().getEquipments();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanByQRActivity.RESULT_SCAN_REQUEST && data != null) {
            devId = data.getStringExtra(ScanByQRActivity.BUNDLE_KEY_SCAN_RESULT);
            Equipment equipment = null;
            if (FitnessCenterHelper.instance().isEditModel()) {
                equipment = getEquipmentByEMac(devId);
            } else {
                equipment = FitnessCenterHelper.instance().getEquipmentByEMac(devId);
            }
            Bundle bundle = new Bundle();
            if (equipment == null) {
                bundle.putBoolean(AddEditEquipmentActivity.BUNDLE_KEY_EQUIPMENT_STATUS, AddEditEquipmentActivity.BUNDLE_VALUE_ADD_EQUIPMENT);
                bundle.putString(AddEditEquipmentActivity.BUNDLE_KEY_DEV_ID, devId);
            } else {
                bundle.putBoolean(AddEditEquipmentActivity.BUNDLE_KEY_EQUIPMENT_STATUS, AddEditEquipmentActivity.BUNDLE_VALUE_EDIT_EQUIPMENT);
                bundle.putSerializable(AddEditEquipmentActivity.BUNDLE_KEY_EDIT_EQUIPMENT, equipment);
            }
            openActivity(AddEditEquipmentActivity.class, bundle);
        }
    }

    public @Nullable
    Equipment getEquipmentByEMac(String eMac) {
        if (TextUtils.isEmpty(eMac)) return null;
        for (Equipment equipment : list) {
            if (eMac.equals(equipment.getEmac())) return equipment;
        }
        return null;
    }

    @Override
    public void onMessageEvent(EventAction event) {
        super.onMessageEvent(event);
        if (event == Action.ACTION_REFRESH_DEVICE_LSIT) {
            if (FitnessCenterHelper.instance().isEditModel()) {
                getPresenter().getEquipments();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (list != null && list.size() > 0) {
            EventBus.getDefault().post(Action.ACTION_ADD_EQUIPMENT);
        }
    }
}
