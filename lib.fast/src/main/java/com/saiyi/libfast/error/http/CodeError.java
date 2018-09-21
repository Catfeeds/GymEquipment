package com.saiyi.libfast.error.http;

import com.saiyi.libfast.R;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.error.CustomError;

/**
 * 后台返回状态码枚举
 */
public enum CodeError implements CustomError {

    CODE_UNKNOW(0, R.string.code_unknow),
    CODE1001(1001, R.string.code1001);

    private int code;
    private int strId;

    CodeError(int code, int strId) {
        this.code = code;
        this.strId = strId;
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
        return BaseApplication.getInstance().getResources().getString(strId);
    }

    @Override
    public void setMsg(int strRes) {
        this.strId = strRes;
    }
}
