package com.saiyi.gymequipment.user.model.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Account extends DataSupport implements Serializable {

    private String uname;
    private String upwd;


    public Account(String uname, String upwd) {
        this.uname = uname;
        this.upwd = upwd;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

}
