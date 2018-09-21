package com.saiyi.gymequipment.app;

import android.support.annotation.NonNull;

import com.lib.bugly.BuglyLib;
import com.lib.jpush.JPushLib;
import com.saiyi.gymequipment.BuildConfig;
import com.saiyi.libfast.activity.BaseApplication;
import com.saiyi.libfast.logger.Logger;

public class GymApplication extends BaseApplication {

    @NonNull
    @Override
    public GymBuildConfig getBuildConfig() {
        return new GymBuildConfig();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        BuglyLib.addUserData(this, "CodeVersion", String.valueOf(BuildConfig.VERSION_CODE));
        BuglyLib.addUserData(this, "BuildType", BuildConfig.BUILD_TYPE);
        BuglyLib.initBugly(this, true);
        JPushLib.initSdk(this, getBuildConfig().isDebug());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        JPushLib.releaseSDK();
    }
}
