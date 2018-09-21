package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.run.model.HistoryModel;
import com.saiyi.gymequipment.run.ui.RunHistoryActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class HistoryPresenter extends PresenterImpl<RunHistoryActivity, HistoryModel> {
    public HistoryPresenter(Context context) {
        super(context);
    }

    @Override
    public HistoryModel initModel() {
        return new HistoryModel();
    }
}
