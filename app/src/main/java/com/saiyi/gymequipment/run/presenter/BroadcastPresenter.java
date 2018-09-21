package com.saiyi.gymequipment.run.presenter;

import android.content.Context;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.run.model.BroadcastModel;
import com.saiyi.gymequipment.run.ui.BroadcastActivity;
import com.saiyi.libfast.mvp.PresenterImpl;

public class BroadcastPresenter extends PresenterImpl<BroadcastActivity, BroadcastModel> {
    public RemindDialog mRemindDialog;
    private Context context;

    public BroadcastPresenter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BroadcastModel initModel() {
        return new BroadcastModel();
    }

    /**
     * 提示对话框
     *
     * @param title   对话框标题
     * @param message 对话框提示内容
     */
    public void showCancelSetting(String title, String message, BaseDialog.OnDialogClick mOpenSettingDialogClick) {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(context);
        mRemindDialog = dialog;
        dialog.setTitleText(title);
        dialog.setMsgText(message);
        dialog.setComplateTextColorRes(R.color.colorAccent).setComplateText(context.getString(R.string.confirm));
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    //弹窗消失
    private void dismissRemindDialog() {
        if (mRemindDialog != null && mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
    }
}
