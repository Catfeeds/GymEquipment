package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.run.event.BroadcastEvent;
import com.saiyi.gymequipment.run.presenter.AddFootpathPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.utils.Arith;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增步道
 */
public class AddFootpathActivity extends BKMVPActivity<AddFootpathPresenter> {

    @BindView(R.id.tv_footpath_name)
    TextView tv_footpath_name;

    @BindView(R.id.tv_distance)
    TextView tv_distance;

    @BindView(R.id.tv_broadcast_name)
    TextView tv_broadcast_name;

    @BindView(R.id.tv_submission)
    TextView tv_submission;

    private ArrayList<BroadcastEvent> broadcastList;

    private String footpath;//步道名
    private String distance;//距离
    private String broadcastNumber;//步道包个数

    public static final String INFO_TYPE_FOR_EDIT = "info_type_for_edit";
    public static final int GET_BROADCAST_INFORMATION = 333;
    public static final String ALREADY_ADD_BROADCAST = "already_add_broadcast";
    public static final int FOOT_PATH_NAME = 1;
//    public static final int FOOT_PATH_DISTANCE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_footpath);
        mTitleBar.setTitle(R.string.add_footpath);
        broadcastList = new ArrayList<>();
    }

    @Override
    public AddFootpathPresenter initPresenter(Context context) {
        return new AddFootpathPresenter(context);
    }

    @Override
    protected void initListener() {
        mTitleBar.setClickListener(new NavBar.NavBarOnClickListener() {
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

            }

            @Override
            public void onRightTxtClick(View view) {

            }
        });
    }


    @OnClick({R.id.rl_set_name, R.id.rl_distance_set, R.id.rl_broadcast_packet, R.id.tv_submission})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_set_name:
                intent = new Intent(this, FootPathSettingActivity.class);
                intent.putExtra(INFO_TYPE_FOR_EDIT, FOOT_PATH_NAME);
                startActivityForResult(intent, FOOT_PATH_NAME);
                break;
//            case R.id.rl_distance_set:
//                Toast.makeText(AddFootpathActivity.this, getString(R.string.add_broadcast_and_number), Toast.LENGTH_SHORT).show();
//                intent = new Intent(this, FootPathSettingActivity.class);
//                intent.putExtra(INFO_TYPE_FOR_EDIT, FOOT_PATH_DISTANCE);
//                startActivityForResult(intent, FOOT_PATH_DISTANCE);
//                break;
            case R.id.rl_broadcast_packet:
                if (broadcastList != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ALREADY_ADD_BROADCAST, broadcastList);
                    openActivityForResult(BroadcastActivity.class, bundle, GET_BROADCAST_INFORMATION);
                    broadcastList.clear();
                }
                break;
            case R.id.tv_submission:
                addFootpath();
                break;
            default:
                break;
        }
    }

    /**
     * 添加步道
     */
    private void addFootpath() {
        if (TextUtils.isEmpty(footpath)) {
            Toast.makeText(this, getString(R.string.please_set_footpath_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (broadcastList == null && broadcastList.size() == 0) {
            Toast.makeText(this, getString(R.string.please_add_footpath_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(distance)) {
            return;
        }
        getPresenter().addFootpath(footpath, distance, broadcastList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case 3:
                footpath = data.getStringExtra(FootPathSettingActivity.FOOT_PATH_INFORMATION);
                if (!TextUtils.isEmpty(footpath)) {
                    tv_footpath_name.setText(footpath);
                }
                break;
            case BroadcastActivity.ADD_BROADCAST_RESULT:
                double length = 0;
                broadcastList.addAll((ArrayList<BroadcastEvent>) data.getSerializableExtra(BroadcastActivity.ADD_RESULT_FOR_BROADCAST));
                if (broadcastList != null && broadcastList.size() > 0) {
                    broadcastNumber = broadcastList.size() + getString(R.string.individual);
                    tv_broadcast_name.setText(broadcastNumber);
                    for (BroadcastEvent event : broadcastList) {
                        length = Arith.add(length,event.getTbpdistance());
                    }
                    StringBuffer sb = new StringBuffer();
                    sb.append(length);
                    sb.append(getString(R.string.rice));
                    distance = sb.toString();
                    tv_distance.setText(distance);
                }
                break;
//            case 4:
//                distance = data.getStringExtra(FootPathSettingActivity.FOOT_PATH_INFORMATION);
//                if (!TextUtils.isEmpty(distance)) {
//                    tv_distance.setText(distance);
//                }
//                break;
        }
    }


    /**
     * 步道添加成功
     */
    public void addSuccess() {
        Log.e("dismiss", "步道添加成功，刷新页面");
        Toast.makeText(this, getString(R.string.foot_path_add_success), Toast.LENGTH_SHORT).show();
        distance = "";
        footpath = "";
        broadcastNumber = "";
        broadcastList.clear();
        tv_footpath_name.setText(getString(R.string.go_setting));
        tv_distance.setText(getString(R.string.zero_kilometres));
        tv_broadcast_name.setText(getString(R.string.go_setting));
    }

    /**
     * 步道添加失败
     *
     * @param message
     */
    public void addFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
