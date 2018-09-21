package com.saiyi.gymequipment.common.permission;


import android.Manifest;

import com.saiyi.gymequipment.R;
import com.saiyi.libfast.utils.StringUtils;

/**
 * 项目：智能控制     SmartLock
 */
public enum PermissionRationale {

    //蓝牙或定位需要的权限
    LOCATION_PERMISSION(StringUtils.getStringByResource(R.string.permission_bluetooth_admin),
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),
    //蓝牙需要的权限
    BLE_PERMISSION(StringUtils.getStringByResource(R.string.permission_bluetooth),
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN),
    //相机需要的权限
    CAMERA_PERMISSION(StringUtils.getStringByResource(R.string.permission_camera),
            Manifest.permission.CAMERA),
    //读写文件需要的权限
    WRITE_READ_PERMISSION(StringUtils.getStringByResource(R.string.permission_write_external_storgage),
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE);

    private String[] permission;//权限
    private String rationale;//为什么需要权限的介绍

    PermissionRationale(String rationale, String... permission) {
        this.permission = permission;
        this.rationale = rationale;
    }

    public String[] getPermission() {
        return permission;
    }

    public void setPermission(String... permission) {
        this.permission = permission;
    }

    public String getRationale() {
        return rationale;
    }

    public void setRationale(String rationale) {
        this.rationale = rationale;
    }
}
