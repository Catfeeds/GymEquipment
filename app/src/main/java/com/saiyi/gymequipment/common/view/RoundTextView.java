package com.saiyi.gymequipment.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;

public class RoundTextView extends RelativeLayout {

    private TextView contentTv;
    private TextView hintTv;
    private TextView titleTv;


    public RoundTextView(Context context) {
        super(context);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_round_text, this, true);
        contentTv = findViewById(R.id.content_tv);
        hintTv = findViewById(R.id.hint_tv);
        titleTv = findViewById(R.id.title_tv);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.roundtextview);
        if (attributes != null) {
            String content = attributes.getString(R.styleable.roundtextview_content_text);
            if (!TextUtils.isEmpty(content)) {
                contentTv.setText(content);
            }
            String hint = attributes.getString(R.styleable.roundtextview_hint_text);
            if (!TextUtils.isEmpty(hint)) {
                hintTv.setText(hint);
            }
            String title = attributes.getString(R.styleable.roundtextview_title_text);
            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
        }
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setContentText(String content) {
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(content);
        }
    }

    public String getContentText() {
        return contentTv.getText().toString().trim();
    }

    public void setHintText(String hint) {
        if (!TextUtils.isEmpty(hint)) {
            hintTv.setText(hint);
        }
    }

    public String getHintText() {
        return hintTv.getText().toString().trim();
    }

    public void setTitleText(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    public String getTitleText() {
        return titleTv.getText().toString().trim();
    }
}
