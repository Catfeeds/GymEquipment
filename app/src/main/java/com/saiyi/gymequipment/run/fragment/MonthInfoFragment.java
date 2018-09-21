package com.saiyi.gymequipment.run.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.InfoTextView;
import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.run.adapter.RunHistoryAdapter;
import com.saiyi.gymequipment.run.bean.RunHistoryBean;
import com.saiyi.gymequipment.run.presenter.MonthInfoPresenter;
import com.saiyi.libfast.utils.Arith;
import com.saiyi.libfast.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MonthInfoFragment extends BKMVPFragment<MonthInfoPresenter> {

    @BindView(R.id.tv_today_distance)
    TextView tv_today_distance;

    @BindView(R.id.tv_distance)
    TextView tv_distance;

    @BindView(R.id.it_times)
    InfoTextView it_times;

    @BindView(R.id.it_steps)
    InfoTextView it_steps;

    @BindView(R.id.it_time)
    InfoTextView it_time;

    @BindView(R.id.it_consume)
    InfoTextView it_consume;

    @BindView(R.id.it_speed)
    InfoTextView it_speed;

    @BindView(R.id.ccv_day_running_info)
    ColumnChartView ccv_day_running_info;

    @BindView(R.id.rlc_day_information)
    RecyclerView rlc_day_information;

    private static final int COLUMNS_NUMBER = 7;   //单页显示柱数
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private boolean hasTiltedLabels = false;  //X坐标轴字体是斜的显示还是直的，true是斜的显示

    private List<RunHistoryBean> runHistoryBeanList;
    private RunHistoryAdapter mRunHistoryAdapter;

    private String[] xValues = null;
    private double[] yValues = null;

    @Override
    public MonthInfoPresenter initPresenter(Context context) {
        return new MonthInfoPresenter(context);
    }

    public MonthInfoFragment() {

    }

    public static MonthInfoFragment newInstance() {
        return new MonthInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_info, container, false);
        runHistoryBeanList = new ArrayList<>();
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        String date = StringUtils.fromDateToyyyyMM(System.currentTimeMillis());
        tv_today_distance.setText(date + "\t" + getString(R.string.run_total_distance));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRunHistoryAdapter = new RunHistoryAdapter(getContext());
        rlc_day_information.setLayoutManager(layoutManager);
        rlc_day_information.setAdapter(mRunHistoryAdapter);

        setInformation();
    }

    @Override
    protected void initData() {
        super.initData();
        String date = DateUtils.formatDateByTime(System.currentTimeMillis());
        getPresenter().getRunTotalInformation(2, date);
        getPresenter().getRunInformation(2, date);
    }

    /**
     * @param mRunInfoBean
     */
    public void totalDayInfo(RunInfoBean mRunInfoBean) {
        if (null == mRunInfoBean) {
            showDefaultValue();
            return;
        }

        if (null != mRunInfoBean) {
            if (mRunInfoBean.getRedistance() == null) {
                showDefaultValue();
            } else {

                double minute = Arith.divide(Integer.valueOf(mRunInfoBean.getReduration()), 60);
                double length = Arith.divide(mRunInfoBean.getRedistance().doubleValue(), 1000);
                double speed = Arith.divide(length, minute);
                it_speed.setInfoText(String.valueOf(speed));
                tv_distance.setText(String.valueOf(mRunInfoBean.getRedistance().doubleValue()));
                it_times.setInfoText(String.valueOf(mRunInfoBean.getTimes().intValue()));
                it_steps.setInfoText(String.valueOf(mRunInfoBean.getRestepNumber().intValue()));
                it_time.setInfoText(StringUtils.secToTime(Integer.valueOf(mRunInfoBean.getReduration())));
                it_consume.setInfoText(String.valueOf(mRunInfoBean.getReconsume().doubleValue()));

            }
        }

    }

    /**
     * 服务器信息获取失败
     *
     * @param number 1 总信息获取失败  2 详细信息获取失败
     */
    public void infoGetFail(int number) {
        if (number == 1) {
            showDefaultValue();
        }
        toast(getString(R.string.no_get_data));
    }


    /**
     * 信息获取失败  显示默认的值
     */
    private void showDefaultValue() {
        tv_distance.setText("0");
        it_times.setInfoText("0");
        it_steps.setInfoText("0");
        it_time.setInfoText("00:00:00");
        it_consume.setInfoText("0");
        it_speed.setInfoText("0");
    }

    /**
     * 设置运动数据的单位和状态
     */
    private void setInformation() {
        it_times.setCompanyText(getString(R.string.times)).setStateText(getString(R.string.running_history_text_ok));
        it_steps.setCompanyText(getString(R.string.step)).setStateText(getString(R.string.step_number));
        it_time.setCompanyVisibility(View.GONE).setStateText(getString(R.string.time_length));
        it_consume.setCompanyText(getString(R.string.motion_result_calories)).setStateText(getString(R.string.running_history_text_expend));
        it_speed.setCompanyText(getString(R.string.km_m)).setStateText(getString(R.string.speed));
    }

    public void getRunDetailedInfo(List<RunHistoryBean> beanList) {
        if (null != beanList && beanList.size() > 0) {
            runHistoryBeanList.clear();
            runHistoryBeanList.addAll(beanList);
            mRunHistoryAdapter.setData(runHistoryBeanList);

        }
        initColumnChart();
    }

    private void initColumnChart() {
        mAxisXValues.clear();
        initXYValues();
        List<Column> columns = new ArrayList<>();
        //子列数据集合
        List<SubcolumnValue> values;
        for (int i = 0; i < xValues.length; i++) {
            values = new ArrayList<>();
            Log.e("sdfsd", "Y值：" + yValues[i]);
            values.add(new SubcolumnValue((float) yValues[i], yValues[i] == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#00E2A5")));
            Column column = new Column(values);
            columns.add(column);
            mAxisXValues.add(new AxisValue(i).setLabel(xValues[i]));
        }
        //数据小于一个页面指定数，补空
        if (xValues.length < COLUMNS_NUMBER) {
            for (int i = 0; i < COLUMNS_NUMBER - runHistoryBeanList.size(); i++) {
                values = new ArrayList<SubcolumnValue>();
                values.add(new SubcolumnValue(0, Color.parseColor("#ffffff")));
                Column column = new Column(values);
                columns.add(column);
                mAxisXValues.add(new AxisValue(i).setLabel(""));
            }
        }
        ColumnChartData data = new ColumnChartData();
        data.setColumns(columns);

        //坐标轴X
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(hasTiltedLabels);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#999999"));  //设置字体颜色
        axisX.setMaxLabelChars(0);
        axisX.setLineColor(Color.parseColor("#F2F2F2"));
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);//x 轴在底部


        //设置行为属性，支持缩放、滑动以及平移
        ccv_day_running_info.setInteractive(true);
        ccv_day_running_info.setZoomEnabled(false);
        ccv_day_running_info.setZoomType(ZoomType.HORIZONTAL);
        ccv_day_running_info.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        ccv_day_running_info.setColumnChartData(data);
        ccv_day_running_info.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(ccv_day_running_info.getMaximumViewport());
        v.right = runHistoryBeanList.size() - (runHistoryBeanList.size() - COLUMNS_NUMBER);
        ccv_day_running_info.setCurrentViewport(v);
    }

    private void initXYValues() {
        List<String> dates = StringUtils.getDayListOfMonth(System.currentTimeMillis());
        xValues = new String[dates.size()];
        yValues = new double[dates.size()];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < yValues.length; i++) {
            xValues[i] = dates.get(i).substring(5, dates.get(i).length()).replace("-", "/");
            if (runHistoryBeanList.size() == 0) {
                yValues[i] += 0;
            } else {
                for (RunHistoryBean rb : runHistoryBeanList) {
                    if (dates.get(i).equals(dateFormat.format(new Date(rb.getRecreatetime().longValue())))) {
                        yValues[i] += rb.getRedistance().doubleValue();
                        Log.e("sdfsd", "获取到的距离：" + rb.getRedistance().doubleValue());
                    }
                }
            }
        }
    }

}
