package com.saiyi.gymequipment.equipment.presenter;

import android.content.Context;

import com.saiyi.gymequipment.common.constans.RequestConstans;
import com.saiyi.gymequipment.equipment.model.FitnessRecordModel;
import com.saiyi.gymequipment.equipment.model.bean.FitnessRecord;
import com.saiyi.gymequipment.equipment.ui.FitnessRecordMonthFragement;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.DateUtils;

import java.util.List;

public class FitnessRecordMonthPresenter extends PresenterImpl<FitnessRecordMonthFragement, FitnessRecordModel> {

    public FitnessRecordMonthPresenter(Context context) {
        super(context);
    }

    @Override
    public FitnessRecordModel initModel() {
        return new FitnessRecordModel();
    }

    public void getTimesTotalRecordMonth() {
        String time = DateUtils.formatLogDate(System.currentTimeMillis());
        getModel().getTotalData(RequestConstans.GET_TIMESRECORDS_TYPE_MONTH, time, new BaseResponseListener<FitnessData>() {
            @Override
            public void onResponse(FitnessData data) {
                super.onResponse(data);
                getView().setmFitnessData(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });

    }


    public void getFitnessRecordsMonth() {
        String time = DateUtils.formatLogDate(System.currentTimeMillis());
        getModel().getFitnessRecords(RequestConstans.GET_TIMESRECORDS_TYPE_MONTH, time, new BaseResponseListener<List<FitnessRecord>>() {
            @Override
            public void onResponse(List<FitnessRecord> data) {
                super.onResponse(data);
                getView().setmFitnessRecords(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
            }
        });
    }
}
