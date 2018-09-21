package com.saiyi.gymequipment.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.home.adapter.MenuListViewAdapter;
import com.saiyi.gymequipment.me.model.bean.Article;
import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FitnessNewsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Article> list = new ArrayList<>();

    public FitnessNewsAdapter(Context context) {
        this.mContext = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_fitnessnews, null);
            holder.iconIv = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.titleTv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.msgTv = convertView.findViewById(R.id.tv_context);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article article = list.get(position);
        Picasso.with(mContext).load(article.getHgimg()).placeholder(R.drawable.app_icon).error(R.drawable.app_icon).into(holder.iconIv);
        holder.titleTv.setText(article.getHgtitle());
        holder.msgTv.setText(article.getHgccontent());
        return convertView;
    }

    class ViewHolder {
        public ImageView iconIv;
        public TextView titleTv;
        private TextView msgTv;
    }

    public void update(List<Article> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<Article> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
