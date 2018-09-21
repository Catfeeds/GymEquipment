package com.saiyi.gymequipment.run.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.libfast.activity.view.NavBar;

/**
 * 步道信息编辑页面
 */
public class FootPathSettingActivity extends AppCompatActivity {

    private NavBar title_bar;
    private EditText et_info;
    private TextView tv_edit_info;
    private TextView tv_show_info;
    private int number;//判断当前设置的数据类型 1 编辑步道名  2 步道总长编辑 3 广播包间距编辑

    public static final String FOOT_PATH_INFORMATION = "info_form_edit";

    private RemindDialog mRemindDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_path_seting);
        title_bar = (NavBar) findViewById(R.id.title_bar);
        et_info = (EditText) findViewById(R.id.et_info);
        tv_edit_info = (TextView) findViewById(R.id.tv_edit_info);
        tv_show_info = (TextView) findViewById(R.id.tv_show_info);

        if (getIntent() != null) {
            number = getIntent().getIntExtra(AddFootpathActivity.INFO_TYPE_FOR_EDIT, -1);
        }

        if (number == AddFootpathActivity.FOOT_PATH_NAME) {//步道名编辑页面
            title_bar.setTitle(R.string.footpath_setting);
            tv_edit_info.setText(R.string.name);
        }

//        else if (number == AddFootpathActivity.FOOT_PATH_DISTANCE) {//广播包编辑页面
//            title_bar.setTitle(R.string.footpath_distance);
//            tv_edit_info.setText(R.string.length);
//        }

        else if (number == 3) {
            title_bar.setTitle(R.string.distance_of_up_one);
            tv_edit_info.setText(R.string.distance);
            et_info.setInputType(InputType.TYPE_CLASS_NUMBER);
            tv_show_info.setVisibility(View.VISIBLE);
        } else {
            finish();
        }

        title_bar.setLeftText(R.string.cancel);
        title_bar.setRightText(R.string.confirm);
        title_bar.hiddenLeftIcon();
        title_bar.showLeftText();
        title_bar.showRightText();

        initListener();
    }

    protected void initListener() {

        title_bar.setClickListener(new NavBar.NavBarOnClickListener() {
            @Override
            public void onLeftIconClick(View view) {

            }

            @Override
            public void onLeftSenIconClick(View view) {

            }

            @Override
            public void onLeftTxtClick(View view) {
                String content = et_info.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    finish();
                } else {
                    showCancelSetting();
                }
            }

            @Override
            public void onRightIconClick(View view) {

            }

            @Override
            public void onRightTxtClick(View view) {
                String info = et_info.getText().toString().trim();
                if (TextUtils.isEmpty(info)) {
                    if (number == AddFootpathActivity.FOOT_PATH_NAME) {
                        Toast.makeText(FootPathSettingActivity.this,getString(R.string.please_enter_name),Toast.LENGTH_SHORT).show();
                    }

//                    if (number == AddFootpathActivity.FOOT_PATH_DISTANCE || number == 3) {
                    if ( number == 3) {
                        Toast.makeText(FootPathSettingActivity.this,getString(R.string.please_enter_distance),Toast.LENGTH_SHORT).show();
                    }

                    return;
                }

                getIntent().putExtra(FOOT_PATH_INFORMATION, info);
                setResult(number + 2, getIntent());
                finish();
            }
        });
    }


    /**
     * 是否放弃此次编辑
     */
    private void showCancelSetting() {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(getString(R.string.give_up_edit));
        dialog.setMsgText(getString(R.string.content_is_not_empty));
        dialog.setComplateTextColorRes(R.color.colorAccent).setComplateText(getString(R.string.confirm));
        dialog.setClick(mOpenSettingDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mOpenSettingDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            String content = et_info.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                finish();
            } else {
                showCancelSetting();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
