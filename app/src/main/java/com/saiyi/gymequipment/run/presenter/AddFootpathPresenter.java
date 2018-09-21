package com.saiyi.gymequipment.run.presenter;

import android.content.Context;
import android.util.Log;

import com.saiyi.gymequipment.run.event.BroadcastEvent;
import com.saiyi.gymequipment.run.model.AddFootpathModel;
import com.saiyi.gymequipment.run.ui.AddFootpathActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.ArrayList;

public class AddFootpathPresenter extends PresenterImpl<AddFootpathActivity, AddFootpathModel> {
    public AddFootpathPresenter(Context context) {
        super(context);
    }

    @Override
    public AddFootpathModel initModel() {
        return new AddFootpathModel();
    }

    /**
     * 添加步道
     *
     * @param name           步道名
     * @param distance       步道总长度
     * @param eventArrayList 步道包信息
     */
    public void addFootpath(String name, String distance, ArrayList<BroadcastEvent> eventArrayList) {

        getModel().addFootpath(name, distance, eventArrayList, new BaseResponseListener<String>() {

            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().addSuccess();
                Log.e("dismiss", "onResponse");
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().addFailed(e.msg);
            }
        });
    }


}
