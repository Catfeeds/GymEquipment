package com.saiyi.libfast.http;

import com.saiyi.libfast.http.exception.ErrorStatus;

/**
 * Created by Administrator on 2018/3/19.
 */

public interface ResponseListener<T> {

    void onComplete();

    void onResponse(T data);

    void onFailed(ErrorStatus e);

}
