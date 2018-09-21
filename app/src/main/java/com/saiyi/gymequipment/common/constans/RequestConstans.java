package com.saiyi.gymequipment.common.constans;

/**
 * Created by Administrator on 2018/3/19.
 * 请求接口常量
 */

public class RequestConstans {

    /**-----------------------------验证码类型----------------------------------*/
    /**注册*/
    public static final int GET_IDENTIFY_TYPE_REGISTER = 0;
    /**找回密码*/
    public static final int GET_IDENTIFY_TYPE_FIND_BACK_PWD = 1;
    /**-----------------------------验证码类型----------------------------------*/

    /**-----------------------------查询运动数据 类型----------------------------------*/
    /**天的运动量*/
    public static final int GET_TIMESRECORDS_TYPE_DAY = 1;
    /**周的运动量*/
    public static final int GET_TIMESRECORDS_TYPE_WEEK = 2;
    /**月的运动量*/
    public static final int GET_TIMESRECORDS_TYPE_MONTH = 3;
    /**总的运动量*/
    public static final int GET_TIMESTOTALRECORDS_TYPE_ALL = 4;
    /**-----------------------------查询运动数据 类型----------------------------------*/


    /**验证码的长度*/
    public static final int IDENTIFY_CODE_LENGTH = 6;
}
