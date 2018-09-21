package com.saiyi.gymequipment.common.action;

import android.os.Bundle;

import com.saiyi.libfast.event.EventAction;

/**
 * App内事件传播Action
 */
public enum Action implements EventAction {

    /**
     * 添加器材
     */
    ACTION_ADD_EQUIPMENT(0x100, "add_equipment"),
    /**
     * 登录成功
     */
    ACTION_LOGOUT(0x101, "logout"),
    /**
     * 添加健身中心
     */
    ACTION_ADD_FITNESS_CENTER(0x102, "add_fitness_center"),
    /**
     * 编辑健身中心
     */
    ACTION_EDIT_FITNESS_CENTER(0x103, "edit_fitness_center"),
    /**
     * 编辑健身中心时刷新设备列表
     */
    ACTION_REFRESH_DEVICE_LSIT(0x104, "action_refresh_device_lsit"),
    /**
     * 退出程序
     */
    ACTION_EXIT(0x105, "exit_app");;

    private int actionId;
    private String actionName;
    private Bundle actionData;

    Action(int actionId) {
        this.actionId = actionId;
    }

    Action(int actionId, String actionName) {
        this.actionId = actionId;
        this.actionName = actionName;
    }

    Action(int actionId, String actionName, Bundle actionData) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionData = actionData;
    }

    @Override
    public int getActionId() {
        return actionId;
    }

    @Override
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    @Override
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public Bundle getActionData() {
        return actionData;
    }

    @Override
    public void setActionData(Bundle actionData) {
        this.actionData = actionData;
    }
}
