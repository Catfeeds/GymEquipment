package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.CircleProgress;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.gymequipment.me.presenter.HealthDataPresenter;
import com.saiyi.libfast.logger.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2018/4/24.
 */

public class HealthDataActivity extends BKMVPActivity<HealthDataPresenter> {
    @BindView(R.id.cp_running)
    CircleProgress cpRunning;
    @BindView(R.id.cp_equipment)
    CircleProgress cpEquipment;
    @BindView(R.id.progesss1)
    ProgressBar progesss1;
    @BindView(R.id.tv_bodyweight)
    TextView tvBodyweight;
    @BindView(R.id.tv_rundata)
    TextView tvRundata;
    @BindView(R.id.rl_gotosetting1)
    RelativeLayout rlGotosetting1;
    @BindView(R.id.tv_runtime_data)
    TextView tvRuntimeData;
    @BindView(R.id.rl_gotosetting2)
    RelativeLayout rlGotosetting2;

    private ExerciseVolume mExerciseVolume;

    @Override
    public HealthDataPresenter initPresenter(Context context) {
        return new HealthDataPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthdata);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.health_data);
        tvRundata.setText(String.format(getString(R.string.run_data), 0 + ""));
        tvRuntimeData.setText(String.format(getString(R.string.run_time_data), StringUtils.secToTime(0)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getExerciseVolume();
    }

    @OnClick({R.id.rl_gotosetting1, R.id.rl_gotosetting2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gotosetting1:
            case R.id.rl_gotosetting2:
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", mExerciseVolume);
                openActivity(MonthlyExerciseSettingActivity.class, bundle);
                break;
        }
    }

    public void showGetDataLoading() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void setBmi(double bmi) {
        progesss1.setProgress((int) bmi);
        String msg;
        if (bmi < 18.5) {
            msg = getString(R.string.thin);
        } else if (bmi >= 18.5 && bmi < 23.9) {
            msg = getString(R.string.normal);
        } else if (bmi >= 23.9 && bmi <= 24) {
            msg = getString(R.string.overweight);
        } else if (bmi > 24 && bmi <= 27.9) {
            msg = getString(R.string.fat);
        } else {
            msg = getString(R.string.obesity);
        }
        tvBodyweight.setText(bmi + " " + msg);
    }

    public void getExerciseVolumeSuccess(ExerciseVolume exerciseVolume) {
        dismissLoading();
        this.mExerciseVolume = exerciseVolume;
        if (exerciseVolume == null) return;
        BigDecimal bg = new BigDecimal(exerciseVolume.getHtbmi().doubleValue()).setScale(1, RoundingMode.UP);
        setBmi(bg.doubleValue());
        cpRunning.setMaxValue(exerciseVolume.getHtrun().floatValue());
        cpRunning.setValue(exerciseVolume.getRunDistance().floatValue());
        tvRundata.setText(String.format(getString(R.string.run_data), new BigDecimal(exerciseVolume.getHtrun().doubleValue()).setScale(2, RoundingMode.UP).doubleValue() + ""));
        cpEquipment.setMaxValue(exerciseVolume.getHtfitness().floatValue());
        cpEquipment.setValue(exerciseVolume.getFitnessTims().floatValue());
        tvRuntimeData.setText(String.format(getString(R.string.run_time_data), StringUtils.secToTime(exerciseVolume.getHtfitness().intValue())));
    }

    public void getExerciseVolumeFailed(String msg) {
        dismissLoading();
        toast(msg);
    }
}
