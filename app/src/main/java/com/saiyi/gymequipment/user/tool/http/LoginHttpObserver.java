package com.saiyi.gymequipment.user.tool.http;

import com.saiyi.libfast.error.ErrorEngine;
import com.saiyi.libfast.http.ResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/19.
 */

public abstract class LoginHttpObserver<T> implements Observer<LoginResponse<T>> {

    private ResponseListener<T> mResponseListener;
    private CompositeDisposable compositeDisposable;
    private Disposable disposable;

    public LoginHttpObserver(ResponseListener<T> responseListener){
        this.mResponseListener = responseListener;
    }

    public LoginHttpObserver(CompositeDisposable compositeDisposable, ResponseListener<T> responseListener){
        this.mResponseListener = responseListener;
        this.compositeDisposable = compositeDisposable;
    }

    public ResponseListener<T> getResponseListener() {
        return mResponseListener;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if(compositeDisposable != null){
            compositeDisposable.add(d);
        }
    }

    @Override
    public void onNext(LoginResponse<T> tBaseResponse) {
        onResponse(tBaseResponse);
        //Next执行完成则任务任务被执行完成了
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        if(mResponseListener != null){
            final ErrorStatus apiException = ErrorEngine.handleHttpException(e);
            Logger.d("接口执行失败; code="+apiException.code+" 信息:"+apiException.msg);
            Observable.empty().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Object o) {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    mResponseListener.onFailed(apiException);
                }
            });

        }
        unSubscrible();
    }

    @Override
    public void onComplete() {
        unSubscrible();
    }

    protected void dispatchListenerResponse(T data){
        if(mResponseListener != null){
            mResponseListener.onResponse(data);
        }
    }

    protected void dispatchListenerFaild(ErrorStatus e){
        if(mResponseListener != null){
            mResponseListener.onFailed(e);
        }
    }

    public abstract void onResponse(LoginResponse<T> response);

    /**解除订阅*/
    protected void unSubscrible(){
        if(disposable != null && !disposable.isDisposed()){
            disposable.dispose();
            if(compositeDisposable != null){
                compositeDisposable.remove(disposable);
            }
        }
        if(mResponseListener != null){
            mResponseListener.onComplete();
        }
    }


}
