package com.saiyi.libfast.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.saiyi.libfast.activity.callback.ActivityLifeListener;
import com.saiyi.libfast.cache.ACache;
import com.saiyi.libfast.config.IBuildConfig;
import com.saiyi.libfast.http.HttpFactory;
import com.saiyi.libfast.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;


/**
 * Created by siwei on 2015/11/2.
 */
public abstract class BaseApplication extends LitePalApplication implements Thread.UncaughtExceptionHandler {

    private ActivityLifeListener mLifeListener;

    private static BaseApplication instance;

    public synchronized static BaseApplication getInstance() {
        return instance;
    }

    private static final String TAG = "BaseApplication";

    /**
     * 返回app配置项
     */
    public abstract @NonNull
    IBuildConfig getBuildConfig();

    @Override
    public void onCreate() {
        super.onCreate();
        onAppInit();
    }

    //App进来进行的一些初始化操作
    private void onAppInit() {
        instance = this;

        //初始化Logger
        Logger.init(getBuildConfig().getAppName(this)).hideThreadInfo().setMethodCount(3).setMethodOffset(2);

        //网络框架初始化
        HttpFactory.initFactory(this, getBuildConfig().getHttpBaseUrl());

        //Activity生命周期监听
        mLifeListener = new ActivityLifeListener();
        registerActivityLifecycleCallbacks(mLifeListener);

        //异常捕获
//        Thread.setDefaultUncaughtExceptionHandler(this);

        //LeakCanary初始化
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public ACache getCache() {
        return ACache.get(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!getBuildConfig().isDebug()) {
            System.exit(1);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(mLifeListener);
        mLifeListener.destory();
        mLifeListener = null;
    }


}
