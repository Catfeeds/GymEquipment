package com.saiyi.gymequipment.home.presenter;

import android.content.Context;

import com.saiyi.gymequipment.home.model.EquipmentModel;
import com.saiyi.gymequipment.home.model.bean.BannerBean;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.gymequipment.home.ui.EquipmentFragement;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.DateUtils;

public class EquipmentPresenter extends PresenterImpl<EquipmentFragement, EquipmentModel> {

    public EquipmentPresenter(Context context) {
        super(context);
    }

    @Override
    public EquipmentModel initModel() {
        return new EquipmentModel();
    }

    public void getTotalData() {
        String time = DateUtils.formatLogDate(System.currentTimeMillis());
        getModel().getTotalData(time, new BaseResponseListener<FitnessData>() {
            @Override
            public void onResponse(FitnessData data) {
                super.onResponse(data);
                if (data != null) {
                    getView().getTotalDataSuccess(data.getTimes().intValue(), data.getConsume().doubleValue(), data.getDuration().doubleValue());
                } else {
                    getView().getTotalDataSuccess(0, 0, 0);
                }
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getTotalDataFaild(e.msg);
            }
        });
    }

    public void getBanner() {
        getModel().getBanner(new BaseResponseListener<BannerBean>() {
            @Override
            public void onResponse(BannerBean data) {
                super.onResponse(data);
                getView().getBannerSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });
    }
}
