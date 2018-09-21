package com.saiyi.gymequipment.user.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.user.model.SupplementInfoModel;
import com.saiyi.gymequipment.user.ui.SupplementInfoActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.StringUtils;


public class SupplementInfoPresenter extends PresenterImpl<SupplementInfoActivity, SupplementInfoModel> {

    public SupplementInfoPresenter(Context context) {
        super(context);
    }

    @Override
    public SupplementInfoModel initModel() {
        return new SupplementInfoModel();
    }


    public void updateUserDate(String nickName, String sex, String brithDay, String height, String weight, String sosPhone) {
        int h;
        if (TextUtils.isEmpty(height)) {
            getView().showErrorMsg(getContext().getString(R.string.input_info));
            return;
        } else {
            h = Integer.parseInt(height);
        }
        int w;
        if (TextUtils.isEmpty(weight)) {
            getView().showErrorMsg(getContext().getString(R.string.input_info));
            return;
        } else {
            w = Integer.parseInt(weight);
        }
        if (TextUtils.isEmpty(nickName)) {
            getView().showErrorMsg(getContext().getString(R.string.input_info));
            return;
        }
        if (TextUtils.isEmpty(sosPhone)) {
            getView().showErrorMsg(getContext().getString(R.string.input_info));
            return;
        }
        if (TextUtils.isEmpty(brithDay)) {
            getView().showErrorMsg(getContext().getString(R.string.input_info));
            return;
        }
        if(!StringUtils.matchesDate(brithDay)){
            getView().showErrorMsg(getContext().getString(R.string.brithday_format_error));
            return;
        }
        getModel().updateUserDate(nickName, sex, brithDay, h, w, sosPhone,new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().onSupplementInfoSuccess(data);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().onSupplementInfoFaild(e.msg);
            }
        });
    }
}
