package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.activity.ScanByQRActivity;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.home.adapter.SpaceItemDecoration;
import com.saiyi.gymequipment.run.adapter.BroadcastAdapter;
import com.saiyi.gymequipment.run.event.BroadcastEvent;
import com.saiyi.gymequipment.run.presenter.BroadcastPresenter;
import com.saiyi.libfast.activity.view.NavBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 廣播包列表頁面
 */
public class BroadcastActivity extends BKMVPActivity<BroadcastPresenter> {

    @BindView(R.id.rl_broadcast_list)
    RelativeLayout rl_broadcast_list;

    @BindView(R.id.broadcast_info)
    RecyclerView broadcast_info;

    @BindView(R.id.tv_no_show)
    TextView tv_no_show;

    @BindView(R.id.tv_broadcast_add_finish)
    TextView tv_broadcast_add_finish;


    private ArrayList<BroadcastEvent> broadcastList;
    private BroadcastAdapter mBroadcastAdapter;

    public static final int RESULT_SCAN_BROADCAST = 22222;
    public static final int ADD_BROADCAST_RESULT = 1234;
    public static final String ADD_RESULT_FOR_BROADCAST = "add_result_for_broadcast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tv_broadcast_add_finish})
    public void onClick(View view) {
        if (broadcastList == null && broadcastList.size() == 0) {
            Toast.makeText(BroadcastActivity.this, getString(R.string.please_add_broadcast), Toast.LENGTH_SHORT).show();
        } else {
            getIntent().putExtra(ADD_RESULT_FOR_BROADCAST, broadcastList);
            setResult(ADD_BROADCAST_RESULT, getIntent());
            back();
        }
    }

    @Override
    public BroadcastPresenter initPresenter(Context context) {
        return new BroadcastPresenter(context);
    }

    @Override
    protected void initView() {
        mTitleBar.setTitle(R.string.broadcast_page);
        mTitleBar.setRightImageResource(R.drawable.nav_add);
        mTitleBar.showRightIcon();
        mTitleBar.setClickListener(navBarOnClickListener);
        hasBroadcast(false);
        broadcastList = new ArrayList<>();
        mBroadcastAdapter = new BroadcastAdapter(this, R.layout.adapter_fitness_center, broadcastList);
        mBroadcastAdapter.setItemDeleteClick(onItemDeleteClick);
        broadcast_info.setLayoutManager(new LinearLayoutManager(this));
        broadcast_info.addItemDecoration(new SpaceItemDecoration(20));
        broadcast_info.setAdapter(mBroadcastAdapter);

        if (getIntent() != null) {
            ArrayList<BroadcastEvent> events = (ArrayList<BroadcastEvent>) getIntent().getExtras().getSerializable(AddFootpathActivity.ALREADY_ADD_BROADCAST);
            if (null != events && events.size() > 0) {
                broadcastList.addAll(events);
                mBroadcastAdapter.setNewData(broadcastList);
            }
        }
    }

    /**
     * 删除监听
     */
    protected BroadcastAdapter.OnItemDeleteClick onItemDeleteClick = new BroadcastAdapter.OnItemDeleteClick() {

        @Override
        public void onDeleteClick(BroadcastEvent item, final int position) {
            getPresenter().showCancelSetting(getString(R.string.delete_broadcast), getString(R.string.is_delete_broadcast), new BaseDialog.OnDialogClick() {

                @Override
                public void onClick(int whichOne) {
                    if (whichOne == RemindDialog.WHICH_COMPLATE) {
                        if (getPresenter().mRemindDialog != null && getPresenter().mRemindDialog instanceof RemindDialog) {
                            if (null != broadcastList && broadcastList.size() > position) {
                                broadcastList.remove(position);
                                mBroadcastAdapter.setNewData(broadcastList);
                            }
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventMainThread(BroadcastEvent event) {
        if (event != null) {
            if (broadcastList != null && broadcastList.size() > 0) {
                for (BroadcastEvent broadcastEvent : broadcastList) {
                    if (broadcastEvent.getTbpmac().equals(event.getTbpmac())) {
                        Toast.makeText(this, getString(R.string.please_not_add_again), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        broadcastList.add(event);
                        hasBroadcast(true);
                        mBroadcastAdapter.setNewData(broadcastList);
                    }
                }
            }


            if (broadcastList != null && broadcastList.size() == 0) {
                broadcastList.add(event);
                hasBroadcast(true);
                mBroadcastAdapter.setNewData(broadcastList);
            }
        }
    }

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
            giveUp();
        }

        @Override
        public void onLeftSenIconClick(View view) {
        }

        @Override
        public void onLeftTxtClick(View view) {

        }

        @Override
        public void onRightIconClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(ScanByQRActivity.BUNDLE_KEY_TITLE, getString(R.string.scan_code));
            bundle.putString(ScanByQRActivity.BUNDLE_KEY_MESSAGE, getString(R.string.scan_broadcast_code));
            bundle.putInt(ScanByQRActivity.BUNDLE_KEY_TYPE, ScanByQRActivity.BUNDLE_VALUE_TYPE_BROADCAST);
            openActivityForResult(ScanByQRActivity.class, bundle, RESULT_SCAN_BROADCAST);
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };

    /**
     * 按下返回键
     */
    private void giveUp() {
        if (null != broadcastList && broadcastList.size() > 0) {
            getPresenter().showCancelSetting(getString(R.string.give_up_add_broadcast), getString(R.string.is_give_up_add_broadcast), new BaseDialog.OnDialogClick() {
                @Override
                public void onClick(int whichOne) {
                    if (whichOne == RemindDialog.WHICH_COMPLATE) {
                        if (getPresenter().mRemindDialog != null && getPresenter().mRemindDialog instanceof RemindDialog) {
                            back();
                        }
                    }
                }
            });
        } else {
            back();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            giveUp();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 是否有添加广播包
     *
     * @param hasBroadcast true已经添加  false未添加  默认未添加
     */
    private void hasBroadcast(boolean hasBroadcast) {
        if (hasBroadcast) {
            rl_broadcast_list.setVisibility(View.VISIBLE);
            tv_no_show.setVisibility(View.GONE);
        } else {
            rl_broadcast_list.setVisibility(View.GONE);
            tv_no_show.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
