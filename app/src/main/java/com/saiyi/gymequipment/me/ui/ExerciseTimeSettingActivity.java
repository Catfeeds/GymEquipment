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
import com.saiyi.gymequipment.common.view.wheelview.DataPickBuilder;
import com.saiyi.gymequipment.common.view.wheelview.DataPickInterface;
import com.saiyi.gymequipment.common.view.wheelview.DataPickView;
import com.saiyi.gymequipment.me.model.bean.ExerciseVolume;
import com.saiyi.gymequipment.me.presenter.ExerciseTimeSettingPresenter;
import com.saiyi.libfast.activity.BaseActivity;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2018/4/27.
 */

public class ExerciseTimeSettingActivity extends BKMVPActivity<ExerciseTimeSettingPresenter> {

    @BindView(R.id.tv_textleft)
    TextView tvTextleft;
    @BindView(R.id.tv_timeset)
    TextView tvTimeset;
    private DataPickView dataPickView;
    private boolean[] mPickTypes = null;
    private String[] mPickTexts = null;
    private List<List<String>> mPickLists = null;

    private int mShowType;
    private ExerciseVolume mExerciseVolume;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_time_setting);
    }

    @Override
    public ExerciseTimeSettingPresenter initPresenter(Context context) {
        return new ExerciseTimeSettingPresenter(context);
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mShowType = bundle.getInt(Constant.INTENT_SHOW_TYPE, 0);
            mExerciseVolume = (ExerciseVolume) bundle.getSerializable(Constant.INTENT_SHOW_DATA);
        }
        mTitleBar.showRightText();
        mTitleBar.setRightText(R.string.confirm);
        if (mExerciseVolume != null) {
            if (mShowType == Constant.RUN_TYPE) {
                mTitleBar.setTitle(R.string.run_setting);
            } else {
                mTitleBar.setTitle(R.string.training_settings);
            }
            showPickDatas();
        }
    }

    @Override
    protected void initListener() {
        mTitleBar.setClickListener(new NavBar.NavBarOnClickListener() {
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
                saveData();
            }
        });
    }

    private void saveData() {
        if (mShowType == Constant.TRAINING_TYPE) {
            String[] datas = dataPickView.getDatasArray();
            if (datas.length == 3) {
                mExerciseVolume.setHtfitness(Integer.parseInt(datas[0]) * 3600 + Integer.parseInt(datas[1]) * 60 + Integer.parseInt(datas[2]));
            }
        } else {
            mExerciseVolume.setHtrun(Integer.parseInt(dataPickView.getDatas()));
        }
        getPresenter().updateExerciseVolume(mExerciseVolume.getHtfitness().intValue(), (int) mExerciseVolume.getHtrun().intValue());
    }

    public void showSetting() {
        showCustomLoading(getString(R.string.set_up));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void updateExerciseVolumeSuccess() {
        dismissLoading();
        Intent intent = new Intent();
        intent.putExtra(Constant.INTENT_SHOW_DATA, mExerciseVolume);
        setResult(100, intent);
        back();
    }

    public void updateExerciseVolumeFailed(String msg) {
        dismissLoading();
        toast(msg);
    }

    @Override
    protected void initData() {

    }

    public void showPickDatas() {
        mPickLists = new ArrayList<>();//显示每一个滑动控件的数据
        switch (mShowType) {
            case Constant.RUN_TYPE:
                tvTextleft.setText(R.string.run);
                tvTimeset.setText(mExerciseVolume.getHtrun().intValue() + getString(R.string.monthlyexercise_setting_text_gongli));
                mPickTypes = new boolean[]{true, true, true, false, false, false};//是否显示异常每一个滑动控件
                mPickTexts = new String[]{"", "", getString(R.string.monthlyexercise_setting_text_gongli), "", "", ""};//每一个滑动控件旁边的描述文字
                mPickLists.add(getSimpleListData(0, 9));
                mPickLists.add(getSimpleListData(0, 9));
                mPickLists.add(getSimpleListData(0, 9));
                break;
            case Constant.TRAINING_TYPE:
                tvTextleft.setText(R.string.monthlyexercise_setting_text_training);
                tvTimeset.setText(StringUtils.secToHour(mExerciseVolume.getHtfitness().doubleValue()) + getString(R.string.running_history_text_hour));

                mPickTypes = new boolean[]{true, true, true, false, false, false};//是否显示异常每一个滑动控件
                mPickTexts = new String[]{getString(R.string.monthlyexercise_setting_text_h), getString(R.string.monthlyexercise_setting_text_m), getString(R.string.monthlyexercise_setting_text_s), "", "", ""};//每一个滑动控件旁边的描述文字
                mPickLists.add(getSimpleListData(0, 240));
                mPickLists.add(getSimpleListData(0, 59));
                mPickLists.add(getSimpleListData(0, 59));
                break;
            default:
                break;
        }


        if (dataPickView != null)
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
                int item1, item2, item3;
                if (mShowType == Constant.RUN_TYPE) {
                    String mShowData = (int) mExerciseVolume.getHtrun().intValue() + "";
                    if (mShowData.length() == 3) {
                        item1 = mPickLists.get(0).indexOf(mShowData.substring(0, 1));
                        item2 = mPickLists.get(1).indexOf(mShowData.substring(1, 2));
                        item3 = mPickLists.get(2).indexOf(mShowData.substring(2, 3));
                        indexs[0] = item1;
                        indexs[1] = item2;
                        indexs[2] = item3;
                    }
                    if (mShowData.length() == 2) {
                        item2 = mPickLists.get(1).indexOf(mShowData.substring(0, 1));
                        item3 = mPickLists.get(2).indexOf(mShowData.substring(1, 2));
                        indexs[0] = 0;
                        indexs[1] = item2;
                        indexs[2] = item3;

                    }
                    if (mShowData.length() == 1) {
                        item2 = mPickLists.get(2).indexOf(mShowData.substring(0, 1));
                        indexs[0] = 0;
                        indexs[1] = 0;
                        indexs[2] = item2;
                    }
                }

                if (mShowType == Constant.TRAINING_TYPE) {
                    String[] times = StringUtils.secToTime(mExerciseVolume.getHtfitness().intValue()).split(":");
                    if (times.length < 3) return indexs;
                    indexs[0] = Integer.parseInt(times[0]);
                    indexs[1] = Integer.parseInt(times[1]);
                    indexs[2] = Integer.parseInt(times[2]);
                }
                return indexs;
            }
        });
    }

    public List<String> getSimpleListData(int min, int max) {
        List<String> lists = new ArrayList<>();
        for (int i = min; i < max + 1; i++) {
            lists.add("" + i);
        }
        return lists;
    }
}
