package com.saiyi.gymequipment.equipment.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPort;

import java.util.List;

public class DeviceAdapter extends BaseQuickAdapter<EquipmentPort, DeviceAdapter.DeviceViewHolder> {

    private OnItemDeleteClick mItemDeleteClick;

    public DeviceAdapter(int layoutResId, @Nullable List<EquipmentPort> data) {
        super(layoutResId, data);
    }

    public void setItemDeleteClick(OnItemDeleteClick itemDeleteClick) {
        this.mItemDeleteClick = itemDeleteClick;
    }

    @Override
    protected void convert(final DeviceViewHolder helper, EquipmentPort item) {
        TextView portTv = helper.getView(R.id.port_tv);
        TextView nameTv = helper.getView(R.id.name_tv);
        TextView deleteTv = helper.getView(R.id.delete_tv);
        final EasySwipeMenuLayout easySwipeMenuLayout = helper.getView(R.id.easy_swip_ml);
        if (deleteTv.getTag() == null) {
            deleteTv.setTag("tag");
            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easySwipeMenuLayout.resetStatus();
                    if (mItemDeleteClick != null) {
                        EquipmentPort device = getData().get(helper.getLayoutPosition());
                        mItemDeleteClick.onDeleteClick(device);
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
                        getOnItemClickListener().onItemClick(DeviceAdapter.this, helper.itemView, helper.getLayoutPosition());
                    }
                }
            });
        }
        portTv.setText(item.getEpnumber());
        nameTv.setText(item.getEquipmentPortType().getEtname());
    }

    protected class DeviceViewHolder extends BaseViewHolder {

        public DeviceViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClick {

        void onDeleteClick(EquipmentPort item);
    }
}
