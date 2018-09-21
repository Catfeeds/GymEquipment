package com.saiyi.gymequipment.common.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.equipment.model.bean.EquipmentPortType;
import com.saiyi.gymequipment.equipment.model.bean.FitnessCenter;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;

import java.util.List;

public class FitnessCenterHelper {

    private FitnessCenter fitnessCenter;       //正在操作的健身中心
    private List<EquipmentPortType> equipmentPortTypeList; //健身器材类型
    private boolean isEditModel = false;        //当前是不是编辑模式,默认是新建模式，不是编辑模式
    private GetFitnessCenter getFitnessCenter;   //要编辑的健身中心

    private static FitnessCenterHelper INSTANCE;

    public static FitnessCenterHelper instance() {
        if (INSTANCE == null) {
            synchronized (FitnessCenterHelper.class) {
                INSTANCE = new FitnessCenterHelper();
            }
        }
        return INSTANCE;
    }

    //获取当前要编辑的健身中心
    public GetFitnessCenter getGetFitnessCenter() {
        if(getFitnessCenter == null){
            getFitnessCenter = new GetFitnessCenter();
        }
        return getFitnessCenter;
    }

    //设置当前要编辑的健身中心
    public void setGetFitnessCenter(GetFitnessCenter getFitnessCenter) {
        this.getFitnessCenter = getFitnessCenter;
    }

    //设置添加健身中心
    public void setAddFitnesscenter(@NonNull FitnessCenter fitnesscenter) {
        this.fitnessCenter = fitnesscenter;
    }

    //获取当前操作的健身中心
    public @Nullable
    FitnessCenter getFitnessCenter() {
        if(fitnessCenter == null){
            fitnessCenter = new FitnessCenter();
        }
        return fitnessCenter;
    }

    //当前是否是编辑模式
    public boolean isEditModel() {
        return isEditModel;
    }

    //设置是否是编辑模式
    public void setEditModel(boolean editModel) {
        isEditModel = editModel;
    }

    //退出操作
    public void exitOperation() {
        this.fitnessCenter = null;
        this.getFitnessCenter = null;
        this.isEditModel = false;
    }

    public synchronized @Nullable
    Equipment getEquipmentByEMac(String eMac) {
        if (TextUtils.isEmpty(eMac)) return null;
        for (Equipment equipment : fitnessCenter.getEquipment()) {
            if (eMac.equals(equipment.getEmac())) return equipment;
        }
        return null;
    }

    public List<EquipmentPortType> getEquipmentPortTypeList() {
        return equipmentPortTypeList;
    }

    public void setEquipmentPortTypeList(List<EquipmentPortType> equipmentPortTypeList) {
        this.equipmentPortTypeList = equipmentPortTypeList;
    }
}
