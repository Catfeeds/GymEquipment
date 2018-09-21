package com.saiyi.gymequipment.run.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.common.view.XCRoundImageView;
import com.saiyi.gymequipment.run.bean.RankingBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {
    private List<RankingBean> rankingBean;
    private Context context;

    public RankAdapter(Context context, List<RankingBean> list) {
        this.rankingBean = list;
        this.context = context;
    }

    public void setNewData(List<RankingBean> rankingBeanList) {
        this.rankingBean = rankingBeanList == null ? new ArrayList<RankingBean>() : rankingBeanList;
        notifyDataSetChanged();
    }

    @Override
    public RankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_randkings, parent, false);
        RankAdapter.MyViewHolder viewHolder = new RankAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RankAdapter.MyViewHolder holder, int position) {
        holder.tv_number.setText(String.valueOf(position + 1));
        if (null == rankingBean.get(position).getUnickname()) {
            holder.tv_user_name.setText("");
        } else {
            holder.tv_user_name.setText(rankingBean.get(position).getUnickname());
        }

        if (null == rankingBean.get(position).getDistance()) {
            holder.tv_user_time.setText("0" + context.getString(R.string.km));
        } else {
            holder.tv_user_time.setText(String.valueOf(rankingBean.get(position).getDistance().doubleValue()) + context.getString(R.string.km));
        }


        if (!TextUtils.isEmpty(rankingBean.get(position).getUimg())) {
            CropCircleTransformation circle = (CropCircleTransformation) holder.user_imageview.getTag();
            if (circle == null) {
                circle = new CropCircleTransformation();
                holder.user_imageview.setTag(circle);
            }
            String uimg =  rankingBean.get(position).getUimg();
            if (!TextUtils.isEmpty(uimg) && !uimg.startsWith("http") && !uimg.contains("wx.qlogo.cn")) {
                uimg = ((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + uimg;
            }
            Picasso.with(context).load(uimg).transform(circle).error(R.drawable.leftbar_info).placeholder(R.drawable.leftbar_info).into(holder.user_imageview);
        }
    }


    @Override
    public int getItemCount() {
        return rankingBean.size();
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
