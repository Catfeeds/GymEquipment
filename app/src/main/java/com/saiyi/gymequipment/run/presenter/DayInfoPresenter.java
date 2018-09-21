package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.run.bean.RunHistoryBean;
import com.saiyi.gymequipment.run.fragment.DayInfoFragment;
import com.saiyi.gymequipment.run.model.DayInfoModel;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

public class DayInfoPresenter extends PresenterImpl<DayInfoFragment, DayInfoModel> {
    public DayInfoPresenter(Context context) {
        super(context);
    }

    @Override
    public DayInfoModel initModel() {
        return new DayInfoModel();
    }


    /**
     * 获取跑步详细记录
     *
     * @param dayType      数据类型 1-日,2-周， 3-月
     * @param recreateTime 查询时间  格式：2018-04-25 10:59:48
     */
    public void getRunInformation(int dayType, String recreateTime) {
        getModel().getRunInformation(dayType, recreateTime, new BaseResponseListener<List<RunHistoryBean>>() {
            @Override
            public void onResponse(List<RunHistoryBean> data) {
                super.onResponse(data);
                getView().getRunDetailedInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().infoGetFail(2);
            }
        });
    }

    /**
     * 获取跑步总记录
     *
     * @param dayType      1-日,2-周， 3-月， 4-总
     * @param recreateTime 查询时间  格式：2018-04-25 10:59:48
     */
    public void getRunTotalInformation(int dayType, String recreateTime) {
        getModel().getRunTotalInformation(dayType, recreateTime, new BaseResponseListener<RunInfoBean>() {
            @Override
            public void onResponse(RunInfoBean data) {
                super.onResponse(data);
                getView().totalDayInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().infoGetFail(1);
            }
        });
    }


}
