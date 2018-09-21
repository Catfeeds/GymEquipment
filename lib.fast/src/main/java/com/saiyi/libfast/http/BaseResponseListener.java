package com.saiyi.libfast.http;

import com.saiyi.libfast.http.exception.ErrorStatus;

/**
 * Created by Administrator on 2018/3/19.
 */

public class BaseResponseListener<T> implements ResponseListener<T>{

    @Override
    public void onComplete() {
    }

    @Override
    public void onResponse(T data) {
    }

    public void onFailed(ErrorStatus e) {
    }
}
