package com.saiyi.gymequipment.home.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.RoundTextView;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.equipment.ui.FitnessCenterActivity;
import com.saiyi.gymequipment.equipment.ui.FitnessRecordActivity;
import com.saiyi.gymequipment.equipment.ui.RankingsActivity;
import com.saiyi.gymequipment.home.adapter.MenuListViewAdapter;
import com.saiyi.gymequipment.home.adapter.PicassoImageLoader;
import com.saiyi.gymequipment.home.model.bean.BannerBean;
import com.saiyi.gymequipment.home.model.bean.BannerImgBean;
import com.saiyi.gymequipment.home.presenter.EquipmentPresenter;
import com.saiyi.libble.BluetoothHelper;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.logger.Logger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class EquipmentFragement extends BKMVPFragment<EquipmentPresenter> implements AdapterView.OnItemClickListener {

    private static final int AD_DELAY_TIME = 3000;// 默认广告轮播时长

    @BindView(R.id.title_bar)
    NavBar titleBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.menu_lv)
    ListView menuLv;
    @BindView(R.id.content_sv)
    ScrollView contentSv;
    @BindView(R.id.frequency_rtv)
    RoundTextView frequencyRtv;
    @BindView(R.id.energy_consumed_rtv)
    RoundTextView energyConsumedRtv;
    @BindView(R.id.time_length_rtv)
    RoundTextView timeLengthRtv;

    private RemindDialog mRemindDialog;

    public EquipmentFragement() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static EquipmentFragement newInstance() {
        EquipmentFragement fragment = new EquipmentFragement();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_equipment, container, false);
        return view;
    }

    @Override
    public EquipmentPresenter initPresenter(Context context) {
        return new EquipmentPresenter(context);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        titleBar.setTitle(R.string.body_build);
        titleBar.hiddenLeftIcon();
        titleBar.noEndLine();
        initMenuLv();
        if (!BluetoothHelper.getInstance(getContext()).isBlueToothOpened()) {
            showBlueToothSetting();
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }


    /**
     * 初始化广告轮播
     */
    private void initBanner(int delayTime, List<String> images) {
        banner.setIndicatorGravity(BannerConfig.RIGHT)
                .setDelayTime(delayTime)
                .setImages(images)
                .setImageLoader(new PicassoImageLoader())
                .start();
    }

    /**
     * 初始化菜单列表
     */
    private void initMenuLv() {
        menuLv.setAdapter(new MenuListViewAdapter(getContext()));
        menuLv.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().getTotalData();
        getPresenter().getBanner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        banner.stopAutoPlay();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case MenuListViewAdapter.FITNESS_CENTER:
                //点击健康中心
                openActivity(FitnessCenterActivity.class);
                break;
            case MenuListViewAdapter.FITNESS_HISTORY:
                //点击健身历史
                openActivity(FitnessRecordActivity.class);
                break;
            case MenuListViewAdapter.RANDKINGS:
                //点击排行榜
                openActivity(RankingsActivity.class);
                break;
//            case MenuListViewAdapter.BLUETOOTH:
//                //点击蓝牙
//                if (BluetoothHelper.getInstance(getContext()).isBlueToothOpened()) {
//                    showConnectBlueTooth();
//                } else {
//                    showBlueToothSetting();
//                }
//                break;
            case MenuListViewAdapter.SCAN:
                //点击扫码
                Bundle bundle = new Bundle();
                bundle.putString(ScanByQRActivity.BUNDLE_KEY_TITLE, getString(R.string.scan_code_result));
                bundle.putString(ScanByQRActivity.BUNDLE_KEY_MESSAGE, getString(R.string.qrcode_tips));
                bundle.putInt(ScanByQRActivity.BUNDLE_KEY_TYPE, ScanByQRActivity.BUNDLE_VALUE_TYPE_BODYBUILD);
                openActivityForResult(ScanByQRActivity.class, bundle, ScanByQRActivity.RESULT_SCAN_REQUEST);
                break;
        }
    }

    private void showConnectBlueTooth() {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(getContext());
        mRemindDialog = dialog;
        dialog.setTitleText(getString(R.string.tips));
        dialog.setMsgText(getString(R.string.bluetooth_not_opened_tips1));
        dialog.setComplateTextColorRes(R.color.colorAccent).setComplateText(getString(R.string.go_connect));
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    //去打开蓝牙
    private void showBlueToothSetting() {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(getContext());
        mRemindDialog = dialog;
        dialog.setTitleText(getString(R.string.bluetooth_not_opened));
        dialog.setMsgText(getString(R.string.bluetooth_not_opened_tips));
        dialog.setComplateTextColorRes(R.color.colorAccent).setComplateText(getString(R.string.go_open));
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mOpenSettingDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    //去开启蓝牙
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //弹窗消失
    private void dismissRemindDialog() {
        if (mRemindDialog != null && mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
    }

    public void getTotalDataSuccess(int times, double consume, double duration) {
        frequencyRtv.setContentText(times + "");
        energyConsumedRtv.setContentText(consume + "");
        timeLengthRtv.setContentText(StringUtils.secToMinute(duration) + "");
    }

    public void getTotalDataFaild(String msg) {
        toast(msg);
    }

    public void getBannerSuccess(BannerBean bean) {
        int dalayTime = AD_DELAY_TIME;
        if (bean.getPctime() != null) {
            dalayTime = bean.getPctime().intValue() * 1000;//秒转毫秒
            if (dalayTime < 1000) {    //小于1s使用默认值
                dalayTime = AD_DELAY_TIME;
            }
        }
        List<BannerImgBean> imgBeans = bean.getCarouselImgs();
        if (imgBeans != null) {
            List<String> images = new ArrayList<String>();
            for (BannerImgBean imgBean : imgBeans) {
                if (imgBean.getPciimg() != null) {
                    images.add(imgBean.getPciimg());
                }
            }
            initBanner(dalayTime, images);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanByQRActivity.RESULT_SCAN_REQUEST && data != null) {
            openActivity(BodybuildingActivity.class, data.getExtras());
        }
    }
}
