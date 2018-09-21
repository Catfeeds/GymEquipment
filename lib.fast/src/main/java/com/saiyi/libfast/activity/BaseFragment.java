package com.saiyi.libfast.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.saiyi.libfast.activity.tool.InputMethodHelper;
import com.saiyi.libfast.activity.tool.LoadingProgressHelper;
import com.saiyi.libfast.cache.ACache;
import com.saiyi.libfast.event.EventAction;
import com.saiyi.libfast.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by siwei on 2015/6/9.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    private InputMethodHelper mInputMethodHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    protected void registerEventBus(){
        EventBus.getDefault().register(this);
    }

    /**event bus事件接收*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventAction event) {
        /* Do something */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingProgressHelper != null) {
            mLoadingProgressHelper.destory();
        }
        mInputMethodHelper.fixInputMethodManagerLeak(getActivity());
        mInputMethodHelper = null;
        EventBus.getDefault().unregister(this);
    }

    protected abstract void initView(View view);

    protected abstract void initListener();

    protected abstract void initData();

    private LoadingProgressHelper mLoadingProgressHelper;

    protected boolean isLoadingShowed() {
        if (mLoadingProgressHelper != null) {
            return mLoadingProgressHelper.isLoadingShowed();
        }
        return false;
    }

    protected void showLoadingDialog() {
        if (mLoadingProgressHelper == null) {
            mLoadingProgressHelper = new LoadingProgressHelper(getActivity());
        }
        mLoadingProgressHelper.showProgress();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingProgressHelper != null) {
            mLoadingProgressHelper.dismissProgress();
        }
    }

    public void toast(String msg) {
        if(mContext != null){
            ToastUtils.showToast(mContext.getApplicationContext(), msg);
        }
    }

    /**
     * 获取缓存
     */
    public ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    /**
     * 关闭软键盘
     */
    protected void closeSoftInput(){
        mInputMethodHelper.closeSoftInput(getActivity());
    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput(EditText et){
        mInputMethodHelper.showSoftInput(et);
    }

    protected void openActivity(Class<? extends Activity> pClass) {
        openActivity(pClass, null);
    }


    protected void openActivity(Class<? extends Activity> pClass, Bundle pBundle) {
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    protected void openActivityForResult(Class<? extends Activity> pClass, int requestCode){
        openActivityForResult(pClass, null, requestCode);
    }

    protected void openActivityForResult(Class<? extends Activity> pClass, Bundle pBundle, int requestCode){
        Intent intent = new Intent(mContext, pClass);
        if(pBundle != null){
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
