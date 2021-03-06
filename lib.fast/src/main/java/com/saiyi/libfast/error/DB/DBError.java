package com.saiyi.libfast.error.DB;

import com.saiyi.libfast.R;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.error.CustomError;

/**
 * 数据库操作异常
 * code 1500-2000
 */
public enum DBError implements CustomError {

    /**存储用户信息失败*/
    UserSaveError(1500, R.string.UserSaveError);


    private int code;
    private int strRes;

    DBError(int code, int strRes) {
        this.code = code;
        this.strRes = strRes;
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
        return BaseApplication.getInstance().getResources().getString(strRes);
    }

    @Override
    public void setMsg(int strRes) {
        this.strRes = strRes;
    }
}
