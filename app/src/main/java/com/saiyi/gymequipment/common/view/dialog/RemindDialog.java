package com.saiyi.gymequipment.common.view.dialog;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.libfast.utils.UiUtil;

import butterknife.BindView;

public class RemindDialog extends BaseDialog implements View.OnClickListener{

    /**取消按钮*/
    public static final int WHICH_CANCLE = 10;

    /**确定按钮*/
    public static final int WHICH_COMPLATE = 11;

    private FrameLayout mConstansyFl;

    private View mDialogView;

    @BindView(R.id.cancle_tv)
    TextView mCancleTv;

    @BindView(R.id.complate_tv)
    TextView mComplateTv;

    @BindView(R.id.line)
    View mLine;

    public RemindDialog(@NonNull Context context) {
        super(context, R.style.dialog);
    }

    public RemindDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.dialog);
    }

    protected RemindDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        mDialogView = View.inflate(getContext(), R.layout.remind_dialog_layout, null);
        mConstansyFl = mDialogView.findViewById(R.id.content_fl);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mCancleTv.setOnClickListener(this);
        mComplateTv.setOnClickListener(this);
    }

    @Override
    public void setContentView(@NonNull View view) {
        mConstansyFl.addView(view);
        super.setContentView(mDialogView);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(getContext(), layoutResID, null);
        mConstansyFl.addView(view);
        super.setContentView(mDialogView);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        mConstansyFl.addView(view, params);
        super.setContentView(mDialogView);
    }

    public RemindDialog showComplate(){
        UiUtil.setVisibility(mComplateTv, View.VISIBLE);
        UiUtil.setVisibility(mLine, View.VISIBLE);
        return this;
    }

    public RemindDialog hidenComplate(){
        UiUtil.setVisibility(mComplateTv, View.GONE);
        UiUtil.setVisibility(mLine, View.GONE);
        return this;
    }

    public RemindDialog showCancle(){
        UiUtil.setVisibility(mCancleTv, View.VISIBLE);
        UiUtil.setVisibility(mLine, View.VISIBLE);
        return this;
    }

    public RemindDialog hidenCancle(){
        UiUtil.setVisibility(mCancleTv, View.GONE);
        UiUtil.setVisibility(mLine, View.GONE);
        return this;
    }

    public RemindDialog setComplateText(String text){
        mComplateTv.setText(text);
        return this;
    }

    public RemindDialog setCancleText(String text){
        mCancleTv.setText(text);
        return this;
    }

    public RemindDialog setCancleTextColor(@ColorInt int color){
        mCancleTv.setTextColor(color);
        return this;
    }

    public RemindDialog setCancleTextColorRes(@ColorRes int colorRes){
        mCancleTv.setTextColor(getContext().getResources().getColor(colorRes));
        return this;
    }

    public RemindDialog setComplateTextColor(@ColorInt int color){
        mComplateTv.setTextColor(color);
        return this;
    }

    public RemindDialog setComplateTextColorRes(@ColorRes int colorRes){
        mComplateTv.setTextColor(getContext().getResources().getColor(colorRes));
        return this;
    }

    protected <T extends RemindDialog> T setViewVisibility(View view, int visibility){
        UiUtil.setVisibility(view, visibility);
        return (T) this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complate_tv:
                onWhichOneClick(WHICH_COMPLATE);
                dismiss();
                break;
            case R.id.cancle_tv:
                onWhichOneClick(WHICH_CANCLE);
                dismiss();
                break;
        }
    }
}
