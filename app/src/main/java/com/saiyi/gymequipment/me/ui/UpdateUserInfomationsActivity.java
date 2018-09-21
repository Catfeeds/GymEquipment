package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.tools.TimeUtil;
import com.saiyi.gymequipment.common.view.wheelview.CustomDatePicker;
import com.saiyi.gymequipment.common.view.wheelview.DataPickBuilder;
import com.saiyi.gymequipment.common.view.wheelview.DataPickInterface;
import com.saiyi.gymequipment.common.view.wheelview.DataPickView;
import com.saiyi.gymequipment.me.presenter.UserInforMationPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class UpdateUserInfomationsActivity extends BKMVPActivity<UserInforMationPresenter> {
    @BindView(R.id.et_updatenikename)
    EditText etUpdatenikename;
    @BindView(R.id.rl_update_nikename)
    RelativeLayout rlUpdateNikename;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.iv_male)
    ImageView ivMale;
    @BindView(R.id.iv_fmale)
    ImageView ivFmale;
    @BindView(R.id.male_selection)
    ImageView maleSelection;
    @BindView(R.id.fmale_selection)
    ImageView fmaleSelection;
    @BindView(R.id.rl_update_sex)
    LinearLayout rlUpdateSex;
    @BindView(R.id.tv_brithday)
    TextView tvBrithday;
    @BindView(R.id.rl_update_brithday)
    RelativeLayout rlUpdateBrithday;
    @BindView(R.id.tv_userheight)
    TextView tvUserheight;
    @BindView(R.id.rl_update_userheight)
    RelativeLayout rlUpdateUserheight;
    @BindView(R.id.tv_userbody)
    TextView tvUserbody;
    @BindView(R.id.rl_update_userbody)
    RelativeLayout rlUpdateUserbody;
    @BindView(R.id.npick1)
    NumberPicker npick1;
    @BindView(R.id.npick2)
    NumberPicker npick2;
    @BindView(R.id.npick3)
    NumberPicker npick3;
    @BindView(R.id.timepicker)
    LinearLayout timepicker;

    @BindView(R.id.ll_sos_phone)
    LinearLayout ll_sos_phone;

    @BindView(R.id.et_sos_phone)
    EditText et_sos_phone;

    /**
     * 更新信息类型
     */
    private int mUpdateType;
    private String mUpdateData;
    private CustomDatePicker customDatePicker;

    private DataPickView dataPickView;
    private boolean[] mPickTypes = null;
    private String[] mPickTexts = null;
    private List<List<String>> mPickLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_userinfomations);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUpdateType = bundle.getInt("updateType", 0);
            mUpdateData = bundle.getString("data", "");
        }
        showUpdateInfo();

        updateDatas();

    }


    @Override
    public UserInforMationPresenter initPresenter(Context context) {
        return new UserInforMationPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.showRightText();
        mTitleBar.setRightText(R.string.confirm);
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
                saveData();
            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 显示需要更新的布局
     */
    private void showUpdateInfo() {
        switch (mUpdateType) {
            case 0://修改昵称-
                mTitleBar.setTitle(R.string.user_nikename);
                rlUpdateNikename.setVisibility(View.VISIBLE);
                etUpdatenikename.setText(mUpdateData);
                break;
            case 1://修改性别
                mTitleBar.setTitle(getString(R.string.user_sex));
                rlUpdateSex.setVisibility(View.VISIBLE);
                if (getString(R.string.updateuserinfo_boy).equals(mUpdateData))
                    maleSelection.setVisibility(View.VISIBLE);

                if (getString(R.string.updateuserinfo_girl).equals(mUpdateData))
                    fmaleSelection.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mUpdateData))
                    tvSex.setText(mUpdateData);
                break;
            case 2://修改出生日期-
                mTitleBar.setTitle(R.string.user_brithday);
                rlUpdateBrithday.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mUpdateData)) {
                    tvBrithday.setText(mUpdateData);
                }
                initDatePicker();
                showDialog(tvBrithday.getText().toString());
                break;
            case 3://修改身高-
                mTitleBar.setTitle(R.string.user_bodyheight);
                rlUpdateUserheight.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mUpdateData))
                    tvUserheight.setText(mUpdateData);
                break;
            case 4://修改体重-
                mTitleBar.setTitle(getString(R.string.user_bodyweight));
                rlUpdateUserbody.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(mUpdateData))
                    tvUserbody.setText(mUpdateData);
                break;
            case 5://输入紧急求救电话号码
                mTitleBar.setTitle(getString(R.string.sos_phone));
                ll_sos_phone.setVisibility(View.VISIBLE);
                et_sos_phone.setText(mUpdateData);
                et_sos_phone.setSelection(mUpdateData.length());
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.iv_male, R.id.iv_fmale, R.id.tv_brithday})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_male:
                maleSelection.setVisibility(View.VISIBLE);
                fmaleSelection.setVisibility(View.GONE);
                tvSex.setText(getString(R.string.updateuserinfo_boy));
                break;
            case R.id.iv_fmale:
                maleSelection.setVisibility(View.GONE);
                fmaleSelection.setVisibility(View.VISIBLE);
                tvSex.setText(getString(R.string.updateuserinfo_girl));
                break;
            case R.id.tv_brithday:
                showDialog(tvBrithday.getText().toString());
                break;
        }
    }


    private void saveData() {
        String data = "";
        switch (mUpdateType) {
            case 0:
                data = etUpdatenikename.getText().toString();
                break;
            case 1:
                data = tvSex.getText().toString();
                break;
            case 2:
//                data = dataPickView.getTimeDatas();
                data = tvBrithday.getText().toString();
                break;
            case 3:
                data = dataPickView.getDatas();
                try {
                    data = Integer.parseInt(data) + "";
                } catch (NumberFormatException e) {
                    toast(R.string.data_format_error);
                    return;
                }
                break;
            case 4:
                data = dataPickView.getDatas();
                try {
                    data = Integer.parseInt(data) + "";
                } catch (NumberFormatException e) {
                    toast(R.string.data_format_error);
                    return;
                }
                break;
            case 5:
                data = et_sos_phone.getText().toString().trim();

                if (TextUtils.isEmpty(data)) {
                    toast(R.string.data_format_error);
                    return;
                }

                if (!StringUtils.isMobileNum(data)) {
                    toast(R.string.data_format_error);
                    return;
                }

                break;
            default:
                break;
        }

        Intent intent = new Intent();
        intent.putExtra("data", data);
        setResult(mUpdateType + 100, intent);
        back();
    }

    public List<String> getSimpleListData(int min, int max) {
        List<String> lists = new ArrayList<>();
        for (int i = min; i < max + 1; i++) {
            lists.add("" + i);
        }
        return lists;
    }

    public void updateDatas() {


        mPickLists = new ArrayList<>();//显示每一个滑动控件的数据


        switch (mUpdateType) {
            case 0:
                timepicker.setVisibility(View.GONE);
                break;
            case 1:
                timepicker.setVisibility(View.GONE);
                break;
//            case 2:
//                mPickTypes = new boolean[]{true, true, true, false, false, false};//是否显示异常每一个滑动控件
//                mPickTexts = new String[]{"年", "月", "日", "", "", ""};//每一个滑动控件旁边的描述文字
//
//                //调用方式2
//                dataPickView = new DataPickBuilder(this).setShowType(2).setDadaPickInterface(new DataPickInterface() {
//                    @Override
//                    public boolean[] getIsVisiables() {
//                        return mPickTypes;
//                    }
//
//                    @Override
//                    public String[] getVisiableText() {
//                        return mPickTexts;
//                    }
//
//                    @Override
//                    public List<List<String>> getAdapter() {
//                        return mPickLists;
//                    }
//
//                    @Override
//                    public Calendar getSelectIndexs() {
//                        //2018-04-22
//                        Calendar selectedDate = Calendar.getInstance();
//                        int year = 0;
//                        int month = 0;
//                        int day = 0;
//                        try {
//                            year = Integer.parseInt(mUpdateData.substring(0, mUpdateData.indexOf("-")));
//                            month = Integer.parseInt(mUpdateData.substring(mUpdateData.indexOf("-") + 1, mUpdateData.lastIndexOf("-")));
//                            day = Integer.parseInt(mUpdateData.substring(mUpdateData.lastIndexOf("-") + 1, mUpdateData.length()));
//                        } catch (Exception e) {
//                            return null;
//                        }
//                        selectedDate.set(year, month - 1, day, 0, 0, 0);
//                        return selectedDate;
//                    }
//                });
//
//                break;
            case 3:
                mPickTypes = new boolean[]{true, true, true, false, false, false};//是否显示异常每一个滑动控件
                mPickTexts = new String[]{"", "", "cm", "", "", ""};//每一个滑动控件旁边的描述文字
                mPickLists.add(getSimpleListData(1, 2));
                mPickLists.add(getSimpleListData(0, 9));
                mPickLists.add(getSimpleListData(0, 9));
                break;
            case 4:

                mPickTypes = new boolean[]{true, true, true, false, false, false};//是否显示异常每一个滑动控件
                mPickTexts = new String[]{"", "", "kg", "", "", ""};//每一个滑动控件旁边的描述文字
                mPickLists.add(getSimpleListData(0, 2));
                mPickLists.add(getSimpleListData(0, 9));
                mPickLists.add(getSimpleListData(0, 9));
                break;
            default:
                timepicker.setVisibility(View.GONE);
                break;
        }


        if (dataPickView != null || timepicker.getVisibility() == View.GONE)
            return;
        dataPickView = new DataPickBuilder(this).setDadaPickInterface(new DataPickInterface() {
            @Override
            public boolean[] getIsVisiables() {
                return mPickTypes;
            }

            @Override
            public String[] getVisiableText() {
                return mPickTexts;
            }

            @Override
            public List<List<String>> getAdapter() {
                return mPickLists;
            }

            @Override
            public int[] getSelectIndexs() {
                int[] indexs = new int[6];
                int item1, item2, item3;
                if (mUpdateType == 4) {
                    if (mUpdateData.length() == 3) {
                        item1 = mPickLists.get(0).indexOf(mUpdateData.substring(0, 1));
                        item2 = mPickLists.get(1).indexOf(mUpdateData.substring(1, 2));
                        item3 = mPickLists.get(2).indexOf(mUpdateData.substring(2, 3));
                        indexs[0] = item1;
                        indexs[1] = item2;
                        indexs[2] = item3;
                    }
                    if (mUpdateData.length() == 2) {
                        item2 = mPickLists.get(1).indexOf(mUpdateData.substring(0, 1));
                        item3 = mPickLists.get(2).indexOf(mUpdateData.substring(1, 2));
                        indexs[0] = 0;
                        indexs[1] = item2;
                        indexs[2] = item3;
                    }
                }

                if (mUpdateType == 3) {
                    if (mUpdateData.length() == 3) {
                        item1 = mPickLists.get(0).indexOf(mUpdateData.substring(0, 1));
                        item2 = mPickLists.get(1).indexOf(mUpdateData.substring(1, 2));
                        item3 = mPickLists.get(2).indexOf(mUpdateData.substring(2, 3));
                        indexs[0] = item1;
                        indexs[1] = item2;
                        indexs[2] = item3;
                    }
                    if (mUpdateData.length() == 2) {
                        item2 = mPickLists.get(1).indexOf(mUpdateData.substring(0, 1));
                        item3 = mPickLists.get(2).indexOf(mUpdateData.substring(1, 2));
                        indexs[0] = 0;
                        indexs[1] = item2;
                        indexs[2] = item3;
                    }
                }
                return indexs;
            }
        });
    }

    /**
     * 时间选择器
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvBrithday.setText(time.split(" ")[0]);

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

}
