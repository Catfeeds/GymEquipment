package com.saiyi.gymequipment.common.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

import butterknife.BindView;

/**
 * 只显示提醒消息的弹窗
 */
public class RemindMsgDialog extends RemindDialog {

    @BindView(R.id.title_tv)
    TextView mTitleTv;

    @BindView(R.id.msg_tv)
    TextView mMsgTv;

    public RemindMsgDialog(@NonNull Context context) {
        super(context);
    }

    public RemindMsgDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected RemindMsgDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void initDialog() {
        super.initDialog();
        setContentView(R.layout.remind_msg_dialog);
    }

    public RemindMsgDialog setTitleText(CharSequence text){
        mTitleTv.setText(text);
        return this;
    }

    public RemindMsgDialog setMsgText(CharSequence text){
        if(TextUtils.isEmpty(text)){
            mMsgTv.setVisibility(View.GONE);
            return this;
        }
        mMsgTv.setText(text);
        return this;
    }

    public RemindMsgDialog showTitle(){
        return setViewVisibility(mTitleTv, View.VISIBLE);
    }

    public RemindMsgDialog hidenTitle(){
        return setViewVisibility(mTitleTv, View.GONE);
    }

    public RemindDialog showMsg(){
        return setViewVisibility(mMsgTv, View.VISIBLE);
    }

    public RemindMsgDialog hidenMsg(){
        return setViewVisibility(mMsgTv, View.GONE);
    }
}
