package com.saiyi.gymequipment.common.model;

import com.saiyi.gymequipment.user.model.bean.Account;

import org.litepal.crud.DataSupport;

/**
 * 账号帮助类
 */
public class AccountHelper {

    private static Account mAccount;
    private static AccountHelper INSTANCE;

    protected static AccountHelper instance() {
        if (INSTANCE == null) {
            synchronized (AccountHelper.class) {
                INSTANCE = new AccountHelper();
            }
        }
        return INSTANCE;
    }

    private AccountHelper() {
        mAccount = DataSupport.findFirst(Account.class);
    }

    public boolean hasAccount() {
        return mAccount != null;
    }

    protected String getAccountName() {
        if (mAccount != null) {
            return mAccount.getUname();
        }
        return null;
    }


    protected Account getAccount() {
        return mAccount;
    }

    /**
     * 登出
     */
    protected void loginOut() {
        DataSupport.deleteAll(Account.class);
        mAccount = null;
    }

    /**
     * 登陆
     */
    protected boolean login(Account account) {
        if (account != null) {
            DataSupport.deleteAll(Account.class);
            mAccount = account;
            return mAccount.save();
        }
        return false;
    }
}
