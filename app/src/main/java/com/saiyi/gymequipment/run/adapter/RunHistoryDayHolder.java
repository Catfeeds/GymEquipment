package com.saiyi.gymequipment.run.adapter;

import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class RunHistoryDayHolder extends RunHistoryNormalHolder {

    public TextView tv_day_info;

    public RunHistoryDayHolder(View itemView) {
        super(itemView);
        tv_day_info = itemView.findViewById(R.id.tv_day_info);
    }
}
