package com.saiyi.gymequipment.common.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.saiyi.gymequipment.R;

import butterknife.BindView;

/**
 * 编辑头像菜单弹窗
 */
public class HeadPortraitMenuDialog extends MenuDialog implements View.OnClickListener {

    /**
     * 点击取消
     */
    public static final int WHICH_CANCLE = 1;
    /**
     * 拍照
     */
    public static final int WHICH_TAKE_PHOTO = 2;
    /**
     * 选择头像
     */
    public static final int WHICH_SELECT_PHOTO = 3;

    @BindView(R.id.take_photo_tv)
    TextView takePhotoTv;

    @BindView(R.id.select_photo_tv)
    TextView selectPhotoTv;

    @BindView(R.id.cancle_tv)
    TextView cancleTv;

    public HeadPortraitMenuDialog(@NonNull Context context) {
        super(context);
    }

    public HeadPortraitMenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HeadPortraitMenuDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.head_portrait_menu_dialog);
    }


    @Override
    protected void initView(View view) {
        takePhotoTv.setOnClickListener(this);
        selectPhotoTv.setOnClickListener(this);
        cancleTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_tv:
                onWhichOneClick(WHICH_CANCLE);
                break;
            case R.id.take_photo_tv:
                onWhichOneClick(WHICH_TAKE_PHOTO);
                break;
            case R.id.select_photo_tv:
                onWhichOneClick(WHICH_SELECT_PHOTO);
                break;
        }
        dismiss();
    }

}
