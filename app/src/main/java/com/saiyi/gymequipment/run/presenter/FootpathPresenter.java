package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.model.FootpathModel;
import com.saiyi.gymequipment.run.ui.FootpathActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

/**
 * 描述：
 * 创建作者：ask
 * 创建时间：2018/5/2 14:00
 */

public class FootpathPresenter extends PresenterImpl<FootpathActivity, FootpathModel> {
    public FootpathPresenter(Context context) {
        super(context);
    }

    @Override
    public FootpathModel initModel() {
        return new FootpathModel();
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
                getView().getFootpathInfo(null);
            }
        });
    }

    /**
     * 删除步道
     *
     * @param footpathNumber 步道ID
     */
    public void deleteFootpath(Number footpathNumber, final int position) {
        getModel().deleteFootpath(footpathNumber, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().showData(position);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().deleteFail(e.toString());
            }
        });
    }
}
