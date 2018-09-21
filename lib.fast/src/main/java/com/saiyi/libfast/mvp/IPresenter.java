package com.saiyi.libfast.mvp;

/**
 * Created by siwei on 2018/3/12.
 * MVP Presenter
 */
public interface IPresenter<V extends IView> {

    void attachView(V view);

    void detachView();
}
