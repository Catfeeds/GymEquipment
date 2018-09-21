package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.app.GymBuildConfig;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.home.adapter.SpaceItemDecoration;
import com.saiyi.gymequipment.run.adapter.FootpathAdapter;
import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.presenter.FootpathPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libmap.LocationBean;
import com.saiyi.libmap.LocationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 步道
 */
public class FootpathActivity extends BKMVPActivity<FootpathPresenter> implements LocationUtil.LocationInfoListener {

    private static String TAG = FootpathActivity.class.getSimpleName();

    @BindView(R.id.iv_search)
    ImageView iv_search;

    @BindView(R.id.search_et)
    EditText search_et;

    @BindView(R.id.fitness_center_ll)
    LinearLayout fitness_center_ll;

    @BindView(R.id.rl_footpath_list)
    RecyclerView rl_footpath_list;

    @BindView(R.id.tv_add_footpath)
    TextView tv_add_footpath;

    @BindView(R.id.mode_iv)
    ImageView mode_iv;

    private FootpathAdapter mFootpathAdapter;
    private ArrayList<FootpathBean> list;

    public static final String FOOT_PATH_INFORMATION_LIST = "footpath_information_list";
    public final static String FOOTPATH_INFORMATION_FOR_RUNNING_BEGIN = "footpath_information_for_running_begin";

    private LocationBean mLocationBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footpath);
    }

    @Override
    protected void initView() {
        super.initView();

        mTitleBar.setTitle(R.string.foot_path);
        mTitleBar.setRightImageResource(R.drawable.nav_add);

        showProgressDialog();

        if (UserHelper.instance().getUser().getIsAuthorize() == GymBuildConfig.isAdmin) {
            mTitleBar.showRightIcon();
            tv_add_footpath.setText(R.string.add_foot_path);
        }

        if (UserHelper.instance().getUser().getIsAuthorize() == GymBuildConfig.isUser) {
            mTitleBar.hiddenRightIcon();
            tv_add_footpath.setText(R.string.no_footpath);
        }

        Log.e(TAG, "开始定位");
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(this);
        LocationUtil.getInstance(GymApplication.getContext()).startLoaction();

        list = new ArrayList<>();

        mFootpathAdapter = new FootpathAdapter(this, R.layout.adapter_fitness_center, list);
        mFootpathAdapter.setItemDeleteClick(onItemDeleteClick);
        mFootpathAdapter.setOnItemClickListener(onItemClickListener);
        rl_footpath_list.setLayoutManager(new LinearLayoutManager(this));
        rl_footpath_list.addItemDecoration(new SpaceItemDecoration(20));
        rl_footpath_list.setAdapter(mFootpathAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationBean != null){
            getPresenter().getFootpathInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
        }
    }

    /**
     * 删除监听
     */
    protected FootpathAdapter.OnItemDeleteClick onItemDeleteClick = new FootpathAdapter.OnItemDeleteClick() {

        @Override
        public void onDeleteClick(FootpathBean item, int position) {
            getPresenter().deleteFootpath(item.getIdTrail(), position);
        }
    };

    /**
     * 点击监听
     */
    protected BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            FootpathBean footpathBean = list.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(FOOTPATH_INFORMATION_FOR_RUNNING_BEGIN, footpathBean);
            openActivity(RunningActivity.class, bundle);
        }
    };

    /**
     * 获取步道列表
     *
     * @param footpathBeanList
     */
    public void getFootpathInfo(List<FootpathBean> footpathBeanList) {
        dismissProgressDialog();
        list.clear();
        if (footpathBeanList != null && footpathBeanList.size() > 0) {
            list.addAll(footpathBeanList);
            showData(-100);
        } else {
            nothingToShow();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTitleBar.setClickListener(navBarOnClickListener);

        search_et.addTextChangedListener(new TextWatcher() {
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
                        tv_add_footpath.setText(getString(R.string.location_fail));
                    } else {
                        getPresenter().getFootpathInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), s.toString());
                    }
                } else {
                    getPresenter().getFootpathInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
                }
            }
        });
    }

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
            openActivity(AddFootpathActivity.class);
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };

    @Override
    public FootpathPresenter initPresenter(Context context) {
        return new FootpathPresenter(context);
    }

    /**
     * 添加了步道  显示步道列表
     */
    public void showData(int position) {
        fitness_center_ll.setVisibility(View.VISIBLE);
        tv_add_footpath.setVisibility(View.GONE);

        if (list != null && list.size() > 0) {
            if (position >= 0 && position < list.size()) {
                list.remove(position);
                if (list.size() == 0) {
                    nothingToShow();
                } else {
                    mFootpathAdapter.setNewData(list);
                }
            } else {
                mFootpathAdapter.setNewData(list);
            }
        }
    }

    /**
     * 删除失败
     *
     * @param message
     */
    public void deleteFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 没有添加步道 显示未添加步道
     */
    private void nothingToShow() {
        fitness_center_ll.setVisibility(View.GONE);
        tv_add_footpath.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void location(LocationBean bean) {
        Log.e(TAG, "定位成功，开始请求后台数据");
        getPresenter().getFootpathInfo(bean.getLatitude(), bean.getLongitude(), "");
        this.mLocationBean = bean;
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
//        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @Override
    public void locationFail(String message) {
        nothingToShow();
        tv_add_footpath.setText(getString(R.string.location_fail));
        dismissProgressDialog();
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
//        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @OnClick({R.id.mode_iv})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FOOT_PATH_INFORMATION_LIST, list);
        openActivity(FootpathMapActivity.class, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }
}
