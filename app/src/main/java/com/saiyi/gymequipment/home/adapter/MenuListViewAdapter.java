package com.saiyi.gymequipment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class MenuListViewAdapter extends BaseAdapter {

    public static final int FITNESS_CENTER = 0;
    public static final int FITNESS_HISTORY = 1;
    public static final int RANDKINGS = 2;
//    public static final int BLUETOOTH = 3;
    public static final int SCAN = 3;


    private Context mContext;
    private LayoutInflater inflater;

    private int[] icons = new int[]{R.drawable.fitness_center,
            R.drawable.fitness_history,
            R.drawable.rankings,
            R.drawable.scan};
    private String[] titles;


    public MenuListViewAdapter(Context context) {
        this.mContext = context;
        titles = new String[]{
                context.getResources().getString(R.string.fitness_center),
                context.getResources().getString(R.string.fitness_history),
                context.getResources().getString(R.string.ranking_list),
                context.getResources().getString(R.string.scan_code)
        };
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Math.min(icons.length, titles.length);
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            convertView = inflater.inflate(R.layout.adapter_home_menu, null);
            holder.iconIv = (ImageView) convertView.findViewById(R.id.item_icon_iv);
            holder.titleTv = (TextView) convertView.findViewById(R.id.item_head_tv);
            holder.msgTv = convertView.findViewById(R.id.item_msg_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position > icons.length) return convertView;
        holder.iconIv.setImageResource(icons[position]);
        holder.titleTv.setText(titles[position]);

//        if (titles[position].equals(mContext.getResources().getString(R.string.scan_code))) {
//            holder.msgTv.setVisibility(View.VISIBLE);
//        }else{
//            holder.msgTv.setVisibility(View.GONE);
//        }
        return convertView;
    }

    class ViewHolder {
        public ImageView iconIv;
        public TextView titleTv;
        private TextView msgTv;
    }
}
