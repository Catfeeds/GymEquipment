package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.model.FootpathMapModel;
import com.saiyi.gymequipment.run.ui.FootpathMapActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

/**
 * 描述：
 * 创建作者：ask
 * 创建时间：2018/5/2 14:00
 */

public class FootpathMapPresenter extends PresenterImpl<FootpathMapActivity, FootpathMapModel> {
    public FootpathMapPresenter(Context context) {
        super(context);
    }

    @Override
    public FootpathMapModel initModel() {
        return new FootpathMapModel();
    }

    /**
     * 获取步道列表
     *
     * @param clatitude  经度
     * @param clongitude 纬度
     * @param tname      步道名
     */
    public void getFootpathInfo(double clatitude, double clongitude, String tname) {
        getModel().getFootPathInformation(clatitude, clongitude, tname, new BaseResponseListener<List<FootpathBean>>() {

            @Override
            public void onResponse(List<FootpathBean> data) {
                super.onResponse(data);
                getView().getFootpathInfo(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getFootpathFailed(e.msg);
            }
        });
    }
}
