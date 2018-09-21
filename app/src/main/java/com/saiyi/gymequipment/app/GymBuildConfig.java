package com.saiyi.gymequipment.app;

import android.content.Context;

import com.saiyi.gymequipment.R;
import com.saiyi.libfast.config.IBuildConfig;
import com.saiyi.libfast.utils.AppUtil;
import com.saiyi.libfast.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GymBuildConfig implements IBuildConfig {

    private static final boolean DEBUG = false;//当前是否为debug版本

    private static final String BASE_DEBUG_HTTP_URL = "http://172.16.2.135:8089/";//debug版本下的请求地址
    private static final String BASE_HTTP_URL = "https://www.sjxty.net/";//正式版本的请求地址
    public static final String BASE_HTTP_URL_IMAGE_URL = BASE_HTTP_URL+"image";  //图片地址


    @Override
    public boolean isDebug() {
        return DEBUG;
    }

    @Override
    public String getHttpBaseUrl() {
        return DEBUG ? BASE_DEBUG_HTTP_URL : BASE_HTTP_URL;
    }

    @Override
    public int getVersionCode(Context context) {
        return AppUtil.getVersionCode(context);
    }

    @Override
    public String getVersionName(Context context) {
        return AppUtil.getVersionName(context);
    }

    @Override
    public String getAppName(Context context) {
        return StringUtils.getStringByResource(context, R.string.app_name);
    }

    public static final int isAdmin = 1;
    public static final int isUser = 2;
}
