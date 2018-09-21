package com.saiyi.gymequipment.equipment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class NormalItemHolder extends RecyclerView.ViewHolder{
    public TextView tv_type;
    public TextView tv_rundate;
    public TextView tv_runtime;
    public TextView tv_runfire_count;
    public TextView tv_frequency;
    public TextView tv_number;

    private Context context;

    public NormalItemHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        tv_type = (TextView) itemView.findViewById(R.id.tv_type);
        tv_rundate = (TextView) itemView.findViewById(R.id.tv_rundate);
        tv_runtime = (TextView) itemView.findViewById(R.id.tv_runtime);
        tv_runfire_count = (TextView) itemView.findViewById(R.id.tv_runfire_count);
        tv_frequency = (TextView)itemView.findViewById(R.id.tv_frequency);
        tv_number = (TextView)itemView.findViewById(R.id.tv_number);
    }
}
