package com.saiyi.gymequipment.run.presenter;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.run.model.RunningModel;
import com.saiyi.gymequipment.run.ui.RunningActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

public class RunningPresenter extends PresenterImpl<RunningActivity, RunningModel> {

    private Context context;
    public RemindDialog mRemindDialog;

    public RunningPresenter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public RunningModel initModel() {
        return new RunningModel();
    }


    /**
     * 是否打开蓝牙
     *
     * @param isOpenBle true 打开 false 没打开
     */
    public void openBleShow(boolean isOpenBle) {
        if (!isOpenBle) {
            showCancelSetting(R.color.colorAccent, context.getString(R.string.confirm), context.getString(R.string.bluetooth_not_opened), context.getString(R.string.bluetooth_not_opened_tips), new BaseDialog.OnDialogClick() {
                @Override
                public void onClick(int whichOne) {
                    if (whichOne == RemindDialog.WHICH_COMPLATE) {
                        if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    /**
     * 提示对话框
     *
     * @param title   对话框标题
     * @param message 对话框提示内容
     */
    public void showCancelSetting(int color, String sure, String title, String message, BaseDialog.OnDialogClick mOpenSettingDialogClick) {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(context);
        mRemindDialog = dialog;
        dialog.setTitleText(title);
        dialog.setMsgText(message);
        dialog.setComplateTextColorRes(color).setComplateText(sure);
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    //弹窗消失
    private void dismissRemindDialog() {
        if (mRemindDialog != null && mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
    }

    /**
     * 添加跑步数据
     *
     * @param reconsume
     * @param redistance
     * @param reduration
     * @param respeed
     * @param restepNumber
     */
    public void addRunningData(double reconsume, double redistance, double reduration, double respeed, double restepNumber,Number footpathId) {
        getModel().addRunningData(reconsume, redistance, reduration, respeed, restepNumber,footpathId, new BaseResponseListener<String>() {
            @Override
            public void onResponse(String data) {
                super.onResponse(data);
                getView().addDataSuccess(2);
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().addDataFail();
            }
        });
    }

}
