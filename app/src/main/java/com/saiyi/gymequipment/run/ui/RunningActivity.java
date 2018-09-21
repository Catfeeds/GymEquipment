package com.saiyi.gymequipment.run.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.run.bean.BroadcastPacketsBean;
import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.event.StepEvent;
import com.saiyi.gymequipment.run.presenter.RunningPresenter;
import com.saiyi.gymequipment.run.service.SensorService;
import com.saiyi.libble.BleAddressBean;
import com.saiyi.libble.BluetoothHelper;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.utils.Arith;
import com.saiyi.libfast.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 跑步页面
 */
public class RunningActivity extends BKMVPActivity<RunningPresenter> implements BluetoothHelper.SystemBleOpenListener,
        BluetoothHelper.SearchResultListener {

    @BindView(R.id.tv_footpath_name)
    TextView tv_footpath_name;

    @BindView(R.id.tv_distance_to_footpath)
    TextView tv_distance_to_footpath;

    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.tv_run_distance)
    TextView tv_run_distance;

    @BindView(R.id.tv_step_number)
    TextView tv_step_number;

    @BindView(R.id.ct_timer)
    Chronometer ct_timer;

    @BindView(R.id.tv_consume)
    TextView tv_consume;

    @BindView(R.id.tv_sport_speed)
    TextView tv_sport_speed;

    @BindView(R.id.bt_start_sport)
    Button bt_start_sport;

    @BindView(R.id.tv_sos)
    TextView tv_sos;

    private int number = 0;

    private AMap aMap;//地图对象
    //    private LocationBean mLocationBean;
    private FootpathBean mFootpathBean;
    private boolean isBleOpen = false;
    private BroadcastPacketsBean oldBroadcastPacketsBean;//上次经过的广播包
    private BroadcastPacketsBean newBroadcastPacketsBean;//当前扫描到的广播包
    private int oldNumber = 0;
    private int newNumber = 0;
    private double runLength = 0;//跑步长度
    private int stepNumber;//步数
    private double speed;//速度
    private double energy;//能量消耗
    private long runningTime;//跑步时间
    private boolean isFirstScannce = true;//是否第一次扫描蓝牙
    private boolean isStarted = false;
    private final double KEY = 1.036;
    private Handler mHandler;

    private SensorService mSensorService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSensorService = ((SensorService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runing);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        BluetoothHelper.getInstance(this).setSystemBleOpenListener(this);

        Intent intent = new Intent(this, SensorService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        registerEventBus();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        isBleOpen = BluetoothHelper.getInstance(this).isBlueToothOpened();
        getPresenter().openBleShow(isBleOpen);
    }


    @Override
    protected void initView() {
        super.initView();

        mTitleBar.setTitle(R.string.run);
        setLocationInfo();
        if (getIntent() != null) {
            mFootpathBean = (FootpathBean) getIntent().getSerializableExtra(FootpathActivity.FOOTPATH_INFORMATION_FOR_RUNNING_BEGIN);
            if (mFootpathBean.getBroadcastPackets() != null && mFootpathBean.getBroadcastPackets().size() > 0) {
                Collections.sort(mFootpathBean.getBroadcastPackets());
            }
        }
        mHandler = new Handler();

        refreshUi();

        mTitleBar.setClickListener(new NavBar.NavBarOnClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                if (isStarted) {
                    isStartRunning();
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

            }

            @Override
            public void onRightTxtClick(View view) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isStarted) {
                isStartRunning();
            } else {
                back();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 初始化页面
     */
    private void refreshUi() {
        if (mFootpathBean != null) {
            tv_footpath_name.setText(mFootpathBean.getTname());

            tv_distance_to_footpath.setText(String.valueOf(mFootpathBean.getBroadcastPackets().get(0).getDistance()));
            if (mFootpathBean.getBroadcastPackets() != null && mFootpathBean.getBroadcastPackets().size() > 0) {
                for (BroadcastPacketsBean bb : mFootpathBean.getBroadcastPackets()) {
                    LatLng ll = new LatLng(bb.getTbplatitude().doubleValue(), bb.getTbplongitude().doubleValue());
                    addMarker(ll, R.drawable.broadcast_packets, "");
                }
            }
        }
    }

    /**
     * 初始化 地图的显示  在当前定位加上图标
     */
    private void setLocationInfo() {
        //获取地图对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        initMap();

    }

    private void initMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }


    @Override
    public RunningPresenter initPresenter(Context context) {
        return new RunningPresenter(context);
    }


    @OnClick({R.id.bt_start_sport, R.id.tv_sos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_sport:
                if (!isStarted) {//未开始
                    if (!isBleOpen) {
                        getPresenter().openBleShow(isBleOpen);
                    }

                    if (isBleOpen) {
                        showCustomLoading("正在扫描...");
                        BluetoothHelper.getInstance(this).setSearchResultListener(this);
                        BluetoothHelper.getInstance(RunningActivity.this).startSearch();
                    }
                }

                if (isStarted) {//开始了  点击按钮结束此次跑步
                    isStartRunning();
                }
                break;
            case R.id.tv_sos:
                makePhone();
                break;
            default:
                break;
        }

    }

    /**
     * 拨打电话
     */
    private void makePhone() {
        String phone = UserHelper.instance().getUser().getSosPhone();
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(RunningActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            RunningActivity.this.startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.set_sos_phone), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开始跑步了 点击此按钮结束运动
     */
    private void isStartRunning() {
        getPresenter().showCancelSetting(R.color.over_color, getString(R.string.over), "", getString(R.string.sure_to_stop), new BaseDialog.OnDialogClick() {
            @Override
            public void onClick(int whichOne) {
                if (whichOne == RemindDialog.WHICH_COMPLATE) {
                    handler.sendEmptyMessage(0);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                BluetoothHelper.getInstance(RunningActivity.this).stopSearch();
                BluetoothHelper.getInstance(RunningActivity.this).setSearchResultListener(null);
                ct_timer.stop();
                runningTime = SystemClock.elapsedRealtime() - ct_timer.getBase();
                Log.e("run", "结束运动，设置按钮背景");
                setButton(false);
                mSensorService.stop();
                isStarted = false;
                isFirstScannce = true;
                mHandler.removeCallbacks(mRunnable);
                if (runLength == 0) {
                    addDataSuccess(1);
                    Toast.makeText(RunningActivity.this, "当前跑步距离为0，将不做数据上传", Toast.LENGTH_SHORT).show();
                    return;
                }
                getPresenter().addRunningData(energy, Arith.divide(runLength, 1000), (int) Arith.divide(runningTime, 1000), speed, stepNumber, mFootpathBean.getIdTrail());
            }
        }
    };


    /**
     * event bus事件接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StepEvent event) {
        if (event.getStepNubmer() > 0) {
            tv_step_number.setText(String.valueOf(event.getStepNubmer() + getString(R.string.step)));
            stepNumber = event.getStepNubmer();
        }
    }

    /**
     * 跑步数据提交成功
     */
    public void addDataSuccess(int type) {
        energy = 0;
        runLength = 0;
        runningTime = 0;
        speed = 0;
        stepNumber = 0;

        tv_run_distance.setText(String.valueOf(runLength));
        tv_step_number.setText(String.valueOf(stepNumber) + getString(R.string.step));
        tv_sport_speed.setText(String.valueOf(speed) + getString(R.string.km_m));
        tv_consume.setText(energy + getString(R.string.kcal));
        ct_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - ct_timer.getBase()) / 1000 / 60);
        ct_timer.setFormat("0" + String.valueOf(hour) + ":%s");
        if (type == 2) {
            Toast.makeText(this, getString(R.string.data_up_success), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跑步数据提交失败
     */
    public void addDataFail() {
        if (number == 2) {
            Toast.makeText(this, getString(R.string.service_nothing), Toast.LENGTH_SHORT).show();
            number = 0;
            return;
        }
        number++;
        Toast.makeText(this, getString(R.string.add_running_data_fail), Toast.LENGTH_SHORT).show();
        getPresenter().addRunningData(energy, Arith.divide(runLength, 1000), (int) Arith.divide(runningTime, 1000), speed, stepNumber, mFootpathBean.getIdTrail());

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        Log.e("blesearch", "-----------------------------------------onPause");
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("blesearch", "---------------------onStop");
//        if (isStarted) {
//            mHandler.postDelayed(mRunnable, 5000);
//            BluetoothHelper.getInstance(this).startSearch();
//        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("blesearch", "---------------------onDestroy");
        mapView.onDestroy();
//        mHandler.removeCallbacks(mRunnable);
        BluetoothHelper.getInstance(this).stopSearch();
        unbindService(conn);
    }


    /**
     * 添加起点终点Marker
     *
     * @param ll   起点终点 经纬度
     * @param icon 图标
     */
    public final void addMarker(LatLng ll, int icon, String info) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.title(info);

        markerOption.perspective(true);
        markerOption.zIndex(5);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), icon)));
        Marker marker = aMap.addMarker(markerOption);
    }

    @Override
    public void bleOpenResult(boolean openOrClosed) {
        isBleOpen = openOrClosed;
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            Log.e("blesearch", "==================重置扫描");
            BluetoothHelper.getInstance(RunningActivity.this).startSearch();
            mHandler.postDelayed(mRunnable, 5000);
        }
    };
    private Timer timer;

    private void setTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BluetoothHelper.getInstance(RunningActivity.this).startSearch();
            }
        }, 0, 3000);
    }


    @Override
    public void searchResult(BleAddressBean bean) {

        if (bean.getBleName().contains("JSQ")) {
            dismissProgressDialog();

            search(bean);
        }
    }

    @Override
    public void searchStopped() {
        Log.e("run", "------------------------------------停止扫描");
        dismissProgressDialog();
        isFirstTime();
        if (isStarted) {
            BluetoothHelper.getInstance(this).startSearch();
        }
    }

    @Override
    public void searchCanceled() {
        Log.e("run", "------------------------------------取消扫描");
        dismissProgressDialog();

    }

    /**
     * 扫描广播包的结果处理
     */
    private void search(BleAddressBean bean) {
        if (null != bean) {
            for (int i = 0; i < mFootpathBean.getBroadcastPackets().size(); i++) {
                String sMac = StringUtils.getStringByMac(bean.getBleMac());
                String bMac = StringUtils.getStringByMac(mFootpathBean.getBroadcastPackets().get(i).getTbpmac());
                if (sMac.equals(bMac)) {//如果扫描到的mac匹配到广播包mac
                    if (isFirstScannce) {//如果是第一次扫描
                        newBroadcastPacketsBean = mFootpathBean.getBroadcastPackets().get(i);
                        newNumber = i;
                        isFirstScannce = false;
                        Log.e("run", "第一次扫描开始运动");
                        startRunning();
                        return;
                    }

                    if (!isFirstScannce) {//如果不是第一次扫描
                        if (newBroadcastPacketsBean == null) {//不是第一次扫描  但是第一次扫描没有扫描到广播包
                            newBroadcastPacketsBean = mFootpathBean.getBroadcastPackets().get(i);
                            newNumber = i;
                            return;
                        }

                        if (newBroadcastPacketsBean != null) {//不是第一次扫描  但是第一次扫描到了广波包
                            oldBroadcastPacketsBean = newBroadcastPacketsBean;
                            oldNumber = newNumber;
                            newBroadcastPacketsBean = mFootpathBean.getBroadcastPackets().get(i);
                            newNumber = i;
                            if (oldBroadcastPacketsBean.getTbpmac().equals(newBroadcastPacketsBean.getTbpmac())) {//假如位置没有改变 扫描到同一个广播包 则不做处理
                                return;
                            }

                            if (!oldBroadcastPacketsBean.getTbpmac().equals(newBroadcastPacketsBean.getTbpmac())) {//假如位置改变 扫描到另外一个广播包
                                if ((newNumber == mFootpathBean.getBroadcastPackets().size() - 1 && oldNumber == 0) || (newNumber == 0 && oldNumber == mFootpathBean.getBroadcastPackets().size() - 1)) {
                                    if (mFootpathBean.getBroadcastPackets().size() == 2) {
                                        if (oldBroadcastPacketsBean.getIdTrailBroadcastPacket().intValue() > newBroadcastPacketsBean.getIdTrailBroadcastPacket().intValue()) {
                                            runLength = Arith.add(runLength, oldBroadcastPacketsBean.getTbpdistance().doubleValue());
                                        } else {
                                            runLength = Arith.add(runLength, newBroadcastPacketsBean.getTbpdistance().doubleValue());
                                        }
                                    } else if (oldNumber == 0 && mFootpathBean.getBroadcastPackets().size() > 2) {
                                        runLength = Arith.add(runLength, oldBroadcastPacketsBean.getTbpdistance().doubleValue());
                                    } else if (newNumber == 0 && mFootpathBean.getBroadcastPackets().size() > 2) {
                                        runLength = Arith.add(runLength, newBroadcastPacketsBean.getTbpdistance().doubleValue());
                                    } else {

                                    }
                                } else {
                                    if (oldBroadcastPacketsBean.getIdTrailBroadcastPacket().intValue() > newBroadcastPacketsBean.getIdTrailBroadcastPacket().intValue()) {
                                        runLength = Arith.add(runLength, oldBroadcastPacketsBean.getTbpdistance().doubleValue());
                                    } else {
                                        runLength = Arith.add(runLength, newBroadcastPacketsBean.getTbpdistance().doubleValue());
                                    }
                                }

                                runningTime = SystemClock.elapsedRealtime() - ct_timer.getBase();
                                setData();
                            }
                        }
                    }
                    return;
                }

                if (i == mFootpathBean.getBroadcastPackets().size() - 1) {//本次扫描到的广播包不包含在当前步道
                    Toast.makeText(this, getString(R.string.error_footpath), Toast.LENGTH_SHORT).show();
                    back();
                }

            }
        }
    }

    /**
     * 运动数据刷新页面
     */
    private void setData() {
        tv_run_distance.setText(String.valueOf(runLength));
        stepNumber = (int) Arith.divide(runLength, 0.7);
//        tv_step_number.setText(String.valueOf(stepNumber));
        double runTime = Arith.divide(runningTime, 1000 * 60);
        double length = Arith.divide(runLength, 1000);
        if (runTime > 0) {
            speed = Arith.divide(length, runTime);
        }

        tv_sport_speed.setText(speed + getString(R.string.km_m));

        if (UserHelper.instance().getUser().getWeight() > 0) {
            double a = Arith.multiply(UserHelper.instance().getUser().getWeight(), length);
            energy = Math.ceil(Arith.multiply(a, KEY));
            tv_consume.setText(energy + getString(R.string.kcal));
        } else {
            double a = Arith.multiply(60, length);
            energy = Math.ceil(Arith.multiply(a, KEY));
            tv_consume.setText(energy + getString(R.string.kcal));
        }
    }

    /**
     * 第一次扫描蓝牙设备
     */
    private void isFirstTime() {
        if (isFirstScannce) {
            getPresenter().showCancelSetting(R.color.colorAccent, getString(R.string.confirm), getString(R.string.run_remind), getString(R.string.run_remind_no_broadcast), new BaseDialog.OnDialogClick() {
                @Override
                public void onClick(int whichOne) {
                    if (whichOne == RemindDialog.WHICH_COMPLATE) {
                        startRunning();
                        isFirstScannce = false;
                    }
                }
            });
        }
    }

    /**
     * 刷新页面 开始跑步
     */
    private void startRunning() {
        Log.e("run", "开始运动，设置按钮背景");
        setButton(true);
        mSensorService.start();
//        mHandler.postDelayed(mRunnable, 5000);
//        setTimer();
        ct_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - ct_timer.getBase()) / 1000 / 60);
        ct_timer.setFormat("0" + String.valueOf(hour) + ":%s");
        ct_timer.start();
        isStarted = true;
    }

    /**
     * 设置开始按钮的背景
     *
     * @param isStart true 开始运动了 设置背景未结束提示按钮 false结束运动或未开始运动 设置背景未开始运动提示按钮
     */
    private void setButton(boolean isStart) {
        if (isStart) {//开始运动
            bt_start_sport.setBackgroundResource(R.drawable.end);
            tv_sos.setVisibility(View.VISIBLE);
        }

        if (!isStart) {//结束运动或者未开始运动
            bt_start_sport.setBackgroundResource(R.drawable.start);
            tv_sos.setVisibility(View.GONE);
        }
    }
}
