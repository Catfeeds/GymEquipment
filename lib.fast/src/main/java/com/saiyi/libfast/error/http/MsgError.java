package com.saiyi.libfast.error.http;

import com.saiyi.libfast.error.CustomError;

/**
 *
 */
public class MsgError implements CustomError {

    private int code = 1001;
    private String msg = "";

    public MsgError(String msg) {
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    @Deprecated
    public void setMsg(int strRes) {
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
