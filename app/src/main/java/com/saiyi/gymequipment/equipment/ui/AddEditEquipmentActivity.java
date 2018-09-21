package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.common.view.wheelview.DataPickBuilder;
import com.saiyi.gymequipment.common.view.wheelview.DataPickInterface;
import com.saiyi.gymequipment.common.view.wheelview.DataPickView;
import com.saiyi.gymequipment.equipment.adapter.DeviceAdapter;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPort;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.equipment.presenter.AddFitnessPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.event.EventAction;
import com.saiyi.libfast.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加健身器材设备
 */
public class AddEditEquipmentActivity extends BKMVPActivity<AddFitnessPresenter> {

    public static final String BUNDLE_KEY_EDIT_EQUIPMENT = "edit_equipment";

    public static final String BUNDLE_KEY_EQUIPMENT_STATUS = "equipment_status";

    public static final String BUNDLE_KEY_DEV_ID = "dev_id";

    /**
     * 添加设备
     */
    public static final boolean BUNDLE_VALUE_ADD_EQUIPMENT = true;

    /**
     * 编辑设备
     */
    public static final boolean BUNDLE_VALUE_EDIT_EQUIPMENT = false;

    @BindView(R.id.dev_id_tv)
    TextView devIdTv;

    @BindView(R.id.dev_location_tv)
    TextView devLocationTv;

    @BindView(R.id.device_rv)
    RecyclerView deviceRv;

    @BindView(R.id.add_port_tv)
    TextView addPortTv;

    @BindView(R.id.timepicker)
    LinearLayout timepicker;

    @BindView(R.id.port_type_wv_ll)
    LinearLayout portTypeWvLl;
    @BindView(R.id.wv_cancel_tv)
    TextView wvCancelTv;
    @BindView(R.id.wv_confirm_tv)
    TextView wvConfirmTv;

    private boolean equipmentStatus;//当前时候为添加设备的状态

    private String devId;
    private EquipmentPort mEquipmentPort;
    private DeviceAdapter deviceAdapter;
    private RemindDialog mRemindDialog;
    private List<EquipmentPort> equipmentPorts;
    private List<EquipmentPortType> equipmentPortTypeList = new ArrayList<EquipmentPortType>();
    private Equipment equipment;

    private DataPickView dataPickView;
    private boolean[] mPickTypes = null;
    private String[] mPickTexts = null;
    private List<List<String>> mPickLists = null;
    private List<String> protTypes = new ArrayList<String>();

