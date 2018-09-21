package com.saiyi.gymequipment.equipment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class GroupItemHolder extends NormalItemHolder {

    public TextView tv_day;

    public GroupItemHolder(Context context, View itemView) {
        super(context, itemView);
        tv_day = (TextView) itemView.findViewById(R.id.day_tv);
    }
}
