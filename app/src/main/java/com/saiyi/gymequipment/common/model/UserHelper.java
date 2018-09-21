package com.saiyi.gymequipment.common.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.lib.jpush.JPushLib;
import com.saiyi.gymequipment.user.model.bean.Account;
import com.saiyi.gymequipment.user.model.bean.User;

/**
 * User Helper
 */
public class UserHelper {

    private static AccountHelper mAccountHelper;
    private static User mUser;
    private static UserHelper INSTANCE;

    public static UserHelper instance() {
        if (INSTANCE == null) {
            synchronized (UserHelper.class) {
                INSTANCE = new UserHelper();
            }
        }
        return INSTANCE;
    }

    private UserHelper() {
        mAccountHelper = AccountHelper.instance();
    }

    /**
     * 用户是否已有账号
     */
    public boolean hasAccount() {
        return mAccountHelper.hasAccount();
    }

    /**
     * 用户是否已登录
     */
    public boolean hasLogin() {
        return hasAccount() && mUser != null;
    }

    /**
     * 用户登出
     */
    public void loginOut() {
        JPushLib.instance().deleteAlias();
        mUser = null;
        mAccountHelper.loginOut();
    }

    /**
     * 自动登陆
     */
    public boolean autoLogin(String phone, String token, int isAuthorize) {
        if (token == null) return false;
        if (mUser == null) {
            mUser = new User(phone, token, isAuthorize);
        } else {
            mUser.setPhone(phone);
            mUser.setUtoken(token);
            mUser.setIsAuthorize(isAuthorize);
        }
        return JPushLib.instance().setAlias(phone);
    }

    public void setmUser(User user) {
        if (TextUtils.isEmpty(user.getUtoken())) {
            user.setUtoken(mUser.getUtoken());
        }
        if (TextUtils.isEmpty(user.getPhone())) {
            user.setPhone(mUser.getPhone());
        }
        mUser = user;
        JPushLib.instance().setAlias(mUser.getPhone());
    }

    /**
     * 用户登录
     */
    public boolean login(@NonNull Account account, @NonNull User user) {
        if (account == null || user == null) return false;
        if (mAccountHelper.login(account)) {
            mUser = user;
            if (user.getPic() != null && !user.getPic().startsWith("http")) {
                //避免返回的数据地址不完成,需要拼接一个完整的地址
                user.setPic(user.getPic());
            }
            return JPushLib.instance().setAlias(mUser.getPhone());
        }
        return false;
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {
        if (mUser == null) return null;
        return mUser.getUtoken();
    }

    /**
     * 获取用户
     */
    public User getUser() {
        return mUser;
    }

    public Account getAccount() {
        return mAccountHelper.getAccount();
    }

    /**
     * 更新用户头像
     */
    public void updateUserImg(String userImg) {
        if (mUser != null) {
            mUser.setPic(userImg);
//            EventBus.getDefault().post(Action.ACTION_UPDATE_USER_IMG);
        }
    }

    /**
     * 更新用户名称
     */
    public void updateUserName(String uname) {
        if (TextUtils.isEmpty(uname)) return;
        if (mUser != null) {
            mUser.setUnickname(uname);
//            EventBus.getDefault().post(Action.ACTION_UPDATE_USER_NAME);
        }
    }
}
