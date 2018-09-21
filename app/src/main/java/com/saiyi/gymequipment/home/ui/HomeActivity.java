package com.saiyi.gymequipment.home.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.listener.OnPageSelectedListener;
import com.saiyi.gymequipment.common.view.CheckImageView;
import com.saiyi.gymequipment.home.presenter.HomePresenter;
import com.saiyi.gymequipment.user.ui.LoginActivity;
import com.saiyi.libfast.adapter.PagerAdapter;
import com.saiyi.libfast.event.EventAction;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


public class HomeActivity extends BKMVPActivity<HomePresenter> implements View.OnClickListener {


    @BindView(R.id.home_vp)
    ViewPager homeVp;
    @BindView(R.id.menu_ll)
    LinearLayout menuLl;

    private List<View> tableViews;
    private SparseArray<Fragment> fragments = new SparseArray<>();
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
//                        0);//申请权限
//            }
//        }

    }


    @Override
    public HomePresenter initPresenter(Context context) {
        return null;
    }


    @Override
    protected void initView() {
        super.initView();
        hiddenTitleBar();
        initTabs();
        registerEventBus();
    }

    private void initTabs() {
        tableViews = Arrays.asList(findViewById(R.id.tab1_ll),
                findViewById(R.id.tab2_ll),
                findViewById(R.id.tab3_ll));
        for (View tabView : tableViews) {
            tabView.setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        getTitleBar().setTitle(getString(R.string.body_build));
        initFragments();
    }

    private void initFragments() {
        fragments.append(0, EquipmentFragement.newInstance());
        fragments.append(1, RunningFragement.newInstance());
        fragments.append(2, MyFragement.newInstance());
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        homeVp.setOffscreenPageLimit(fragments.size());
        homeVp.setAdapter(pagerAdapter);
        homeVp.addOnPageChangeListener(mOnPageChangeListener);
        setTabSelected(0);
    }

    OnPageSelectedListener mOnPageChangeListener = new OnPageSelectedListener() {
        @Override
        public void onPageSelected(int position) {
            setTabSelected(position);
        }
    };

    private void setTabSelected(int position) {
        if (tableViews == null) return;
        for (int i = 0; i < tableViews.size(); i++) {
            boolean isChecked = (i == position);
            ViewGroup tabView = (ViewGroup) tableViews.get(i);
            CheckImageView tableImgCk = (CheckImageView) tabView.getChildAt(0);
            TextView tableNameTv = (TextView) tabView.getChildAt(1);
            tableImgCk.setChecked(isChecked);
            tableNameTv.setEnabled(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1_ll:
            case R.id.tab2_ll:
            case R.id.tab3_ll:
                int pos = tableViews.indexOf(v);
                homeVp.setCurrentItem(pos);
                break;
        }
    }

    @Override
    public void onMessageEvent(EventAction event) {
        super.onMessageEvent(event);
        if (event == Action.ACTION_LOGOUT) {
            back();
            openActivity(LoginActivity.class);
        }else if(event == Action.ACTION_EXIT){
            back();
        }
    }

    /**
     * 按两次退出
     */
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                toast(getString(R.string.double_exit));
                mExitTime = System.currentTimeMillis();
            } else {
                send(Action.ACTION_EXIT);
                System.exit(0);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}