package com.saiyi.gymequipment.equipment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.XCRoundImageView;
import com.saiyi.gymequipment.equipment.model.bean.RankingUser;
import com.saiyi.libfast.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created on 2018/5/3.
 */

public class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.MyViewHolder> {

    private List<RankingUser> list;
    private Context mContext;

    public RankingsAdapter(List<RankingUser> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public RankingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_randkings, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RankingUser user = list.get(position);
        holder.tv_number.setText(position + 1 + "");
        holder.tv_user_name.setText(user.getUnickname());
        holder.tv_user_time.setText(StringUtils.secToHour(user.getFrduration().doubleValue())  + "h");
        if (TextUtils.isEmpty(user.getUimg())) {
            holder.user_imageview.setImageResource(R.drawable.leftbar_info);
        } else {
            CropCircleTransformation circle = (CropCircleTransformation) holder.user_imageview.getTag();
            if (circle == null) {
                circle = new CropCircleTransformation();
                holder.user_imageview.setTag(circle);
            }
            Logger.d("user.getUimg()=" + user.getUimg());
            Picasso.with(mContext).load(user.getUimg()).
                    transform(circle).
                    placeholder(R.drawable.leftbar_info).
                    error(R.drawable.leftbar_info).
                    into(holder.user_imageview);
        }
    }

//    public void update(List<RankingUser> list) {
//        Logger.d("update this.list.size()%s, list.size():%s, this.list:%s, list:%s", this.list.size(), list.size(), this.list, list);
//        this.list.clear();
//        this.list.addAll(list);
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_number;
        XCRoundImageView user_imageview;
        TextView tv_user_name;
        TextView tv_user_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_number = itemView.findViewById(R.id.tv_number);
            user_imageview = itemView.findViewById(R.id.user_imageview);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_user_time = itemView.findViewById(R.id.tv_user_time);
        }
    }
}
