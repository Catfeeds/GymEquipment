package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.mvp.PresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2018/4/25.
 */

public class MonthlyExerciseSettingActivity extends BKMVPActivity {

    @BindView(R.id.tv_show_run)
    TextView tvShowRun;
    @BindView(R.id.tv_show_runtime)
    TextView tvShowRuntime;

    private ExerciseVolume mExerciseVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyexercise_setting);
        getTitleBar().setTitle(getString(R.string.monthlyexercise_setting_text_title));
    }

    @Override
    public PresenterImpl initPresenter(Context context) {
        return null;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mExerciseVolume = (ExerciseVolume) bundle.get("data");
        }
        if (mExerciseVolume == null) {
            mExerciseVolume = new ExerciseVolume();
            mExerciseVolume.setHtrun(0);
            mExerciseVolume.setHtfitness(0);
        }
        tvShowRun.setText((int) mExerciseVolume.getHtrun().intValue() + "");
        tvShowRuntime.setText(StringUtils.secToHour(mExerciseVolume.getHtfitness().doubleValue()));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.rl_toset_run, R.id.rl_toset_training})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.INTENT_SHOW_DATA, mExerciseVolume);
        switch (view.getId()) {
            case R.id.rl_toset_run:
                bundle.putInt(Constant.INTENT_SHOW_TYPE, Constant.RUN_TYPE);
                openActivityForResult(ExerciseTimeSettingActivity.class, bundle, 100);
                break;
            case R.id.rl_toset_training:
                bundle.putInt(Constant.INTENT_SHOW_TYPE, Constant.TRAINING_TYPE);
                openActivityForResult(ExerciseTimeSettingActivity.class, bundle, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                mExerciseVolume = (ExerciseVolume) bundle.getSerializable(Constant.INTENT_SHOW_DATA);
                if (mExerciseVolume != null) {
                    tvShowRun.setText((int) mExerciseVolume.getHtrun().intValue() + "");
                    tvShowRuntime.setText(StringUtils.secToHour(mExerciseVolume.getHtfitness().doubleValue()));
                }
            }
        }
    }
}
