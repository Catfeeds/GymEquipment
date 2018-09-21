package com.saiyi.gymequipment.common.error;

import com.saiyi.gymequipment.R;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.error.CustomError;
import com.saiyi.libfast.utils.StringUtils;

/**
 * App内部异常信息 1300-1400
 */
public enum AppError implements CustomError {

    /**
     * 检查当前网络是否可用
     */
    OPEN_LOCK_FAILD(1304, R.string.please_check_net_enable),
    /**
     * 检查当前网络是否可用
     */
    USER_NOT_LOGIN(1303, R.string.please_check_net_enable),
    /**
     * 检查当前网络是否可用
     */
    PLEASE_CHEK_NET_ENABLE(1300, R.string.please_check_net_enable),
    /**
     * 检查当前蓝牙设备是否可用
     */
    PLEASE_CHECK_BLE_ENABLE(1301, R.string.please_check_ble_enable),
    /**
     * 检查蓝牙连接的设备
     */
    PLEASE_CHECK_BLE_CONNECT_DEVICE(1302, R.string.please_check_ble_connect);

    private int code;
    private int msgRes;

    AppError(int code, int msgRes) {
        this.code = code;
        this.msgRes = msgRes;
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
        return StringUtils.getStringByResource(BaseApplication.getContext(), msgRes);
    }

    @Override
    public void setMsg(int strRes) {
        this.msgRes = msgRes;
    }
}
