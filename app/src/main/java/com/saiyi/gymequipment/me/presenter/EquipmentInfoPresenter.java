package com.saiyi.gymequipment.me.presenter;

import android.content.Context;

import com.saiyi.gymequipment.equipment.model.bean.Equipment;
import com.saiyi.gymequipment.me.model.EquipmentInfoModel;
import com.saiyi.gymequipment.me.model.bean.EquipmentInfo;
import com.saiyi.gymequipment.me.ui.EquipmentInfoActivity;
import com.saiyi.libfast.http.BaseResponseListener;
import com.saiyi.libfast.http.exception.ErrorStatus;
import com.saiyi.libfast.mvp.PresenterImpl;

import java.util.List;

/**
 * Created by JingNing on 2018-08-28 09:58
 */
public class EquipmentInfoPresenter extends PresenterImpl<EquipmentInfoActivity, EquipmentInfoModel> {

    public EquipmentInfoPresenter(Context context) {
        super(context);
    }

    @Override
    public EquipmentInfoModel initModel() {
        return new EquipmentInfoModel();
    }

    public void getEquipmentInfo(String mac){
        getView().showGetDataDialog();
        getModel().getEquipmentInfo(mac, new BaseResponseListener<List<EquipmentInfo>>(){
            @Override
            public void onResponse(List<EquipmentInfo> data) {
                super.onResponse(data);
                boolean isEpnumberOne = false;
                getView().dismissLoading();
                if(data == null || data.size()<1){
                    getView().showNotDevice();
                }else if(data.size()==1){
                    getView().showEquipmentInfo(data.get(0));   //只有一个端口号，及返回该设备
                }else{
                    for(EquipmentInfo info : data){
                        if(info.getEpnumber().equals("1")){ //如果有多个端口只显示端口号为1的
                            isEpnumberOne = true;
                            getView().showEquipmentInfo(info);
                        }
                    }
                    //有很多端口号，但就是没有端口号为1的设备，这时候显示列表第一个设备
                    if(!isEpnumberOne){
                        getView().showEquipmentInfo(data.get(0));
                    }
                }
            }

            @Override
            public void onFailed(ErrorStatus e) {
                super.onFailed(e);
                getView().dismissLoading();
            }
        });
    }
}
