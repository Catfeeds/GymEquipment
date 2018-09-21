package com.saiyi.gymequipment.home.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.presenter.MyPresenter;
import com.saiyi.gymequipment.me.ui.AppversionActivity;
import com.saiyi.gymequipment.me.ui.CustomerServiceActivity;
import com.saiyi.gymequipment.me.ui.EquipmentRepairActivity;
import com.saiyi.gymequipment.me.ui.FitnessGuidanceListActivity;
import com.saiyi.gymequipment.me.ui.HealthDataActivity;
import com.saiyi.gymequipment.me.ui.UserInforMationActivity;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.logger.Logger;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * 我fragement界面
 */

public class MyFragement extends BKMVPFragment<MyPresenter> {

    @BindView(R.id.title_bar)
    NavBar titleBar;

    @BindView(R.id.user_rl)
    RelativeLayout userRl;

    @BindView(R.id.user_head_iv)
    ImageView userHeadIv;
    @BindView(R.id.user_phone_number_tv)
    TextView userPhoneNumberTv;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.health_data_rl)
    RelativeLayout healthDataRl;
    @BindView(R.id.fitness_guidance_rl)
    RelativeLayout fitnessGuidanceRl;
    @BindView(R.id.customer_service_rl)
    RelativeLayout customerServiceRl;
    @BindView(R.id.appversion_rl)
    RelativeLayout appversionRl;

    public MyFragement() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MyFragement newInstance() {
        MyFragement fragment = new MyFragement();
        return fragment;
    }

    @Override
    public MyPresenter initPresenter(Context context) {
        return new MyPresenter(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_me, container, false);
        return view;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        titleBar.setTitle(R.string.me);
        titleBar.hiddenLeftIcon();
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getAppUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserHelper.instance().getUser() != null) {
            showUserData(UserHelper.instance().getUser());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.user_head_iv, R.id.user_rl, R.id.health_data_rl, R.id.fitness_guidance_rl, R.id.customer_service_rl, R.id.appversion_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_head_iv:
//                点击头像
                openActivity(UserInforMationActivity.class);
                break;
            case R.id.user_rl:
//                点击用户详细信息
                openActivity(UserInforMationActivity.class);
                break;
            case R.id.health_data_rl:
//                点击健康数据
                openActivity(HealthDataActivity.class);
                break;
            case R.id.fitness_guidance_rl:
//                点击健身指导
                openActivity(FitnessGuidanceListActivity.class);
                break;
            case R.id.customer_service_rl:
//                点击客户服务
                openActivity(CustomerServiceActivity.class);
                break;
            case R.id.appversion_rl:
//                点击版本信息
                openActivity(AppversionActivity.class);
                break;
        }
    }

    public void showUserData(User user) {
        if (user == null) return;
        if (!TextUtils.isEmpty(user.getPhone()))
            userPhoneNumberTv.setText(user.getPhone());
        if (!TextUtils.isEmpty(user.getUnickname()))
            userNameTv.setText(user.getUnickname());
        if (!TextUtils.isEmpty(user.getPic())) {
            CropCircleTransformation circle = (CropCircleTransformation) userHeadIv.getTag();
            if (circle == null) {
                circle = new CropCircleTransformation();
                userHeadIv.setTag(circle);
            }
            Logger.d("user.getPic()=" + user.getPic());
            Picasso.with(mContext).load(user.getPic()).placeholder(R.drawable.leftbar_info).error(R.drawable.leftbar_info).transform(circle).into(userHeadIv);
        } else {
            userHeadIv.setImageResource(R.drawable.leftbar_info);
        }
    }

}
