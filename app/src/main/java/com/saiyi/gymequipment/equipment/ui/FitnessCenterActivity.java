package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.common.model.FitnessCenterHelper;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.equipment.adapter.FitnessCenterAdapter;
import com.saiyi.gymequipment.equipment.model.bean.GetFitnessCenter;
import com.saiyi.gymequipment.equipment.presenter.FitnessCenterPresenter;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.home.adapter.SpaceItemDecoration;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.event.EventAction;
import com.saiyi.libmap.LocationBean;
import com.saiyi.libmap.LocationUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 健康中心列表
 */
public class FitnessCenterActivity extends BKMVPActivity<FitnessCenterPresenter> implements LocationUtil.LocationInfoListener {


    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.fitness_center_rv)
    RecyclerView fitnessCenterRv;
    @BindView(R.id.mode_iv)
    ImageView modeIv;
    @BindView(R.id.search_bar_ll)
    LinearLayout searchBarLl;
    @BindView(R.id.add_fc_tv)
    TextView addFcTv;
    @BindView(R.id.fitness_center_ll)
    LinearLayout fitnessCenterLl;

    public static final String FITNESSCENTER_INFORMATION_LIST = "fitnesscenter_information_list";

    private ArrayList<GetFitnessCenter> list;
    private FitnessCenterAdapter fitnessCenterAdapter;
    private RemindDialog mRemindDialog;
    private GetFitnessCenter deleteGetFC;
    private LocationBean mLocationBean;

    @Override
    public FitnessCenterPresenter initPresenter(Context context) {
        return new FitnessCenterPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_center);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.fitness_center);
        if (UserHelper.instance().getUser().isAdmin()) {
            mTitleBar.setRightImageResource(R.drawable.nav_add);
            mTitleBar.showRightIcon();
            addFcTv.setText(R.string.click_add_fitness_center);
        } else {
            mTitleBar.hiddenRightIcon();
            addFcTv.setText(R.string.nearby_not_fitness_center);
        }

        showProgressDialog();

        LocationUtil.getInstance(GymApplication.getContext()).startLoaction();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(this);

        registerEventBus();

        list = new ArrayList<GetFitnessCenter>();
        fitnessCenterAdapter = new FitnessCenterAdapter(R.layout.adapter_fitness_center, list);
        fitnessCenterAdapter.setItemDeleteClick(onItemDeleteClick);
        fitnessCenterAdapter.setOnItemClickListener(onItemClickListener);
        fitnessCenterRv.setLayoutManager(new LinearLayoutManager(this));
        fitnessCenterRv.addItemDecoration(new SpaceItemDecoration(20));
        fitnessCenterRv.setAdapter(fitnessCenterAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLocationBean != null) {
            getPresenter().getFitnessCenterInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
        } else {
            LocationUtil.getInstance(GymApplication.getContext()).startLoaction();
        }
    }

    public void showDeleteFCing() {
        showCustomLoading(getString(R.string.deleteing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 获附近的健身中心
     *
     * @param getFitnessCenters
     */
    public void getFitnessCenterInfo(List<GetFitnessCenter> getFitnessCenters) {
        dismissProgressDialog();
        list.clear();
        if (getFitnessCenters != null && getFitnessCenters.size() > 0) {
            list.addAll(getFitnessCenters);
            showData(-100);
        } else {
            nothingToShow();
        }
    }

    private void nothingToShow() {
        addFcTv.setVisibility(View.VISIBLE);
        fitnessCenterLl.setVisibility(View.GONE);
    }

    private void showData(int position) {
        addFcTv.setVisibility(View.GONE);
        fitnessCenterLl.setVisibility(View.VISIBLE);

        if (list != null && list.size() > 0) {
            if (position >= 0 && position < list.size()) {
                list.remove(position);
                fitnessCenterAdapter.setNewData(list);
            } else {
                fitnessCenterAdapter.setNewData(list);
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTitleBar.setClickListener(navBarOnClickListener);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s && s.length() > 0) {
                    if (mLocationBean == null) {
                        nothingToShow();
                        addFcTv.setText(getString(R.string.location_fail));
                    } else {
                        getPresenter().getFitnessCenterInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), s.toString());
                    }
                } else {
                    if (mLocationBean != null) {
                        getPresenter().getFitnessCenterInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
                    }
                }
            }
        });
    }

    final protected BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            FitnessCenterHelper.instance().setEditModel(true);//进入编辑模式
            GetFitnessCenter center = fitnessCenterAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.BUNDLE_KEY_DATA, center);
            openActivity(AddEditFitnessCenterActivity.class, bundle);
        }
    };

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
            back();
        }

        @Override
        public void onLeftSenIconClick(View view) {
        }

        @Override
        public void onLeftTxtClick(View view) {
        }

        @Override
        public void onRightIconClick(View view) {
            openActivity(AddEditFitnessCenterActivity.class);
        }

        @Override
        public void onRightTxtClick(View view) {
        }
    };

    protected FitnessCenterAdapter.OnItemDeleteClick onItemDeleteClick = new FitnessCenterAdapter.OnItemDeleteClick() {
        @Override
        public void onDeleteClick(GetFitnessCenter item) {
            deleteFitnessCenter(item);
        }
    };

    private void deleteFitnessCenter(GetFitnessCenter item) {
        deleteGetFC = item;
        showDeleteFitnessCenterDialog(item);
    }

    //删除设备弹窗
    private void showDeleteFitnessCenterDialog(GetFitnessCenter item) {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(String.format(getResources().getString(R.string.delete_dev_tips), item.getFcdefinition()));
        dialog.setMsgText("");
        dialog.setComplateTextColorRes(R.color.red_ff3d3d);
        dialog.setClick(mDeleteDeviceDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mDeleteDeviceDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    if (deleteGetFC != null) {
                        getPresenter().deleteFitness(deleteGetFC.getIdFitnessCenter().intValue());
                    }
                    deleteGetFC = null;
                }
            }
        }
    };

    public void deleteFitnessCenterSuccess(String msg) {
        toast(msg);
        if (mLocationBean != null) {
            getPresenter().getFitnessCenterInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
        }
    }

    public void deleteFitnessCenterFaild(String msg) {
        toast(msg);
    }


    //弹窗消失
    private void dismissRemindDialog() {
        if (mRemindDialog != null && mRemindDialog.isShowing()) {
            mRemindDialog.dismiss();
        }
    }

    @Override
    public void location(LocationBean bean) {
        getPresenter().getFitnessCenterInfo(bean.getLatitude(), bean.getLongitude(), "");
        this.mLocationBean = bean;
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
//        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @Override
    public void locationFail(String message) {
        nothingToShow();
        addFcTv.setText(getString(R.string.location_fail));
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
//        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @OnClick(R.id.mode_iv)
    protected void onClickModeIV(View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable(FITNESSCENTER_INFORMATION_LIST, list);
        openActivity(FitnessCenterMapActivity.class, bundle);
    }

    @Override
    public void onMessageEvent(EventAction event) {
        super.onMessageEvent(event);
        if (event == Action.ACTION_ADD_FITNESS_CENTER || event == Action.ACTION_EDIT_FITNESS_CENTER) {
            if (mLocationBean != null) {
                getPresenter().getFitnessCenterInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }
}
