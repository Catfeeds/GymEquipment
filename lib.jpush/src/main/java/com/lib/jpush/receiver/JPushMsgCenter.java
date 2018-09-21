package com.lib.jpush.receiver;

import android.content.Context;
import android.content.Intent;

import com.lib.jpush.receiver.listener.JPushMsgListener;
import com.lib.jpush.receiver.listener.JPushOperatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siwei on 2018/4/8.
 */

public class JPushMsgCenter {

    private static JPushMsgCenter INSTANCE;
    private List<JPushMsgListener> mMsgListeners;
    private List<JPushOperatorListener> mOperatorListener;

    private JPushMsgCenter() {
        mMsgListeners = new ArrayList<>();
        mOperatorListener = new ArrayList<>();
    }

    public static JPushMsgCenter instance() {
        if (INSTANCE == null) {
            synchronized (JPushMsgCenter.class) {
                INSTANCE = new JPushMsgCenter();
            }
        }
        return INSTANCE;
    }

    public void addOperatorListener(JPushOperatorListener listener) {
        if (listener != null && !mOperatorListener.contains(listener)) {
            mOperatorListener.add(listener);
        }
    }

    public void removeOperatorListener(JPushOperatorListener listener) {
        if (mOperatorListener.contains(listener)) {
            mOperatorListener.remove(listener);
        }
    }

    protected void dispatchOnSetAliasSuccess() {
        for (JPushOperatorListener listener : mOperatorListener) {
            try {
                listener.onSetAliasSuccess();
            } catch (Exception e) {
            }
        }
    }

    protected void dispatchOnSetAliasFaild() {
        for (JPushOperatorListener listener : mOperatorListener) {
            try {
                listener.onSetAliasFaild();
            } catch (Exception e) {
            }
        }
    }

    public void addReceiver(JPushMsgListener listener) {
        if (listener != null && !mMsgListeners.contains(listener)) {
            mMsgListeners.add(listener);
        }
    }

    public void removeReceiver(JPushMsgListener listener) {
        if (mMsgListeners.contains(listener)) {
            mMsgListeners.remove(listener);
        }
    }


    protected void dispatchOnRegistrationID(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onRegistrationID(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnMsgReceived(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onMsgReceived(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnNotificationReceived(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onNotificationReceived(context, intent);
            } catch (Exception e) {
            }
        }
    }

    protected void dispatchOnNotificationOpened(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onNotificationOpened(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnRitchpushCallBack(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onRitchpushCallBack(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnConnectionChange(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onConnectionChange(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnOther(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onOther(context, intent);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 释放
     */
    public void release() {
        mMsgListeners.clear();
        mMsgListeners = null;
        mOperatorListener .clear();
        mOperatorListener = null;
        INSTANCE = null;
    }

}
