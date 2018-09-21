package com.saiyi.gymequipment.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class InfoTextView extends RelativeLayout {

    private TextView tv_info;
    private TextView tv_company;
    private TextView tv_state;

    public InfoTextView(Context context) {
        super(context);

    }

    public InfoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_info_text, this, true);
        tv_info = findViewById(R.id.tv_info);
        tv_company = findViewById(R.id.tv_company);
        tv_state = findViewById(R.id.tv_state);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.roundtextview);
        if (attributes != null) {
            String content = attributes.getString(R.styleable.roundtextview_content_text);
            if (!TextUtils.isEmpty(content)) {
                tv_info.setText(content);
            }
            String hint = attributes.getString(R.styleable.roundtextview_hint_text);
            if (!TextUtils.isEmpty(hint)) {
                tv_company.setText(hint);
            }
            String title = attributes.getString(R.styleable.roundtextview_title_text);
            if (!TextUtils.isEmpty(title)) {
                tv_state.setText(title);
            }
        }
    }

    public InfoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置信息
     *
     * @param info
     * @return
     */
    public InfoTextView setInfoText(String info) {
        if (!TextUtils.isEmpty(info)) {
            tv_info.setText(info);
        }
        return this;
    }

    /**
     * 获取信息
     *
     * @return
     */
    public String getInfoText() {
        return tv_info.getText().toString().trim();
    }

    /**
     * 设置单位
     *
     * @param company
     * @return
     */
    public InfoTextView setCompanyText(String company) {
        if (!TextUtils.isEmpty(company)) {
            tv_company.setText(company);
        }
        return this;
    }

    /**
     * 获取单位
     *
     * @return
     */
    public String getCompanyText() {
        return tv_company.getText().toString().trim();
    }

    /**
     * 设置单位是否可见
     *
     * @param visibility
     * @return
     */
    public InfoTextView setCompanyVisibility(int visibility) {
        tv_company.setVisibility(visibility);
        return this;
    }

    /**
     * 设置状态
     *
     * @param state
     */
    public void setStateText(String state) {
        if (!TextUtils.isEmpty(state)) {
            tv_state.setText(state);
        }
    }

    /**
     * 获取状态
     *
     * @return
     */
    public String getStatText() {
        return tv_state.getText().toString().trim();
    }
}
