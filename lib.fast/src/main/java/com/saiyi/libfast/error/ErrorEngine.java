package com.saiyi.libfast.error;

import com.saiyi.libfast.error.http.CodeHelper;
import com.saiyi.libfast.error.http.HttpError;
import com.saiyi.libfast.error.http.MsgError;
import com.saiyi.libfast.http.exception.ErrorStatus;

import org.apache.http.HttpException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.net.ssl.SSLException;


public class ErrorEngine {

    public static ErrorStatus handleHttpException(Throwable t) {
        ErrorStatus apiException = new ErrorStatus();
        System.out.println("handleHttpException Throwable=" + t);
        if (t instanceof HttpException) {
            apiException.setError(HttpError.HTTP_ERROR);
        } else if (t instanceof JSONException || t instanceof ParseException) {
            apiException.setError(HttpError.PARSE_ERROR);
        } else if (t instanceof ConnectException || t instanceof SocketTimeoutException) {
            apiException.setError(HttpError.NETWORD_ERROR);
        } else if (t instanceof UnknownHostException) {
            apiException.setError(HttpError.UNKNOW);
        } else if (t instanceof SSLException) {
            apiException.setError(HttpError.SSL_ERROR);
        } else {
            apiException.setError(HttpError.UNKNOW);
        }
        return apiException;
    }

    public static ErrorStatus handleParamsException() {
        return new ErrorStatus(HttpError.PARAMS_ERROR);
    }

    public static ErrorStatus handleIdentifyError() {
        return new ErrorStatus(HttpError.SMS_IDENTIFY_ERROR);
    }

    public static ErrorStatus handleServiceResultError(int code) {
        return CodeHelper.handleCodeError(code);
    }

    public static ErrorStatus handleCustomError(CustomError error) {
        return new ErrorStatus(error);
    }

    public static ErrorStatus handleServiceResultError(String msg) {
        return new ErrorStatus(new MsgError(msg));
    }
}
