package com.saiyi.gymequipment.run.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.GymBuildConfig;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.run.bean.FootpathBean;

import java.util.List;

public class FootpathAdapter extends BaseQuickAdapter<FootpathBean, FootpathAdapter.FootpathCenterViewHolder> {

    private OnItemDeleteClick mItemDeleteClick;
    private Context context;

    public FootpathAdapter(Context context, int layoutResId, @Nullable List<FootpathBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    public void setItemDeleteClick(OnItemDeleteClick itemDeleteClick) {
        this.mItemDeleteClick = itemDeleteClick;
    }


    @Override
    protected void convert(final FootpathCenterViewHolder helper, FootpathBean item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView locationTv = helper.getView(R.id.location_tv);
        TextView deleteTv = helper.getView(R.id.delete_tv);
        final EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swip_ml);
        if (UserHelper.instance().getUser().getIsAuthorize() == GymBuildConfig.isUser) {
            easySwipeMenuLayout.setCanLeftSwipe(false);
        }

        if (UserHelper.instance().getUser().getIsAuthorize() == GymBuildConfig.isAdmin) {
            easySwipeMenuLayout.setCanLeftSwipe(true);
        }


        if (deleteTv.getTag() == null) {
            deleteTv.setTag("tag");
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easySwipeMenuLayout.resetStatus();
                    if (mItemDeleteClick != null) {
                        FootpathBean center = getData().get(helper.getLayoutPosition());
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
                        getOnItemClickListener().onItemClick(FootpathAdapter.this, helper.itemView, helper.getLayoutPosition());
                    }
                }
            });
        }
        nameTv.setText(item.getTname());
        locationTv.setText(String.format(context.getString(R.string.distance_to_footpath), item.getBroadcastPackets().get(0).getDistance()));
    }


    protected class FootpathCenterViewHolder extends BaseViewHolder {

        public FootpathCenterViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClick {

        void onDeleteClick(FootpathBean item, int position);
    }
}
