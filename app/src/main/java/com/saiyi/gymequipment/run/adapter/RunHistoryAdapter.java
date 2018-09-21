package com.saiyi.gymequipment.run.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.run.bean.RunHistoryBean;

import java.util.ArrayList;
import java.util.List;

public class RunHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<RunHistoryBean> mDataList;

    public RunHistoryAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setData(List<RunHistoryBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            mDataList.clear();
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_history_normal, parent, false);
            RunHistoryNormalHolder holder = new RunHistoryNormalHolder(itemView);
            return holder;
        } else if (viewType == GROUP_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_history_day, parent, false);
            RunHistoryDayHolder holder = new RunHistoryDayHolder(itemView);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RunHistoryBean rb = mDataList.get(position);
        if (null == rb) {
            return;
        }

        if (viewHolder instanceof RunHistoryDayHolder) {
            RunHistoryDayHolder holder = (RunHistoryDayHolder) viewHolder;
            bindGroupItem(rb, holder);
        } else {
            RunHistoryNormalHolder holder = (RunHistoryNormalHolder) viewHolder;
            bindNormalItem(rb, holder.tv_run_total_info, holder.tv_start_run_time, holder.tv_run_step, holder.tv_running_time, holder.tv_speed_distribution, holder.tv_consume);
        }

    }

    private void bindNormalItem(RunHistoryBean rb, TextView tv_run_total_info,TextView tv_start_run_time,TextView tv_run_step,TextView tv_running_time,TextView tv_speed_distribution,TextView tv_consume) {
        tv_run_total_info.setText(String.format(mContext.getResources().getString(R.string.out_door_running), String.valueOf(rb.getRedistance().doubleValue())));//跑步总距离  如：户外跑步4公里
        tv_start_run_time.setText(StringUtils.fromDateToHHmm(rb.getRecreatetime().longValue()));//开始跑步时间
        tv_run_step.setText(String.valueOf(rb.getRestepNumber().intValue()));//跑步步数
        tv_running_time.setText(StringUtils.secToTime(rb.getReduration().intValue()));//跑步时间
        tv_speed_distribution.setText(StringUtils.getSpeedDistribution(rb.getRedistance().doubleValue(), rb.getReduration().intValue()));//配速
        tv_consume.setText(String.valueOf(rb.getReconsume().doubleValue()));//消耗
    }

    private void bindGroupItem(RunHistoryBean rb, RunHistoryDayHolder holder) {
        bindNormalItem(rb, holder.tv_run_total_info, holder.tv_start_run_time, holder.tv_run_step, holder.tv_running_time, holder.tv_speed_distribution, holder.tv_consume);
        holder.tv_day_info.setText(StringUtils.fromDateToMMdd(rb.getRecreatetime().longValue()));//日期
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0)
            return GROUP_ITEM;

        String currentDate = StringUtils.fromDateToMMdd(mDataList.get(position).getRecreatetime().longValue());
        int prevIndex = position - 1;
        boolean isDifferent = !StringUtils.fromDateToMMdd(mDataList.get(prevIndex).getRecreatetime().longValue()).equals(currentDate);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }
}
