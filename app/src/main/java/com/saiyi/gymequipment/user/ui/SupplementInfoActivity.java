package com.saiyi.gymequipment.user.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.tools.TimeUtil;
import com.saiyi.gymequipment.common.view.wheelview.CustomDatePicker;
import com.saiyi.gymequipment.home.ui.HomeActivity;
import com.saiyi.gymequipment.user.presenter.SupplementInfoPresenter;
import com.saiyi.libfast.utils.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupplementInfoActivity extends BKMVPActivity<SupplementInfoPresenter> {

    @BindView(R.id.skip_tv)
    TextView skipTv;
    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.user_brithday_et)
    EditText userBrithdayEt;
    @BindView(R.id.user_height_et)
    EditText userHeightEt;
    @BindView(R.id.user_weight_et)
    EditText userWeightEt;
    @BindView(R.id.sos_phone_et)
    EditText sosPhoneEt;
    @BindView(R.id.confirm_btn)
    TextView confirmBtn;
    @BindView(R.id.male_iv)
    ImageView maleIv;
    @BindView(R.id.male_rl)
    RelativeLayout maleRl;
    @BindView(R.id.female_iv)
    ImageView femaleIv;
    @BindView(R.id.female_rl)
    RelativeLayout femaleRl;
    @BindView(R.id.logger_tv)
    TextView loggerTv;

    private int sexType; //1：男性， 2：女性
    private CustomDatePicker customDatePicker;

    @Override
    public SupplementInfoPresenter initPresenter(Context context) {
        return new SupplementInfoPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplement_info);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.brown_status_bar_color));
        hiddenTitleBar();
        initSexSelection();
        initDatePicker();
        userBrithdayEt.setCursorVisible(false);
        userBrithdayEt.setFocusable(false);
        userBrithdayEt.setFocusableInTouchMode(false);
    }

    private void initSexSelection() {
        maleIv.setImageResource(R.drawable.male_focused);
        femaleIv.setImageResource(R.drawable.female_normal);
        sexType = 1;
    }

    public void showErrorMsg(String msg) {
        loggerTv.setVisibility(View.VISIBLE);
        loggerTv.setText(msg);
    }

    public void onSupplementInfoSuccess(String msg) {
        openActivity(HomeActivity.class);
        back();
        toast(msg);
    }

    public void onSupplementInfoFaild(String msg) {
        toast(msg);
    }

    @OnClick(R.id.skip_tv)
    protected void onClickSkip(View view) {
        openActivity(HomeActivity.class);
        back();
    }

    @OnClick(R.id.user_brithday_et)
    protected void onClickBrithday(View view) {
        showDialog(userBrithdayEt.getText().toString());
    }

    /*
     * 时间选择器
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                userBrithdayEt.setText(time.split(" ")[0]);

            }
        }, "1900-01-01 01:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }

    private void showDialog(String time) {

        if (TextUtils.isEmpty(time)) {
            customDatePicker.show(TimeUtil.getCurrentTime() + " " + "01:01");
        } else {
            customDatePicker.show(time + " " + "01:01");
        }
    }

    @OnClick(R.id.confirm_btn)
    protected void onClickConfirm(View view) {
        String sex;
        if (sexType == 1) {
            sex = getString(R.string.updateuserinfo_boy);
        } else {
            sex = getString(R.string.updateuserinfo_girl);
        }
        getPresenter().updateUserDate(
                userNameEt.getText().toString().trim(),
                sex,
                userBrithdayEt.getText().toString().trim(),
                userHeightEt.getText().toString().trim(),
                userWeightEt.getText().toString().trim(),
                sosPhoneEt.getText().toString().trim());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            openActivity(HomeActivity.class);
            back();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @OnClick(R.id.male_rl)
    protected void onClickMale(View view) {
        maleIv.setImageResource(R.drawable.male_focused);
        femaleIv.setImageResource(R.drawable.female_normal);
        maleRl.setBackgroundResource(R.drawable.login_edit_border_focused);
        femaleRl.setBackgroundResource(R.drawable.login_edit_border_normal);
        sexType = 1;
    }

    @OnClick(R.id.female_rl)
    protected void onClickFemale(View view) {
        maleIv.setImageResource(R.drawable.male_normal);
        femaleIv.setImageResource(R.drawable.female_focused);
        femaleRl.setBackgroundResource(R.drawable.login_edit_border_focused);
        maleRl.setBackgroundResource(R.drawable.login_edit_border_normal);
        sexType = 2;
    }

}
