package com.saiyi.gymequipment.user.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.WeChatManager;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.bean.AccessToken;
import com.saiyi.gymequipment.bean.WUser;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.permission.PermissionHelper;
import com.saiyi.gymequipment.common.permission.PermissionRationale;
import com.saiyi.gymequipment.home.ui.HomeActivity;
import com.saiyi.gymequipment.user.presenter.LoginPresenter;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.utils.StatusBarUtil;
import com.saiyi.libfast.utils.UiUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BKMVPActivity<LoginPresenter> {

    private static final int WX_AUTHORIZATION_SUCCESS = 1;

    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.user_pwd_et)
    EditText userPwdEt;
    @BindView(R.id.login_logger_tv)
    TextView loginLoggerTv;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.go_register_tv)
    TextView goRegistTv;
    @BindView(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @BindView(R.id.wechat_login_iv)
    ImageView wechatIv;

    private PermissionHelper mPermissionHelper;
    private String registerPhone;
    private AccessToken accessToken;
    private WUser wUser;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WX_AUTHORIZATION_SUCCESS:
                    getPresenter().onWxLogin(accessToken, wUser);
                    break;
            }
        }
    };

    @Override
    public LoginPresenter initPresenter(Context context) {
        return new LoginPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.setColor(this, getResources().getColor(R.color.brown_status_bar_color));
        hiddenTitleBar();
        WeChatManager.getInstance().setListener(authListener);
    }

    @Override
    protected void initData() {
        super.initData();
//        initPermission();
    }

    public void showErrorMsg(String msg) {
        UiUtil.setVisibility(loginLoggerTv, View.VISIBLE);
        loginLoggerTv.setText(msg);
    }

    //权限初始化
    private void initPermission() {
        mPermissionHelper = new PermissionHelper(this,
                PermissionRationale.CAMERA_PERMISSION,
                PermissionRationale.WRITE_READ_PERMISSION,
                PermissionRationale.BLE_PERMISSION,
                PermissionRationale.LOCATION_PERMISSION);
        mPermissionHelper.requestPermission();
    }


    @OnClick(R.id.login_btn)
    public void onClickLogin() {
        String uName = userNameEt.getText().toString().trim();
        String uPwd = userPwdEt.getText().toString().trim();
        UiUtil.setVisibility(loginLoggerTv, View.INVISIBLE);
        getPresenter().onLogin(uName, uPwd);
    }

    public void showLoginLoading() {
        showCustomLoading(getString(R.string.lgoining));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }


    public void loginSuccess() {
        if (!TextUtils.isEmpty(registerPhone) && userNameEt.getText().toString().trim().equals(registerPhone)) {
            //从注册来的登录，去完善资料
            openActivity(SupplementInfoActivity.class);
            back();
        } else {
            //直接登录
            openActivity(HomeActivity.class);
            back();
        }
    }

    public void loginFaild(String errorMsg) {
        toast(errorMsg);
    }

    @OnClick(R.id.go_register_tv)
    public void onClickRegister() {
        openActivityForResult(RegisterActivity.class, RegisterActivity.RESULT_REGISTER_REQUEST);
    }

    @OnClick(R.id.forget_pwd_tv)
    public void onClickForgetPwd() {
        openActivity(ForgetPasswordActivity.class);
    }

    @OnClick(R.id.wechat_login_iv)
    public void onClickWechatLogin() {
        WeChatManager.getInstance().loginToWeiXin(this);
    }

    protected WeChatManager.AuthListener authListener = new WeChatManager.AuthListener() {

        @Override
        public void onSuccess(AccessToken accessToken, WUser wUser) {
            LoginActivity.this.accessToken = accessToken;
            LoginActivity.this.wUser = wUser;
            handler.sendEmptyMessage(WX_AUTHORIZATION_SUCCESS);
        }

        @Override
        public void onFailed() {
            toast("微信授权失败");
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.RESULT_REGISTER_REQUEST) {
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                //注册成功过来的，提示完善资料界面
                registerPhone = bundle.getString(RegisterActivity.BUNDLE_KEY_REGISTER_RESULT);
                userNameEt.setText(registerPhone);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPermissionHelper != null) {
            mPermissionHelper.release();
        }
        WeChatManager.getInstance().release();
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
                back();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
