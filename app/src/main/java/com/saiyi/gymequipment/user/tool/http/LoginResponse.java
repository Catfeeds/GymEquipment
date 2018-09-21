package com.saiyi.gymequipment.user.tool.http;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/6/23.
 */
public class LoginResponse<T> implements Serializable {

    private boolean success;
    private String message;
    private int code;
    private int isAuthorize;

    @SerializedName("data")
    private T data;

    public int getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(int isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", isAuthorize=" + isAuthorize +
                ", data=" + data +
                '}';
    }
}
