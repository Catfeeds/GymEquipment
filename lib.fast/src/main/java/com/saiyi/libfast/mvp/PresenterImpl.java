package com.saiyi.libfast.mvp;

import android.content.Context;
import android.os.Handler;

import java.util.List;

/**
 * Created by siwei on 2018/3/13.
 */

public abstract class PresenterImpl<V extends IView, M extends IModel> implements IPresenter<V> {

    private V mIView;
    private M mIModel;
    private Handler mHandler;
    private Context mContext;

    public PresenterImpl(Context context) {
        this.mContext = context;
        mIModel = initModel();
        mHandler = new Handler();
    }

    public abstract M initModel();

    @Override
    public void attachView(V view) {
        this.mIView = view;
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public void detachView() {
        release();
    }

    private void release() {
        mIView = null;
        mContext = null;
        if (mIModel != null) {
            //Model层数据释放
            mIModel.onRelease();
            mIModel = null;
        }
        //Presenter层数据释放
        onRelease();
    }

    public V getView() {
        return mIView;
    }

    public M getModel() {
        return mIModel;
    }

    protected void onRelease() {
    }
}
