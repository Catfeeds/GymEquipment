package com.saiyi.libfast.http.exception;


import com.saiyi.libfast.error.CustomError;

/**
 * 错误状态
 */
public class ErrorStatus {

    public int code;
    public String msg;

    public ErrorStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorStatus(CustomError error) {
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

    public ErrorStatus() {
    }

    public void setError(CustomError error) {
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
