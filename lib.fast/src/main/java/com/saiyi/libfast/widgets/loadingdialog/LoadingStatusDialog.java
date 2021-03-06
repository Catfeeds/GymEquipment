package com.saiyi.libfast.widgets.loadingdialog;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.libfast.R;
import com.saiyi.libfast.activity.view.LoadingProgress;


/**
 *
 *
 */
public class LoadingStatusDialog extends LoadingProgress {

    public static final int LEVEL_LOADING = 0;
    public static final int LEVEL_SUCCESS = 1;
    public static final int LEVEL_FAILD = 2;

    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';

    private ImageView iv_route;
    private TextView tv;
    private TextView tv_point;
    private RotateAnimation mAnim;
    private boolean cancelable = true;
    private LoadingStatus currentStatus, defaultStatus;//当前loading的显示状态

    private Handler handler = new Handler() {
        private int num = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num++;
                for (int i = 0; i < num; i++) {
                    builder.append(SUFFIX);
                }
                tv_point.setText(builder.toString());
                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            }
        }

        ;
    };

    public LoadingStatusDialog(Context context, LoadingStatus defaultStatus) {
        super(context, R.style.Dialog_bocop);
        this.defaultStatus = defaultStatus;
        init();
    }

    @SuppressWarnings("ResourceType")
    private void init() {
        View contentView = View.inflate(getContext(), R.layout.activity_custom_loding_dialog_layout, null);
        setContentView(contentView);

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelable) {
                    dismiss();
                }
            }
        });
        iv_route = (ImageView) findViewById(R.id.iv_route);
        tv = (TextView) findViewById(R.id.tv);
        tv_point = (TextView) findViewById(R.id.tv_point);
        initAnim();
        getWindow().setWindowAnimations(R.anim.alpha_in);
    }

    /**
     * 设置当前loading的状态
     */
    public void setLoadingStatus(LoadingStatus status) {
        if (status == null) return;
        if (currentStatus == null || (currentStatus != null && !(currentStatus.showPoint && status.showPoint))) {
            if (status.showPoint) {
                iv_route.startAnimation(mAnim);
                tv_point.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
            } else {
                iv_route.clearAnimation();
                tv_point.setVisibility(View.GONE);
                handler.removeMessages(CHANGE_TITLE_WHAT);
            }
        }
        iv_route.setImageLevel(status.level);
        tv.setText(status.msg);
        currentStatus = status;
    }


    private void initAnim() {
        mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
    }

    @Override
    public void show() {
        setLoadingStatus(defaultStatus);
        super.show();
    }

    @Override
    public void dismiss() {
        mAnim.cancel();
        super.dismiss();
    }


    @Override
    public void setCancelable(boolean flag) {
        cancelable = flag;
        super.setCancelable(flag);
    }

    @Override
    public void setTitle(CharSequence title) {
        tv.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public static class LoadingStatus {

        /**
         * status level要根据loading_level.xml文件中的level去进行配置
         */
        public int level;

        /**
         * 显示的文案
         */
        public String msg;

        /**
         * 时候显示右侧三个点
         */
        public boolean showPoint;

        public LoadingStatus() {
        }

        public LoadingStatus(int level, String msg, boolean showPoint) {
            this.level = level;
            this.msg = msg;
            this.showPoint = showPoint;
        }
    }

}
