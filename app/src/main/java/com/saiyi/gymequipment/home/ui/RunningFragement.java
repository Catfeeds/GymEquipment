package com.saiyi.gymequipment.home.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.common.view.RoundTextView;
import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.home.presenter.RunPresenter;
import com.saiyi.gymequipment.run.ui.FootpathActivity;
import com.saiyi.gymequipment.run.ui.RankingListActivity;
import com.saiyi.gymequipment.run.ui.RunHistoryActivity;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.utils.DateUtils;
import com.saiyi.libfast.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class RunningFragement extends BKMVPFragment<RunPresenter> {

    @BindView(R.id.title_bar)
    NavBar titleBar;

    private RoundTextView rt_sport_times;//运动次数
    private RoundTextView rv_distance;//运动距离
    private RoundTextView rv_step_number;//运动步数
    private RoundTextView rt_minute;//运动时间
    private RoundTextView rv_consume;//运动消耗
    private RoundTextView rv_speed;//运动速度


    public RunningFragement() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static RunningFragement newInstance() {
        RunningFragement fragment = new RunningFragement();
        return fragment;
    }

    @Override
    public RunPresenter initPresenter(Context context) {
        return new RunPresenter(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_running, container, false);
        rt_sport_times = view.findViewById(R.id.rt_sport_times);
        rv_distance = view.findViewById(R.id.rv_distance);
        rv_step_number = view.findViewById(R.id.rv_step_number);
        rt_minute = view.findViewById(R.id.rt_minute);
        rv_consume = view.findViewById(R.id.rv_consume);
        rv_speed = view.findViewById(R.id.rv_speed);

        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        titleBar.setTitle(R.string.run);
        titleBar.hiddenLeftIcon();
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().getRunInfomation(4, DateUtils.formatDateByTime(System.currentTimeMillis()));
    }

    @OnClick({R.id.tv_footpath, R.id.tv_fitness_history, R.id.tv_rankings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_footpath://步道
                openActivity(FootpathActivity.class);
                break;
            case R.id.tv_fitness_history://跑步历史
                openActivity(RunHistoryActivity.class);
                break;
            case R.id.tv_rankings://排行榜
                openActivity(RankingListActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 获取运动数据
     *
     * @param bean
     */
    public void getRunInfo(RunInfoBean bean) {
        if (bean == null) {
            rt_sport_times.setContentText("0");
            rv_distance.setContentText("0");
            rv_step_number.setContentText("0");
            rt_minute.setContentText("0");
            rv_consume.setContentText("0");
            rv_speed.setContentText("0");
        }

        if (bean != null) {
            if (null == bean.getTimes()) {
                rt_sport_times.setContentText("0");
            } else {
                if (bean.getTimes().intValue() >= 0) {
                    rt_sport_times.setContentText(String.valueOf(bean.getTimes().intValue()));
                }
            }

            if (null == bean.getRedistance()) {
                rv_distance.setContentText("0");
            } else {
                if (bean.getRedistance().intValue() >= 0) {
                    rv_distance.setContentText(String.valueOf(bean.getRedistance().doubleValue()));
                }
            }

            if (null == bean.getRestepNumber()) {
                rv_step_number.setContentText("0");
            } else {
                if (bean.getRestepNumber().intValue() >= 0) {
                    rv_step_number.setContentText(String.valueOf(bean.getRestepNumber().intValue()));
                }
            }

            if (null == bean.getReduration()) {
                rt_minute.setContentText("0");
            } else {
                if (!TextUtils.isEmpty(bean.getReduration())) {
                    rt_minute.setContentText(String.valueOf(DateUtils.minuteFromSecond(Integer.valueOf(bean.getReduration()))));
                }
            }

            if (null == bean.getReconsume()) {
                rv_consume.setContentText("0");
            } else {
                if (bean.getReconsume().intValue() >= 0) {
                    rv_consume.setContentText(String.valueOf(bean.getReconsume().doubleValue()));
                }
            }

            if (null == bean.getRespeed()) {
                rv_speed.setContentText("0");
            } else {
                if (bean.getRespeed().intValue() >= 0) {
                    rv_speed.setContentText(String.valueOf(bean.getRespeed().doubleValue()));
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
