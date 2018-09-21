package com.saiyi.gymequipment.home.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.ble.BleDataHandler;
import com.saiyi.gymequipment.common.constans.BleConstans;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.home.presenter.BodybuildingPresenter;
import com.saiyi.gymequipment.me.ui.FitnessGuidanceActivity;
import com.saiyi.libble.BluetoothHelper;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.utils.HexDump;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JingNing on 2018-07-10 10:28
 */
public class BodybuildingActivity extends BKMVPActivity<BodybuildingPresenter> implements BluetoothHelper.BleConnectStateListener,
        BluetoothHelper.BleNotifyListener, BleDataHandler.AnalyseDataListener, BluetoothHelper.SystemBleOpenListener, BluetoothHelper.ConnectBleListener {

    private static final int NOTITY_END = 1000;
    private static final int NOTITY_ERR = 1001;
    private static final int NOTITY_DATA = 1002;
    private static final int NOTITY_RESULT = 1003;

    @BindView(R.id.motion_time_tv)
    TextView motionTimeTv;
    @BindView(R.id.burn_calories_tv)
    TextView burnCaloriesTv;
    @BindView(R.id.bt_start_sport)
    Button btStartSport;
    @BindView(R.id.tv_guide)
    TextView tvGuide;
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.number_tv)
    TextView numberTv;
    @BindView(R.id.frequency_tv)
    TextView frequencyTv;

    private String mac;
    private int port = -1;
    private String pType = "";
    private int time = 0;
    private int calorie = 0;
    private int frec = 0;
    private int nums = 0;
    private boolean isStart = false;
    private int reconnNum = 0;
    private BleDataHandler bleDataHandler;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOTITY_END:
                    done();
                    break;
                case NOTITY_ERR:
