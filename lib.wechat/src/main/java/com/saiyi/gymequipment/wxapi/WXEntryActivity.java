package com.saiyi.gymequipment.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.saiyi.gymequipment.WLogger;
import com.saiyi.gymequipment.WeChatManager;
import com.saiyi.gymequipment.view.LoadingStatusDialog;
import com.saiyi.libwechat.BuildConfig;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by JingNing on 2018-07-13 16:31
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WeChatManager";
    private IWXAPI mWeixinAPI;
    private static String uuid;
    private LoadingStatusDialog mLoadingProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
        WLogger.d(TAG, "onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
        WLogger.d(TAG, "onNewIntent");
    }

    @Override
    public void onReq(BaseReq baseReq) {
        WLogger.d(TAG, "onReq");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        WLogger.d(TAG, "onResp");
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                WLogger.d(TAG, "ERR_OK");
                showCustomLoading("正在登录...");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    WeChatManager.getInstance().getAccessToken(this, code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                WeChatManager.getInstance().loginErr(this);
                WLogger.d(TAG, "ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                WLogger.d(TAG, "ERR_AUTH_DENIED");
                //发送被拒绝
                WeChatManager.getInstance().loginErr(this);
                break;
            default:
                //发送返回
                break;
        }
    }

    /**
     * 显示自定义提醒信息的loading
     */
    protected void showCustomLoading(String msg) {
        LoadingStatusDialog.LoadingStatus status = new LoadingStatusDialog.LoadingStatus(LoadingStatusDialog.LEVEL_LOADING, "正在加载", true);
        mLoadingProgress = new LoadingStatusDialog(this, status);
        mLoadingProgress.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingProgress != null && mLoadingProgress.isShowing()) {
            mLoadingProgress.dismiss();
        }
    }
}
