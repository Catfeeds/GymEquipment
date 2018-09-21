package com.saiyi.gymequipment.home.presenter;

import android.content.Context;

import com.saiyi.gymequipment.home.model.RunModel;
import com.saiyi.gymequipment.home.model.bean.RunInfoBean;
import com.saiyi.gymequipment.home.ui.RunningFragement;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class RunPresenter extends PresenterImpl<RunningFragement, RunModel> {
    public RunPresenter(Context context) {
        super(context);
    }

    @Override
    public RunModel initModel() {
        return new RunModel();
    }

    /**
     * 获取跑步总记录
     *
     * @param dayType
     * @param recreatetime
     */
    public void getRunInfomation(int dayType, String recreatetime) {
        getModel().getRunInformation(dayType, recreatetime, new BaseResponseListener<RunInfoBean>() {
            @Override
            public void onResponse(RunInfoBean data) {
                super.onResponse(data);
                getView().getRunInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getRunInfo(null);
            }
        });
    }

}
