package com.saiyi.gymequipment.common.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.libfast.cache.SharePerferenceHelper;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.permission.JumpPSettingTools;
import com.saiyi.libfast.utils.StringUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 权限申请helper
 */
public class PermissionHelper {

    /**当新版本有权限添加需要重新请求权限的,code加1即可*/
    private static final int PERMISSION_VERSION_CODE = 1;

    private int checkedPerVCode;//已检查过权限的版本号
    private RxPermissions rxPermissions;
    private PermissionRationale[] permissions;
    private RemindMsgDialog msgDialog;
    private Context mContext;
    private boolean isAllGranted;//是否所有权限都被授予
    private boolean shouldShowRationale;//是否存在权限需要显示原因
    private boolean shouldShowSetting;//是否存在权限需要提醒前往设置
    private int requestRotionaleTime;//提醒权限请求次数
    private int goSettingRequestTime;//提醒前往设置次数
    private final int MAX_ROTIONALE_TIME = 3;//提醒权限请求的最多次数
    private final int MAX_GO_SETTING_TIME = 1;//提醒前往权限设置的最多次数
    private SharePerferenceHelper spHelper;
    private int grantedSize, checkPermissionSize, permissionSize;

    public PermissionHelper(Activity context, PermissionRationale... permissions) {
        rxPermissions = new RxPermissions(context);
        this.permissions = permissions;
        msgDialog = new RemindMsgDialog(context);
        mContext = context;
        msgDialog.setCancelable(false);
        spHelper = SharePerferenceHelper.createSharePerference("permission_helper");
        initData();
    }

    private void initData() {
        checkedPerVCode = spHelper.getInt("checkedPerVCode", -1);
        if (checkedPerVCode != PERMISSION_VERSION_CODE) {
            //权限检测版本进行了更新,需要重新检测所有的权限,以防止有新的权限添加进来
            checkedPerVCode = PERMISSION_VERSION_CODE;
        } else {
            isAllGranted = spHelper.getBoolean("isAllGranted", false);
            shouldShowRationale = spHelper.getBoolean("shouldShowRationale", false);
            shouldShowSetting = spHelper.getBoolean("shouldShowSetting", false);
            requestRotionaleTime = spHelper.getInt("requestRotionaleTime", 0);
            goSettingRequestTime = spHelper.getInt("goSettingRequestTime", 0);
        }
    }

    private void saveData() {
        spHelper.putBoolean("isAllGranted", isAllGranted);
        spHelper.putBoolean("shouldShowRationale", shouldShowRationale);
        spHelper.putBoolean("shouldShowSetting", shouldShowSetting);
        spHelper.putInt("requestRotionaleTime", requestRotionaleTime);
        spHelper.putInt("goSettingRequestTime", goSettingRequestTime);
        spHelper.putInt("checkedPerVCode", checkedPerVCode);
    }

    private void initRequestDialog() {
        msgDialog.hidenCancle();
        msgDialog.setComplateText(StringUtils.getStringByResource(R.string.permission_ok));
        msgDialog.setTitleText(StringUtils.getStringByResource(R.string.permission_desc));
        msgDialog.setMsgText(StringUtils.getStringByResource(R.string.permission_desc_info));
        msgDialog.setClick(mDialogClick);
    }

    private void initSettingDialog() {
        msgDialog.hidenCancle();
        msgDialog.setComplateText(StringUtils.getStringByResource(R.string.permission_ok));
        msgDialog.setTitleText(StringUtils.getStringByResource(R.string.permission_rejected));
        msgDialog.setMsgText(StringUtils.getStringByResource(R.string.permission_lacked_tips));
        msgDialog.setClick(mSettingDialogClick);
    }

    //权限申请弹窗事件
    final BaseDialog.OnDialogClick mDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindMsgDialog.WHICH_COMPLATE) {
                //开始进行权限申请
                String[] requestPermissions = getPermissions(permissions);
                permissionSize = requestPermissions.length;
                Logger.d("PermissionHelper  permissionSize=%s requestPermissions=%s", permissionSize, requestPermissions);
                requestPermission(requestPermissions);
            }
        }
    };

    //设置权限弹窗事件
    BaseDialog.OnDialogClick mSettingDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindMsgDialog.WHICH_COMPLATE) {
                goPermissionSetting(mContext);
            }
        }
    };

    /**
     * 申请权限
     */
    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Logger.d("PermissionHelper  isAllGranted=%s shouldShowRationale=%s shouldShowSetting=%s", isAllGranted, shouldShowRationale, shouldShowSetting);
            if (!isAllGranted) {
                if (shouldShowRationale && requestRotionaleTime < MAX_ROTIONALE_TIME) {
                    //提醒用户,要进行权限申请
                    initRequestDialog();
                    requestRotionaleTime += 1;
                } else if (shouldShowSetting && goSettingRequestTime < MAX_GO_SETTING_TIME) {
                    //前往设置页面进行权限设置
                    initSettingDialog();
                    goSettingRequestTime += 1;
                } else {
                    //权限申请
                    initRequestDialog();
                }
                showDialog();
            } else {
                //同意所有的权限
                //do nothing
            }
        } else {
            //低版本默认授予所有权限
            isAllGranted = true;
            shouldShowRationale = false;
            shouldShowSetting = false;
        }
        saveData();
    }

    private void showDialog() {
        if (msgDialog != null && !msgDialog.isShowing()) {
            msgDialog.show();
        }
    }

    //前往权限设置
    private void goPermissionSetting(Context context) {
        JumpPSettingTools.jumpPermisionSetting(context);
    }

    //申请权限
    private void requestPermission(String[] permissions) {
        rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                checkPermissionSize = checkPermissionSize + 1;
                if (permission.granted) {
                    //权限被授予
                    grantedSize = grantedSize + 1;
                    if (grantedSize == permissionSize) {
                        isAllGranted = true;
                        shouldShowSetting = false;
                        shouldShowSetting = false;
                    }
                    Logger.d("PermissionHelper " + permission.name + " granted isAllGranted=" + isAllGranted);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    //权限被拒绝,需要显示权限申请理由
                    shouldShowRationale = true;
                    shouldShowSetting = false;
                    isAllGranted = false;
                    Logger.d("PermissionHelper " + permission.name + " denied permission with ask never again");
                } else {
                    //权限被直接拒绝
                    isAllGranted = false;
                    shouldShowRationale = false;
                    shouldShowSetting = true;
                    Logger.d("PermissionHelper " + permission.name + " denied permission with ask never again. Need to go to the settings");
                }

                if (checkPermissionSize == permissionSize) {
                    //权限检查完成
                    saveData();
                }
            }
        });
    }

    //获取权限
    private String[] getPermissions(PermissionRationale[] permissions) {
        List<String> pList = new ArrayList<>();
        for (PermissionRationale pr : permissions) {
            pList.addAll(Arrays.asList(pr.getPermission()));
        }
        String[] permissionsStrs = new String[pList.size()];
        for (int i = 0; i < permissionsStrs.length; i++) {
            permissionsStrs[i] = pList.get(i);
        }
        return permissionsStrs;
    }

    /**
     * 数据释放
     */
    public void release() {
        saveData();
        mContext = null;
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
        rxPermissions = null;
        permissions = null;
    }
}
