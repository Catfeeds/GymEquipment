package com.saiyi.gymequipment.run.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class RunHistoryNormalHolder extends RecyclerView.ViewHolder{

    public TextView tv_run_total_info;
    public TextView tv_start_run_time;
    public TextView tv_run_step;
    public TextView tv_running_time;
    public TextView tv_speed_distribution;
    public TextView tv_consume;

    public RunHistoryNormalHolder(View itemView) {
        super(itemView);

        tv_run_total_info = (TextView)itemView.findViewById(R.id.tv_run_total_info);
        tv_start_run_time = (TextView)itemView.findViewById(R.id.tv_start_run_time);
        tv_run_step = (TextView)itemView.findViewById(R.id.tv_run_step);
        tv_running_time = (TextView)itemView.findViewById(R.id.tv_running_time);
        tv_speed_distribution = (TextView)itemView.findViewById(R.id.tv_speed_distribution);
        tv_consume = (TextView)itemView.findViewById(R.id.tv_consume);
    }
}
