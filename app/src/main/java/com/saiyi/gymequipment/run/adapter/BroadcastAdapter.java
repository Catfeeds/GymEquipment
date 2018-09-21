package com.saiyi.gymequipment.run.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.run.event.BroadcastEvent;

import java.util.List;

public class BroadcastAdapter extends BaseQuickAdapter<BroadcastEvent, BroadcastAdapter.BroadcastCenterViewHolder> {

    private BroadcastAdapter.OnItemDeleteClick mItemDeleteClick;
    private Context context;

    public BroadcastAdapter(Context context, int layoutResId, @Nullable List<BroadcastEvent> data) {
        super(layoutResId, data);
        this.context = context;
    }

    public void setItemDeleteClick(BroadcastAdapter.OnItemDeleteClick itemDeleteClick) {
        this.mItemDeleteClick = itemDeleteClick;
    }


    @Override
    protected void convert(final BroadcastAdapter.BroadcastCenterViewHolder helper, BroadcastEvent item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView locationTv = helper.getView(R.id.location_tv);
        TextView deleteTv = helper.getView(R.id.delete_tv);
        final EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swip_ml);
        if (deleteTv.getTag() == null) {
            deleteTv.setTag("tag");
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easySwipeMenuLayout.resetStatus();
                    if (mItemDeleteClick != null) {
                        BroadcastEvent center = getData().get(helper.getLayoutPosition());
                        mItemDeleteClick.onDeleteClick(center, helper.getLayoutPosition());
                    }
                }
            });
        }
        View contentView = helper.getView(R.id.content);
        if (contentView.getTag() == null) {
            contentView.setTag("tag");
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(BroadcastAdapter.this, helper.itemView, helper.getLayoutPosition());
                    }
                }
            });
        }
        nameTv.setText(item.getTbpmac());
        locationTv.setText(item.getTbpaddress());
    }



    protected class BroadcastCenterViewHolder extends BaseViewHolder {

        public BroadcastCenterViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClick {

        void onDeleteClick(BroadcastEvent item, int position);
    }
}
