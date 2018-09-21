package com.saiyi.gymequipment.equipment.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.model.bean.FitnessCenter;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;

import java.util.List;

public class FitnessCenterAdapter extends BaseQuickAdapter<GetFitnessCenter, FitnessCenterAdapter.FitnessCenterViewHolder> {

    private OnItemDeleteClick mItemDeleteClick;

    public FitnessCenterAdapter(int layoutResId, @Nullable List<GetFitnessCenter> data) {
        super(layoutResId, data);
    }

    public void setItemDeleteClick(OnItemDeleteClick itemDeleteClick) {
        this.mItemDeleteClick = itemDeleteClick;
    }


    @Override
    protected void convert(final FitnessCenterViewHolder helper, GetFitnessCenter item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView locationTv = helper.getView(R.id.location_tv);
        TextView deleteTv = helper.getView(R.id.delete_tv);
        final EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swip_ml);
        if(UserHelper.instance().getUser().isAdmin()){
            easySwipeMenuLayout.setCanLeftSwipe(true);
            if (deleteTv.getTag() == null) {
                deleteTv.setTag("tag");
                deleteTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        easySwipeMenuLayout.resetStatus();
                        if (mItemDeleteClick != null) {
                            GetFitnessCenter center = getData().get(helper.getLayoutPosition());
                            mItemDeleteClick.onDeleteClick(center);
                        }
                    }
                });
            }
        }else{
            easySwipeMenuLayout.setCanLeftSwipe(false);
        }
        View contentView = helper.getView(R.id.content);
        if (contentView.getTag() == null) {
            contentView.setTag("tag");
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onItemClick(FitnessCenterAdapter.this, helper.itemView, helper.getLayoutPosition());
                    }
                }
            });
        }
        nameTv.setText(item.getFcdefinition());
        locationTv.setText(item.getFcaddress());
    }


    protected class FitnessCenterViewHolder extends BaseViewHolder {

        public FitnessCenterViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClick {

        void onDeleteClick(GetFitnessCenter item);
    }
}