    @Override
    public AddFitnessPresenter initPresenter(Context context) {
        return new AddFitnessPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_equipment);


    }

    @Override
    protected void initView() {
        super.initView();
        initBundle();
        deviceAdapter = new DeviceAdapter(R.layout.adapter_device, equipmentPorts);
        deviceAdapter.setItemDeleteClick(mDeviceItemDeleteClick);
        deviceRv.setLayoutManager(new LinearLayoutManager(this));
        deviceRv.setAdapter(deviceAdapter);
        if(FitnessCenterHelper.instance().isEditModel()){
            devLocationTv.setText(FitnessCenterHelper.instance().getGetFitnessCenter().getFcaddress());
        }else {
            devLocationTv.setText(FitnessCenterHelper.instance().getFitnessCenter().getFcaddress());
        }
        initWheelView();
    }

    private void initWheelView() {
        this.equipmentPortTypeList = FitnessCenterHelper.instance().getEquipmentPortTypeList();
        for (EquipmentPortType portType : equipmentPortTypeList) {
            protTypes.add(portType.getEtname());
        }
        mPickLists = new ArrayList<>();//显示每一个滑动控件的数据
        mPickTypes = new boolean[]{true, false, false, false, false, false};//是否显示异常每一个滑动控件
        mPickTexts = new String[]{"", "", " ", "", "", ""};//每一个滑动控件旁边的描述文字
        mPickLists.add(protTypes);

        if (dataPickView != null || timepicker.getVisibility() == View.GONE)
            return;
        //调用方式2
        dataPickView = new DataPickBuilder(this).setDadaPickInterface(new DataPickInterface() {
            @Override
            public boolean[] getIsVisiables() {
                return mPickTypes;
            }

            @Override
            public String[] getVisiableText() {
                return mPickTexts;
            }

            @Override
            public List<List<String>> getAdapter() {
                return mPickLists;
            }

            @Override
            public int[] getSelectIndexs() {
                int[] indexs = new int[6];

                return indexs;
            }
        });
    }

    private void initBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            equipmentStatus = bundle.getBoolean(BUNDLE_KEY_EQUIPMENT_STATUS);
        } else {
            return;
        }
        mTitleBar.setTitle(equipmentStatus ? R.string.dev_result : R.string.dev_details);//添加设备
        mTitleBar.showRightText();
        mTitleBar.setRightText(R.string.done);
        mTitleBar.setClickListener(navBarOnClickListener);
        if (equipmentStatus == BUNDLE_VALUE_ADD_EQUIPMENT) {
            devId = bundle.getString(BUNDLE_KEY_DEV_ID);
            devIdTv.setText(devId);
            equipment = new Equipment(devId);
            equipmentPorts = equipment.getEquipmentPorts();
        } else if (equipmentStatus == BUNDLE_VALUE_EDIT_EQUIPMENT) {
            //编辑设备
            equipment = (Equipment) bundle.getSerializable(BUNDLE_KEY_EDIT_EQUIPMENT);
            devId = equipment.getEmac();
            devIdTv.setText(devId);
            equipmentPorts = equipment.getEquipmentPorts();
            dismissAddport();
        }
    }

    @OnClick({R.id.wv_cancel_tv, R.id.wv_confirm_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wv_cancel_tv:
                dismissAddport();
                break;
            case R.id.wv_confirm_tv:
                dismissAddport();
                getPresenter().addPort(dataPickView.getDatas());
                break;
        }
    }

    private void showAddPort() {
        addPortTv.setVisibility(View.GONE);
        portTypeWvLl.setVisibility(View.VISIBLE);
    }

    private void dismissAddport() {
        addPortTv.setVisibility(View.VISIBLE);
        portTypeWvLl.setVisibility(View.GONE);
    }

    public void AddPortSuccess(EquipmentPortType equipmentPortType) {
        EquipmentPort equipmentPort = new EquipmentPort();
        equipmentPort.setEquipmentPortType(equipmentPortType);
        equipmentPort.setEpnumber(getPortNumber());
        equipmentPorts.add(equipmentPort);
        deviceAdapter.setNewData(equipmentPorts);
    }

    //防止重复端口号
    private String getPortNumber() {
        List<String> ports = new ArrayList<String>();
        for (int i = 0; i < equipmentPorts.size(); i++) {
            ports.add(i,equipmentPorts.get(i).getEpnumber());
        }
        String protNum = "";
        for(int i=1;i<=ports.size();i++){
            String s = i+"";
            if(!ports.contains(s)){
                protNum = s;
                return protNum;
            }
        }
        if(TextUtils.isEmpty(protNum)){
            return equipmentPorts.size()+1+"";
        }
        return protNum;
    }

    @OnClick(R.id.add_port_tv)
    protected void onClickAddPort(View view) {
        showAddPort();
    }

    //设备item点击删除
    final DeviceAdapter.OnItemDeleteClick mDeviceItemDeleteClick = new DeviceAdapter.OnItemDeleteClick() {

        @Override
        public void onDeleteClick(EquipmentPort item) {
            deleteDevice(item);
        }
    };

    //删除设备
    private void deleteDevice(EquipmentPort item) {
        mEquipmentPort = item;
        showDeleteDeviceDialog(item);
    }

    //删除设备弹窗
    private void showDeleteDeviceDialog(EquipmentPort item) {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(String.format(getResources().getString(R.string.delete_dev_tips), item.getEquipmentPortType().getEtname()));
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
                    RemindDialog dialog = (RemindDialog) mRemindDialog;
                    if (mEquipmentPort != null) {
                        equipmentPorts.remove(mEquipmentPort);
                        deviceAdapter.notifyDataSetChanged();
                    }
                    mEquipmentPort = null;
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

        }

        @Override
        public void onRightTxtClick(View view) {
            AddDevice();
        }
    };

    //添加完成
    private void AddDevice() {
        if(FitnessCenterHelper.instance().isEditModel()){
            if (equipmentStatus == BUNDLE_VALUE_ADD_EQUIPMENT) {
                getPresenter().addDevice(FitnessCenterHelper.instance().getGetFitnessCenter().getIdFitnessCenter().intValue(), equipment.getEmac(), equipmentPorts);
            }else{
                getPresenter().updateDevice(FitnessCenterHelper.instance().getGetFitnessCenter().getIdFitnessCenter().intValue(), equipment.getEmac(), equipmentPorts);
            }
        }else{
            equipment.setEquipmentPorts(equipmentPorts);
            getPresenter().AddDone(equipment);
        }
    }

    public void showAddLoading() {
        showCustomLoading(getString(R.string.adding));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void showAddEditSuccess() {
        dismissLoading();
        back();
    }

    public void showAddfailure() {
        toast(getString(R.string.add_failure));
    }
    public void showAddEditfailure(String errMsg) {
        toast(errMsg);
    }
}

