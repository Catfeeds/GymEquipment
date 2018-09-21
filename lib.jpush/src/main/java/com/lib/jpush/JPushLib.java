package com.lib.jpush;

import android.app.Application;
import android.content.Context;

import com.lib.jpush.receiver.JPushMsgCenter;
import com.lib.jpush.utils.JPushUtil;

import java.util.HashSet;
import java.util.Iterator;

import cn.jpush.android.api.DefaultPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import static com.lib.jpush.receiver.TagAliasOperatorHelper.ACTION_ADD;
import static com.lib.jpush.receiver.TagAliasOperatorHelper.ACTION_DELETE;
import static com.lib.jpush.receiver.TagAliasOperatorHelper.ACTION_SET;
import static com.lib.jpush.receiver.TagAliasOperatorHelper.TagAliasBean;
import static com.lib.jpush.receiver.TagAliasOperatorHelper.getInstance;

/**
 * Created by siwei on 2018/4/7.
 */

public class JPushLib {

    public static final String TAG = "JPushLib";
    private Context mContext;
    private int sequence = 0x100;
    private static JPushLib INSTANCE;
    private static boolean hasInited;
    private boolean isDebug;

    private JPushLib(Context context, boolean isDebug) {
        mContext = context;
        this.isDebug = isDebug;
    }

    /**
     * 初始化SDK
     */
    public static void initSdk(Application application, boolean isDebug) {
        if (!hasInited) {
            JLogger.isDebug(isDebug);
            JLogger.d(TAG, "JPushLib initSdk");
            JPushInterface.setDebugMode(isDebug);
            JPushInterface.init(application);
            INSTANCE = new JPushLib(application, isDebug);
            hasInited = true;
        }
    }

    public static JPushLib instance() {
        if (INSTANCE == null)
            throw new NullPointerException("JPushLib has not been initialized, please call JPushLib.initSdk(application, isDebug) initialize");
        return INSTANCE;
    }

    /**
     * 判断当前是否为调试模式
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * 停止推送
     */
    public void stopJPush() {
        JLogger.d(TAG, "JPushLib stopJPush");
        if (!JPushInterface.isPushStopped(mContext)) {
            JPushInterface.stopPush(mContext);
        }
    }

    /**
     * 继续推送
     */
    public void resumeJPush() {
        JLogger.d(TAG, "JPushLib resumeJPush");
        if (JPushInterface.isPushStopped(mContext)) {
            JPushInterface.resumePush(mContext);
        }
    }

    /**
     * 添加别名
     */
    public boolean setAlias(String alias) {
        if (!JPushUtil.isValidTagAndAlias(alias)){
            JLogger.d(TAG, alias+" not is valid Tag or alias, set alias "+alias+" faild");
            return false;
        }
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.isAliasAction = true;
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.alias = alias;
        sequence ++;
        getInstance().handleAction(mContext, sequence, tagAliasBean);
        JLogger.d(TAG, "set alias "+alias+" success");
        return true;
    }

    /**
     * 删除别名
     */
    public boolean deleteAlias() {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.isAliasAction = true;
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.alias = "account_login_out";
        sequence ++;
        getInstance().handleAction(mContext, sequence, tagAliasBean);
        JLogger.d(TAG, "delete alias success");
        return false;
    }

    /**
     * 添加别名
     */
    public boolean addTags(HashSet<String> tags) {
        if(tags == null)return false;
        Iterator<String> iterator = tags.iterator();
        while(iterator.hasNext()){
            String tag = iterator.next();
            if (!JPushUtil.isValidTagAndAlias(tag)){
                iterator.remove();
                JLogger.d(TAG, tag+" not is valid Tag, remove tag "+tag);
                return false;
            }
        }
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.isAliasAction = false;
        tagAliasBean.action = ACTION_ADD;
        tagAliasBean.tags = tags;
        sequence ++;
        getInstance().handleAction(mContext, sequence, tagAliasBean);
        JLogger.d(TAG, "add tags success");
        return true;
    }

    /**移除别名*/
    public boolean removeTags(HashSet<String> tags){
        if(tags == null)return false;
        Iterator<String> iterator = tags.iterator();
        while(iterator.hasNext()){
            String tag = iterator.next();
            if (!JPushUtil.isValidTagAndAlias(tag)){
                iterator.remove();
                JLogger.d(TAG, tag+" not is valid Tag, remove tag "+tag);
                return false;
            }
        }
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.isAliasAction = false;
        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.tags = tags;
        sequence ++;
        getInstance().handleAction(mContext, sequence, tagAliasBean);
        JLogger.d(TAG, "remove tags success");
        return true;
    }

    public void setDefaultPushNotificationBuilder(DefaultPushNotificationBuilder builder){
        if(builder != null)JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

    public void setPushNotificationBuilder(Integer pushId, DefaultPushNotificationBuilder builder){
        if(builder != null)JPushInterface.setPushNotificationBuilder(pushId, builder);
    }

    /**
     * 释放SDK
     */
    public static void releaseSDK() {
        JLogger.d(TAG, "JPushLib releaseSDK");
        JPushMsgCenter.instance().release();
    }
}
