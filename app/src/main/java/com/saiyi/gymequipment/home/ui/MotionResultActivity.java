package com.saiyi.gymequipment.home.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.home.presenter.MotionResultPresenter;
import com.saiyi.libmap.LocationBean;
import com.saiyi.libmap.LocationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MotionResultActivity extends BKMVPActivity<MotionResultPresenter> implements LocationUtil.LocationInfoListener {

    public static final String BUNDEL_KEY_PORT_TYPE = "port_type";
    public static final String BUNDEL_KEY_MAC = "mac";
    public static final String BUNDEL_KEY_PORT = "port";
    public static final String BUNDEL_KEY_TIME = "time";
    public static final String BUNDEL_KEY_CALORIE = "calorie";
    public static final String BUNDEL_KEY_NUMS = "nums";
    public static final String BUNDEL_KEY_FREC = "frec";

    @BindView(R.id.motion_type_tv)
    TextView motionTypeTv;

    @BindView(R.id.motion_time_tv)
    TextView motionTimeTv;

    @BindView(R.id.burn_calories_tv)
    TextView burnCaloriesTv;

    @BindView(R.id.date_time_tv)
    TextView dateTimeTv;

    @BindView(R.id.location_tv)
    TextView locationTv;
    @BindView(R.id.number_tv)
    TextView numberTv;
    @BindView(R.id.frequency_tv)
    TextView frequencyTv;

    private LocationBean mLocationBean;

    private String mac, dPort, recordId;
    private int mTime, mCalorie, nums, frec;
    private int time = 0;

    @Override
    public MotionResultPresenter initPresenter(Context context) {
        return new MotionResultPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_result);
        LocationUtil.getInstance(GymApplication.getContext()).startLoaction();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.motion_result);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString(BUNDEL_KEY_PORT_TYPE);
            mac = bundle.getString(BUNDEL_KEY_MAC);
            dPort = bundle.getString(BUNDEL_KEY_PORT);
            mTime = bundle.getInt(BUNDEL_KEY_TIME);
            mCalorie = bundle.getInt(BUNDEL_KEY_CALORIE);
            nums = bundle.getInt(BUNDEL_KEY_NUMS);
            frec = bundle.getInt(BUNDEL_KEY_FREC);
            motionTypeTv.setText(String.format(getString(R.string.motion_result_type), type));
            motionTimeTv.setText(StringUtils.secToTime(mTime));
            if(mCalorie >= 1000){
                burnCaloriesTv.setText(Math.round(mCalorie/1000) + getString(R.string.motion_result_kilocalorie));
            }else{
                burnCaloriesTv.setText(mCalorie + getString(R.string.motion_result_calories));
            }
            numberTv.setText(nums+"");
            frequencyTv.setText(frec+getString(R.string.motion_result_frec));
            getPresenter().addRecord(mac, dPort, -1, mTime, mCalorie, nums, frec);
        }
        dateTimeTv.setText(StringUtils.fromDateString(System.currentTimeMillis()));
    }


    @Override
    public void location(LocationBean bean) {
        locationTv.setText(bean.getAddress());
    }

    @Override
    public void locationFail(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }
}
