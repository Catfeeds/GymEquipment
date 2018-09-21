package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.run.bean.RankingBean;
import com.saiyi.gymequipment.run.model.RankingModel;
import com.saiyi.gymequipment.run.ui.RankingListActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

public class RankingPresenter extends PresenterImpl<RankingListActivity, RankingModel> {
    public RankingPresenter(Context context) {
        super(context);
    }

    @Override
    public RankingModel initModel() {
        return new RankingModel();
    }

    /**
     * 获取排行榜数据
     */
    public void rankingData() {
        getModel().addRunningData(new BaseResponseListener<List<RankingBean>>() {
            @Override
            public void onResponse(List<RankingBean> data) {
                super.onResponse(data);
                getView().rankingInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().rankingInfoFali(e.msg);
            }
        });
    }
}
