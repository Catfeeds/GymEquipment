package com.saiyi.gymequipment.user.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.view.CountDownView;
import com.saiyi.gymequipment.user.presenter.RegisterPresenter;
import com.saiyi.libfast.utils.StatusBarUtil;
import com.saiyi.libfast.utils.StringUtils;
import com.saiyi.libfast.utils.UiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BKMVPActivity<RegisterPresenter> {

    public static final int RESULT_REGISTER_REQUEST = 1003;
    public static final String BUNDLE_KEY_REGISTER_RESULT = "register_result";

    private final int COUNT_DOWN_SECOUND = 60;//倒计时60s

    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.valid_code_cdv)
    CountDownView validCodeView;
    @BindView(R.id.register_password_et)
    EditText registerPasswordEt;
    @BindView(R.id.register_pre_password_et)
    EditText registerPrePasswordEt;
    @BindView(R.id.register_btn)
    TextView registerBtn;
    @BindView(R.id.logger_tv)
    TextView loggerTv;
    @BindView(R.id.valid_code_et)
    EditText validCodeEt;
    @BindView(R.id.valid_code_ll)
    LinearLayout validCodeLl;

    @Override
    public RegisterPresenter initPresenter(Context context) {
        return new RegisterPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSoftInput(userNameEt);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.brown_status_bar_color));
        hiddenTitleBar();

        validCodeView.setEnabled(false);
        validCodeView.setTextColor(getResources().getColor(R.color.hint_text_color));
        userNameEt.addTextChangedListener(userNameTextListener);
        validCodeLl.setEnabled(false);
        validCodeEt.setOnFocusChangeListener(validCodeEtOnFocusChangeListener);

    }

    private View.OnFocusChangeListener validCodeEtOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            validCodeLl.setEnabled(hasFocus);
        }
    };

    //监听用户名（手机号）输入内容
    private TextWatcher userNameTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //手机号码长度
            if (s.length() == 11) {
                validCodeView.setEnabled(true);
                validCodeView.setTextColor(getResources().getColor(R.color.white));
            } else {
                validCodeView.setEnabled(false);
                validCodeView.setTextColor(getResources().getColor(R.color.hint_text_color));
            }
        }
    };

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick(R.id.valid_code_cdv)
    protected void onClickGetValidCode(View view) {
        String uName = userNameEt.getText().toString().trim();
        if (getPresenter().getValidCode(uName)) {
            validCodeView.setEnabled(false);
            validCodeView.setText(getString(R.string.sending));
        }
    }

    /**
     * 开始倒计时
     */
    public void startCountDown() {
        validCodeView.startCountDown(COUNT_DOWN_SECOUND, mCallback);
    }

    CountDownView.CountDownCallBack mCallback = new CountDownView.CountDownCallBack() {
        @Override
        public void onCountDown(int second) {
            validCodeView.setText(String.format(getString(R.string.count_str), String.valueOf(second)));
        }

        @Override
        public void onCountComplate() {
            validCodeView.setText(getString(R.string.send_valid_code));
        }
    };

    /**
     * 获取验证码成功
     */
    public void getValidCodeSuccess(String msg) {
        startCountDown();
        toast(msg);
    }

    /**
     * 获取验证码失败
     */
    public void getValidCodeFaild(String msg) {
        validCodeView.setEnabled(true);
        validCodeView.setText(getString(R.string.send_valid_code));
        toast(msg);
    }

    @OnClick(R.id.register_btn)
    protected void onRegisterClick(View view) {
        UiUtil.setVisibility(loggerTv, View.INVISIBLE);
        getPresenter().register(userNameEt.getText().toString(),
                registerPasswordEt.getText().toString(),
                registerPrePasswordEt.getText().toString(),
                validCodeEt.getText().toString());
    }

    public void onRegisterSuccess(String msg) {
        dismissProgressDialog();
        toast(msg);
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_KEY_REGISTER_RESULT, userNameEt.getText().toString());
        setResult(RESULT_REGISTER_REQUEST, intent);
        back();
    }

    public void showLoadingDialog() {
        showCustomLoading(getString(R.string.registering));
    }

    public void onRegisteFaild(String msg) {
        //loading取消
        dismissProgressDialog();
        toast(msg);
    }


    public void showErrorMsg(String msg) {
        UiUtil.setVisibility(loggerTv, View.VISIBLE);
        loggerTv.setText(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        validCodeView.release();
    }
}
