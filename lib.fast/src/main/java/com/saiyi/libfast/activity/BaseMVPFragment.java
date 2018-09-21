package com.saiyi.libfast.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.saiyi.libfast.mvp.IView;
import com.saiyi.libfast.mvp.PresenterImpl;

/**
 * Created by siwei on 2018/3/13.
 * 给予MVP封装的Fragment
 */

public abstract class BaseMVPFragment<P extends PresenterImpl> extends BaseFragment implements IView {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter(getActivity());
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    public abstract P initPresenter(Context context);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detachView();
        }
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {
    }

    protected P getPresenter(){
        return mPresenter;
    }
}
