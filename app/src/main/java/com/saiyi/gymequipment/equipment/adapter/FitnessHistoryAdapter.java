package com.saiyi.gymequipment.equipment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.equipment.model.bean.FitnessRecord;

import java.util.List;

/**
 * Created on 2018/5/3.
 */

public class FitnessHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FitnessRecord> fitnessRecords;

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;


    public FitnessHistoryAdapter(Context context, List<FitnessRecord> list) {
        this.mContext = context;
        this.fitnessRecords = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fitness_history_itme, parent, false);
            NormalItemHolder holder = new NormalItemHolder(mContext, itemView);
            return holder;
        } else if (viewType == GROUP_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fitness_history_list, parent, false);
            GroupItemHolder holder = new GroupItemHolder(mContext, itemView);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FitnessRecord record = fitnessRecords.get(position);
        if (record == null) return;

        if (holder instanceof GroupItemHolder) {
            bindGroupItem(record, (GroupItemHolder) holder);
        } else {
            NormalItemHolder viewHolder = (NormalItemHolder) holder;
            bindNormalItem(record, viewHolder.tv_type, viewHolder.tv_rundate, viewHolder.tv_runtime, viewHolder.tv_runfire_count, viewHolder.tv_frequency, viewHolder.tv_number);
        }
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

        String currentDate = StringUtils.fromDateToMMdd(fitnessRecords.get(position).getFrcreatetime());
        int prevIndex = position - 1;
        boolean isDifferent = !StringUtils.fromDateToMMdd(fitnessRecords.get(prevIndex).getFrcreatetime()).equals(currentDate);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }

    void bindNormalItem(FitnessRecord record, TextView tv_type, TextView tv_rundate, TextView tv_runtime, TextView tv_runfire_count, TextView tv_frequency, TextView tv_number) {
        tv_type.setText(record.getEtname());
        tv_runtime.setText(StringUtils.secToTime(record.getFrduration().intValue()));
        tv_runfire_count.setText(record.getFrconsume() + "");
        tv_rundate.setText(StringUtils.fromDateToHHmm(record.getFrcreatetime()));
        tv_frequency.setText(record.getFrequency().intValue() + "");
        tv_number.setText(record.getFrtimes().intValue() + "");
    }

    void bindGroupItem(FitnessRecord record, GroupItemHolder holder) {
        bindNormalItem(record, holder.tv_type, holder.tv_rundate, holder.tv_runtime, holder.tv_runfire_count, holder.tv_frequency, holder.tv_number);
        holder.tv_day.setText(StringUtils.fromDateToMMdd(record.getFrcreatetime()));
    }


    public void update(List<FitnessRecord> list) {
        fitnessRecords.clear();
        fitnessRecords.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fitnessRecords.size();
    }

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_day;
//        TextView tv_type;
//        TextView tv_rundate;
//        TextView tv_runtime;
//        TextView tv_runfire_count;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tv_day = (TextView) itemView.findViewById(R.id.day_tv);
//            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
//            tv_rundate = (TextView) itemView.findViewById(R.id.tv_rundate);
//            tv_runtime = (TextView) itemView.findViewById(R.id.tv_runtime);
//            tv_runfire_count = (TextView) itemView.findViewById(R.id.tv_runfire_count);
//        }
//    }
}
