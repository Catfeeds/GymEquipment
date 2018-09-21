package com.saiyi.gymequipment.equipment.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;

import java.util.List;

public class EquipmentAdapter extends BaseQuickAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder> {

    private OnItemDeleteClick mItemDeleteClick;

    public void setItemDeleteClick(OnItemDeleteClick itemDeleteClick) {
        this.mItemDeleteClick = itemDeleteClick;
    }

    public EquipmentAdapter(int layoutResId, @Nullable List<Equipment> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final EquipmentViewHolder helper, Equipment item) {
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView locationTv = helper.getView(R.id.location_tv);
        TextView deleteTv = helper.getView(R.id.delete_tv);
        nameTv.setText(item.getEmac());
        if(FitnessCenterHelper.instance().isEditModel()){
            if (TextUtils.isEmpty(FitnessCenterHelper.instance().getGetFitnessCenter().getFcaddress())) {
                locationTv.setText("");
            } else {
                locationTv.setText(FitnessCenterHelper.instance().getGetFitnessCenter().getFcaddress());
            }
        }else{
            if (TextUtils.isEmpty(FitnessCenterHelper.instance().getFitnessCenter().getFcaddress())) {
                locationTv.setText("");
            } else {
                locationTv.setText(FitnessCenterHelper.instance().getFitnessCenter().getFcaddress());
            }
        }
        final EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swip_ml);
        easySwipeMenuLayout.setCanLeftSwipe(true);
        if (deleteTv.getTag() == null) {
            deleteTv.setTag("tag");
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easySwipeMenuLayout.resetStatus();
                    if (mItemDeleteClick != null) {
                        Equipment equipment = getData().get(helper.getLayoutPosition());
                        mItemDeleteClick.onDeleteClick(equipment);
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
                        getOnItemClickListener().onItemClick(EquipmentAdapter.this, helper.itemView, helper.getLayoutPosition());
                    }
                }
            });
        }
    }

    protected class EquipmentViewHolder extends BaseViewHolder {

        public EquipmentViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClick {

        void onDeleteClick(Equipment item);
    }

    public interface onItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}
