package com.saiyi.gymequipment.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class EditLocationDialog extends Dialog implements View.OnClickListener {

    private View v;

    TextView tv_title;

    EditText et_longitude;

    EditText et_latitude;

    TextView tv_cancel;

    TextView tv_sure;

    private SureClickListener mSureClickListener;

    public EditLocationDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        initView();
    }


    protected void initView() {
        v = View.inflate(getContext(), R.layout.dialog_edit_lacation, null);
        setContentView(v);
        tv_title = findViewById(R.id.tv_title);
        et_longitude = findViewById(R.id.et_longitude);
        et_latitude = findViewById(R.id.et_latitude);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_sure = findViewById(R.id.tv_sure);

        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (mSureClickListener != null) {
                    mSureClickListener.cancel();
                }
                dismiss();
                break;
            case R.id.tv_sure:
                if (mSureClickListener != null) {
                    String longitude = getLongitude();
                    String latitude = getLatitude();
                    mSureClickListener.sure(longitude, latitude);
                }
                break;
            default:
                break;
        }
    }

    public interface SureClickListener {
        void sure(String longitude, String latitude);

        void cancel();
    }

    public void setSureClickListener(SureClickListener mSureClickListener) {
        this.mSureClickListener = mSureClickListener;
    }

    public void setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            tv_title.setText(titleText);
        }
    }

    /**
     * 获取经度
     *
     * @return
     */
    public String getLongitude() {
        return et_longitude.getText().toString().trim();
    }

    /**
     * 获取纬度
     *
     * @return
     */
    public String getLatitude() {
        return et_latitude.getText().toString().trim();
    }


}