//                    Toast.makeText(BodybuildingActivity.this, "运动数据错误", Toast.LENGTH_SHORT).show();
                    break;
                case NOTITY_DATA:
                    motionTimeTv.setText(StringUtils.secToTime(time));
                    if (calorie >= 1000) {
                        burnCaloriesTv.setText(Math.round(calorie / 1000) + getString(R.string.motion_result_kilocalorie));
                    } else {
                        burnCaloriesTv.setText(calorie + getString(R.string.motion_result_calories));
                    }
                    numberTv.setText(nums + "");
                    frequencyTv.setText(frec + getString(R.string.motion_result_frec));
                    break;
                case NOTITY_RESULT:
                    motionTimeTv.setText(StringUtils.secToTime(time));
                    if (calorie >= 1000) {
                        burnCaloriesTv.setText(Math.round(calorie / 1000) + getString(R.string.motion_result_kilocalorie));
                    } else {
                        burnCaloriesTv.setText(calorie + getString(R.string.motion_result_calories));
                    }
                    numberTv.setText(nums + "");
                    frequencyTv.setText(frec + getString(R.string.motion_result_frec));
                    done();
                    break;
            }
        }
    };

    @Override
    public BodybuildingPresenter initPresenter(Context context) {
        return new BodybuildingPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_build);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.body_build);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mac = bundle.getString(ScanByQRActivity.BUNDLE_KEY_SCAN_RESULT);
        } else {
            showError();
        }
        bleDataHandler = new BleDataHandler();
        bleDataHandler.setAnalyseDataListener(this);
        BluetoothHelper.getInstance(this).setBleConnectStateListener(this); //连接变化回调
        BluetoothHelper.getInstance(this).setSystemBleOpenListener(this);   //系统蓝牙打开 关闭监听
        BluetoothHelper.getInstance(this).setBleNotifyListener(this);       //通知回调
        BluetoothHelper.getInstance(this).setConnectBleListener(this);      //连接回调
        BluetoothHelper.getInstance(this).setBleConnectStatusListener(mac);
        startListenNotify();
    }


    public void showError() {
        toast(R.string.qr_code_error);
    }

    @Override
    public void connectChangeResult(String mac, int status) {
        if (mac.equals(this.mac)) {
            switch (status) {
                case Constants.STATUS_CONNECTED:
                    reconnNum = 0;
                    startListenNotify();
                    Logger.d("jingning 连接成功。。。");
                    break;
                case Constants.STATUS_DISCONNECTED:
                    reconnect();
                    break;
            }
        }
    }

    private void reconnect() {
        if (reconnNum < 2) {
            reconnNum++;
            BluetoothHelper.getInstance(this).connectBle(mac);
            Logger.d("jingning 蓝牙正在重连...");
        } else {
            done();
            Logger.d("jingning 蓝牙重连超时...");
        }
    }

    @Override
    public void bleOpenResult(boolean openOrClosed) {
    }

    @Override
    public void notifyResult(byte[] value) {
        bleDataHandler.analyseData(value);
    }

    @Override
    public void notifyError(int code) {

    }

    public void startListenNotify() {
        isStart = true;
        btStartSport.setBackgroundResource(R.drawable.end);
        BluetoothHelper.getInstance(this).sendNotify(mac, BleConstans.UUID_SERVICE, BleConstans.UUID_NOTIFY);
    }

    @OnClick({R.id.bt_start_sport, R.id.tv_guide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_start_sport:
                if (!isStart) {
                } else {
                    done();
                }
                break;
            case R.id.tv_guide:
                Bundle bundle = new Bundle();
                bundle.putInt(FitnessGuidanceActivity.BUNDLE_KEY_TYPE, FitnessGuidanceActivity.BUNDLE_VALUE_MOTION);
                bundle.putInt(FitnessGuidanceActivity.BUNDLE_KEY_EQID, port);
                openActivity(FitnessGuidanceActivity.class, bundle);
                break;
        }
    }

    private void done() {
        Bundle bundle = new Bundle();
        bundle.putString(MotionResultActivity.BUNDEL_KEY_PORT_TYPE, pType);
        bundle.putString(MotionResultActivity.BUNDEL_KEY_MAC, mac);
        bundle.putString(MotionResultActivity.BUNDEL_KEY_PORT, port + "");
        bundle.putInt(MotionResultActivity.BUNDEL_KEY_TIME, time);
        bundle.putInt(MotionResultActivity.BUNDEL_KEY_CALORIE, calorie);
        bundle.putInt(MotionResultActivity.BUNDEL_KEY_NUMS, nums);
        bundle.putInt(MotionResultActivity.BUNDEL_KEY_FREC, frec);
        openActivity(MotionResultActivity.class, bundle);
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothHelper.getInstance(this).disconnect(mac);
        bleDataHandler.setAnalyseDataListener(null);
        bleDataHandler = null;
        BluetoothHelper.getInstance(this).setSearchResultListener(null);    //扫描回调
        BluetoothHelper.getInstance(this).setConnectBleListener(null);
        BluetoothHelper.getInstance(this).setSystemBleOpenListener(null);
        BluetoothHelper.getInstance(this).setBleConnectStateListener(null); //连接变化回调
        BluetoothHelper.getInstance(this).setBleNotifyListener(null);       //通知回调
    }

    @Override
    public void notifyMotionData(int port, int nums, int time, int calorie, int frec) {
        if (this.port == -1) {
            this.port = port;
            getPresenter().getEquipmentType(mac, port + "");
        }
        this.time = time;
        this.calorie = calorie;
        this.frec = frec;
        this.nums = nums;
        handler.sendEmptyMessage(NOTITY_DATA);
    }

    @Override
    public void notifyResult(int port, int nums, int time, int calorie, int frec) {
        if (this.port == -1) {
            this.port = port;
            getPresenter().getEquipmentType(mac, port + "");
        }
        this.time = time;
        this.calorie = calorie;
        this.frec = frec;
        this.nums = nums;
        handler.sendEmptyMessage(NOTITY_RESULT);
    }

    @Override
    public void notifyEnd() {
        handler.sendEmptyMessage(NOTITY_END);
    }

    @Override
    public void notifyDataErr() {
        handler.sendEmptyMessage(NOTITY_ERR);
    }

    public void getEquipmentTypeSuccess(EquipmentPortType portType) {
        pType = portType.getEtname();
        tvDevice.setText(pType + getString(R.string.monthlyexercise_setting_text_training));
    }

    @Override
    public void connectResult(int code, BleGattProfile data, String mac) {
        switch (code) {
            case Code.REQUEST_SUCCESS:
                break;
            case Code.REQUEST_FAILED:
                break;
        }
    }
}
