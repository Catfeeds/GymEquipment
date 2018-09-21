package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.action.Action;
import com.saiyi.gymequipment.common.model.UserHelper;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.dialog.BaseDialog;
import com.saiyi.gymequipment.common.view.dialog.HeadPortraitMenuDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindDialog;
import com.saiyi.gymequipment.common.view.dialog.RemindMsgDialog;
import com.saiyi.gymequipment.home.adapter.PicassoImageLoader;
import com.saiyi.gymequipment.me.presenter.UserInforMationPresenter;
import com.saiyi.gymequipment.user.model.bean.User;
import com.saiyi.libfast.helper.ImgSetHelper;
import com.saiyi.libfast.logger.Logger;
import com.saiyi.libfast.utils.DateUtils;
import com.saiyi.libfast.utils.FileUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * 用户信息界面
 */
public class UserInforMationActivity extends BKMVPActivity<UserInforMationPresenter> {

    @BindView(R.id.tv_userimage)
    ImageView tvUserimage;
    @BindView(R.id.ll_userimage)
    RelativeLayout llUserimage;
    @BindView(R.id.tv_nikename)
    TextView tvNikename;
    @BindView(R.id.ll_nikename)
    RelativeLayout llNikename;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_phone)
    RelativeLayout llPhone;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_sex)
    RelativeLayout llSex;
    @BindView(R.id.tv_brithday)
    TextView tvBrithday;
    @BindView(R.id.ll_brithday)
    RelativeLayout llBrithday;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.ll_height)
    RelativeLayout llHeight;
    @BindView(R.id.tv_body)
    TextView tvBody;
    @BindView(R.id.ll_body)
    RelativeLayout llBody;

    @BindView(R.id.rl_phone_sos)
    RelativeLayout rl_phone_sos;
    @BindView(R.id.tv_phone_sos)
    TextView tv_phone_sos;
    @BindView(R.id.tv_phone_sos_time)
    TextView tv_phone_sos_time;

    private HeadPortraitMenuDialog mHeadDialog;

    private ImgSetHelper mImgSetHelper;

    private RemindDialog mRemindDialog;

    PicassoImageLoader picassoImageLoader;

    @Override
    public UserInforMationPresenter initPresenter(Context context) {
        return new UserInforMationPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinformation);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.user_information);
        mHeadDialog = new HeadPortraitMenuDialog(this);
        mHeadDialog.setClick(new BaseDialog.OnDialogClick() {
            @Override
            public void onClick(int whichOne) {
                switch (whichOne) {
                    case HeadPortraitMenuDialog.WHICH_TAKE_PHOTO:
                        mImgSetHelper.takePhotoAndCrop();
                        break;
                    case HeadPortraitMenuDialog.WHICH_SELECT_PHOTO:
                        mImgSetHelper.selectPhotoAndCrop();
                        break;
                }
            }
        });

        mImgSetHelper = new ImgSetHelper(this, FileUtils.getAppFileDir(this).getPath() + "/user_img.jpg");
        mImgSetHelper.setListener(new ImgSetHelper.SetListener() {
            @Override
            public void onCropSuccess(File file) {
                setUserImg(file);
            }

            @Override
            public void onCropFaild() {
                setUserImgFaild();
            }
        });

        picassoImageLoader = new PicassoImageLoader();

        showView();
    }

    private void showView() {
        User user = UserHelper.instance().getUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getPic())) {
                CropCircleTransformation circle = (CropCircleTransformation) tvUserimage.getTag();
                if (circle == null) {
                    circle = new CropCircleTransformation();
                    tvUserimage.setTag(circle);
                }
                Picasso.with(mContext).load(user.getPic()).placeholder(R.drawable.leftbar_info).error(R.drawable.leftbar_info).transform(circle).into(tvUserimage);
            } else {
                tvUserimage.setImageResource(R.drawable.leftbar_info);
            }
        }
        if (!TextUtils.isEmpty(user.getUnickname())) {
            tvNikename.setText(user.getUnickname());
        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            tvPhone.setText(user.getPhone());
        }
        if (!TextUtils.isEmpty(user.getUgender())) {
            tvSex.setText(user.getUgender());
        }
        if (!TextUtils.isEmpty(user.getSosPhone())) {
            tv_phone_sos.setText(user.getSosPhone());
        }

        String birthday = DateUtils.formatLogDate(user.getUbirthday()).split(" ")[0];
        if (!TextUtils.isEmpty(birthday)) {
            tvBrithday.setText(birthday);
        }
        tvHeight.setText(user.getHeight() + "");
        tvBody.setText(user.getWeight() + "");
    }

    //显示头像选择菜单
    public void showHeadMenu() {
        dismissDialog();
        mHeadDialog.show();
    }

    private void dismissDialog() {
        if (mHeadDialog != null && mHeadDialog.isShowing()) {
            mHeadDialog.dismiss();
        }
    }

    //去设置用户头像
    public void setUserImg(File imgFile) {
        showCustomLoading(getString(R.string.set_up));
        getPresenter().uploadUserImg(imgFile);
    }

    //设置用户头像失败
    public void setUserImgFaild() {
        dismissProgressDialog();
        toast(getString(R.string.user_img_setting_failure));
    }

    //设置用户头像成功
    public void setUserImgSuccess() {
        dismissProgressDialog();
        if (!TextUtils.isEmpty(UserHelper.instance().getUser().getPic())) {
            CropCircleTransformation circle = (CropCircleTransformation) tvUserimage.getTag();
            if (circle == null) {
                circle = new CropCircleTransformation();
                tvUserimage.setTag(circle);
            }
            Picasso.with(mContext).load(UserHelper.instance().getUser().getPic()).
                    placeholder(R.drawable.leftbar_info).
                    error(R.drawable.leftbar_info).
                    transform(circle).into(tvUserimage);
        } else {
            tvUserimage.setImageResource(R.drawable.leftbar_info);
        }
        toast(getString(R.string.user_img_setting_success));
    }

    public void showSetting() {
        showCustomLoading(getString(R.string.set_up));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void showSettingSuccess() {
        dismissLoading();
        toast(R.string.set_up_success);
    }

    @OnClick({R.id.ll_userimage, R.id.ll_nikename, R.id.ll_phone, R.id.ll_sex, R.id.ll_brithday, R.id.ll_height, R.id.ll_body, R.id.rl_phone_sos})
    public void onViewClicked(View view) {

//        case 0://修改昵称-
//        case 1://修改性别
//        case 2://修改出生日期-
//        case 3://修改身高-
//        case 4://修改体重-
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_userimage:
                showHeadMenu();
                break;
            case R.id.ll_nikename:
                bundle.putString("data", tvNikename.getText().toString());
                bundle.putInt("updateType", 0);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            case R.id.ll_phone:
                break;
            case R.id.ll_sex:
                bundle.putString("data", tvSex.getText().toString());
                bundle.putInt("updateType", 1);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            case R.id.ll_brithday:
                bundle.putString("data", tvBrithday.getText().toString());
                bundle.putInt("updateType", 2);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            case R.id.ll_height:
                bundle.putString("data", tvHeight.getText().toString());
                bundle.putInt("updateType", 3);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            case R.id.ll_body:
                bundle.putString("data", tvBody.getText().toString());
                bundle.putInt("updateType", 4);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            case R.id.rl_phone_sos:
                bundle.putString("data", tv_phone_sos.getText().toString());
                bundle.putInt("updateType", 5);
                openActivityForResult(UpdateUserInfomationsActivity.class, bundle, 100);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.exit_login_tv)
    protected void exitLogin(View view) {
        showLogout();
    }

    //退出登录提示
    private void showLogout() {
        dismissRemindDialog();
        RemindMsgDialog dialog = new RemindMsgDialog(this);
        mRemindDialog = dialog;
        dialog.setTitleText(getString(R.string.user_exitlogin_tips));
        dialog.setMsgText("");
        dialog.setComplateTextColorRes(R.color.red_ff3d3d).setComplateText(getString(R.string.exit));
        dialog.setClick(mLogoutDialogClick);
        dialog.show();
    }

    final BaseDialog.OnDialogClick mLogoutDialogClick = new BaseDialog.OnDialogClick() {
        @Override
        public void onClick(int whichOne) {
            if (whichOne == RemindDialog.WHICH_COMPLATE) {
                if (mRemindDialog != null && mRemindDialog instanceof RemindDialog) {
                    //退出登录
                    UserHelper.instance().loginOut();
                    back();
                    EventBus.getDefault().post(Action.ACTION_LOGOUT);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            String datas = "";
            if (data != null && data.getStringExtra("data") != null && !data.getStringExtra("data").equals(""))
                datas = data.getStringExtra("data");
            else
                return;
            if (resultCode == 100 + 0) {
                tvNikename.setText(datas);
                UserHelper.instance().getUser().setUnickname(datas);
            } else if (resultCode == 100 + 1) {
                tvSex.setText(datas);
                UserHelper.instance().getUser().setUgender(datas);
            } else if (resultCode == 100 + 2) {
                tvBrithday.setText(datas);
                long date = StringUtils.stringDateTolong(datas);
                if (date != -1) {
                    UserHelper.instance().getUser().setUbirthday(date);
                }
            } else if (resultCode == 100 + 3) {
                tvHeight.setText(datas);
                try {
                    UserHelper.instance().getUser().setHeight(Integer.parseInt(datas));
                } catch (Exception e) {
                }
            } else if (resultCode == 100 + 4) {
                tvBody.setText(datas);
                try {
                    UserHelper.instance().getUser().setWeight(Integer.parseInt(datas));
                } catch (Exception e) {
                }
            } else if (resultCode == 100 + 5) {
                Log.e("sos", "输入电话：" + datas);
                tv_phone_sos.setText(datas);

                try {
                    UserHelper.instance().getUser().setSosPhone(datas);
                } catch (Exception e) {
                }
            }
            getPresenter().updateUserDate(UserHelper.instance().getUser().getUnickname(),
                    UserHelper.instance().getUser().getUgender(),
                    tvBrithday.getText().toString().trim(),
                    UserHelper.instance().getUser().getHeight(),
                    UserHelper.instance().getUser().getWeight()
                    , UserHelper.instance().getUser().getSosPhone());
        } else {//设置头像
            mImgSetHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    //释放头像选择菜单
    private void releaseHeadDialig() {
        if (mHeadDialog != null && mHeadDialog.isShowing()) {
            mHeadDialog.dismiss();
            mHeadDialog = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseHeadDialig();
        dismissDialog();
    }


}
