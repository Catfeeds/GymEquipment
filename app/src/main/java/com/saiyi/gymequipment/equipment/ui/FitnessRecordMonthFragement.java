package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.equipment.adapter.FitnessHistoryAdapter;
import com.saiyi.gymequipment.equipment.model.bean.FitnessRecord;
import com.saiyi.gymequipment.equipment.presenter.FitnessRecordMonthPresenter;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.libfast.logger.Logger;

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

/**
 * Created on 2018/5/2.
 */

public class FitnessRecordMonthFragement extends BKMVPFragment<FitnessRecordMonthPresenter> {


    private static final int COLUMNS_NUMBER = 7;   //单页显示柱数

    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_show_timetype)
    TextView tvShowTimetype;
    @BindView(R.id.tv_times)
    TextView tvTimes;
    @BindView(R.id.tv_consume)
    TextView tvConsume;
    @BindView(R.id.column_chart)
    ColumnChartView columnChart;
    @BindView(R.id.record_rv)
    RecyclerView recordRv;

    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private boolean hasTiltedLabels = false;  //X坐标轴字体是斜的显示还是直的，true是斜的显示

    private FitnessData mFitnessData;
    private List<FitnessRecord> mFitnessRecords = new ArrayList<>();
    private FitnessHistoryAdapter adapter;
    private String[] xValues = null;
    private double[] yValues = null;

    @Override
    public FitnessRecordMonthPresenter initPresenter(Context context) {
        return new FitnessRecordMonthPresenter(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_running_history, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        String date = StringUtils.fromDateToyyyyMM(System.currentTimeMillis());
        tvTime.setText(date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new FitnessHistoryAdapter(getContext(), mFitnessRecords);
        recordRv.setLayoutManager(layoutManager);
        recordRv.setAdapter(adapter);
        if (mFitnessRecords != null && mFitnessRecords.size() > 0) {
            initColumnChart();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getFitnessRecordsMonth();
        getPresenter().getTimesTotalRecordMonth();
    }

    private void initColumnChart() {
        mAxisXValues.clear();
        initXYValues();
        List<Column> columns = new ArrayList<Column>();
        //子列数据集合
        List<SubcolumnValue> values;
        for (int i = 0; i < xValues.length; i++) {
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue((float) yValues[i], yValues[i] == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#00E2A5")));
            Column column = new Column(values);
            columns.add(column);
            mAxisXValues.add(new AxisValue(i).setLabel(xValues[i]));
        }
        //数据小于一个页面指定数，补空
        if (xValues.length < COLUMNS_NUMBER) {
            for (int i = 0; i < COLUMNS_NUMBER - xValues.length; i++) {
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
        columnChart.setInteractive(true);
        columnChart.setZoomEnabled(false);
        columnChart.setZoomType(ZoomType.HORIZONTAL);
        columnChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        columnChart.setColumnChartData(data);
        columnChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(columnChart.getMaximumViewport());
        v.right = mFitnessRecords.size() - (mFitnessRecords.size() - COLUMNS_NUMBER);
        columnChart.setCurrentViewport(v);
    }

    private void initXYValues() {
        List<String> dates = StringUtils.getDayListOfMonth(System.currentTimeMillis());
        xValues = new String[dates.size()];
        yValues = new double[dates.size()];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < yValues.length; i++) {
            xValues[i] = dates.get(i).substring(5, dates.get(i).length()).replace("-", "/");
            for (FitnessRecord record : mFitnessRecords) {
                if (dates.get(i).equals(dateFormat.format(new Date(record.getFrcreatetime())))) {
                    yValues[i] += record.getFrduration().doubleValue();
                }
            }
        }
    }

    private void setDataToV() {
        if (mFitnessData != null) {
            tvDuration.setText(StringUtils.secToHour(mFitnessData.getDuration().doubleValue()) + "");
            tvTimes.setText(mFitnessData.getTimes() + "");
            tvConsume.setText(mFitnessData.getConsume() + "");
        }
    }

    private void showDataError() {
        toast(getString(R.string.no_get_data));
    }

    public void setmFitnessData(FitnessData mFitnessData) {
        this.mFitnessData = mFitnessData;
        if (mFitnessData != null) {
            setDataToV();
        } else {
            showDataError();
        }
    }

    public void setmFitnessRecords(List<FitnessRecord> mFitnessRecords) {
        this.mFitnessRecords = mFitnessRecords;
        Logger.d("jingning mFitnessRecords = %s", mFitnessRecords.size());
        adapter.update(mFitnessRecords);
        initColumnChart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvShowTimetype.setText(R.string.running_history_text_hour);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
