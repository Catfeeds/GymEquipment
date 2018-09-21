package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.me.model.EquipmentRepairModel;
import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.gymequipment.me.ui.EquipmentRepairActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.io.File;
import java.util.List;

public class EquipmentRepairPresenter extends PresenterImpl<EquipmentRepairActivity, EquipmentRepairModel> {
    public EquipmentRepairPresenter(Context context) {
        super(context);
    }

    @Override
    public EquipmentRepairModel initModel() {
        return new EquipmentRepairModel();
    }

    public void getQuestions() {
        getView().showCustomLoading();
        getModel().getQuestions(new BaseResponseListener<List<Question>>() {
            @Override
            public void onResponse(List<Question> data) {
                super.onResponse(data);
                getView().getQuestionsSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().getQuestionsFaild(e.msg);
            }
        });
    }

    public void addFeedback(final String mac, final String qmsg, final int id, final File imgfile) {
        getView().showCustomsubmiting();
        getModel().addFeedbackImg(imgfile, new BaseResponseListener<String>(){
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getModel().addFeedback(mac, qmsg, id, data, new BaseResponseListener<String>() {
                    @Override
                    public void onResponse(String data) {
                        super.onResponse(data);
                        getView().addFeedbackSuccess();
                    }

                    @Override
                    public void onFailed(ErrorStatus e) {
                        super.onFailed(e);
                        getView().addFeedbackFaild(e.msg);
                    }
                });
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().addFeedbackFaild(e.msg);
            }
        });
    }
}
