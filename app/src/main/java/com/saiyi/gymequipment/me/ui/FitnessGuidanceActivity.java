package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidance;
import com.saiyi.gymequipment.me.model.bean.FitnessGuidanceContent;
import com.saiyi.gymequipment.me.presenter.FitnessGuidancePresenter;
import com.saiyi.libfast.utils.UiUtil;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JingNing on 2018-07-17 11:32
 */
public class FitnessGuidanceActivity extends BKMVPActivity<FitnessGuidancePresenter> {

    public static final String BUNDLE_KEY_TYPE = "type";    //从哪个界面过来的
    public static final int BUNDLE_VALUE_LIST = 1;    //从list界面过来，需要数据 idHealthyGuidance	健康指导ID     hgimg	图片地址   hgtitle	标题
    public static final String BUNDLE_KEY_IDHEALTHYGUIDANCE = "id_healthyguidance";
    public static final String BUNDLE_KEY_HGIMG = "hgimg";
    public static final String BUNDLE_KEY_HGTITLE = "hgtitle";

    public static final int BUNDLE_VALUE_MOTION = 2;    //从运动界面过来，需要数据 eptid
    public static final String BUNDLE_KEY_EQID = "eqid";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;

    private boolean isFullscreen = false;

    @Override
    public FitnessGuidancePresenter initPresenter(Context context) {
        return new FitnessGuidancePresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_guidance);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.details);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        int type = bundle.getInt(BUNDLE_KEY_TYPE);
        if (type == BUNDLE_VALUE_LIST) {
            int idHealthyGuidanc = bundle.getInt(BUNDLE_KEY_IDHEALTHYGUIDANCE);
            String hgtitle = bundle.getString(BUNDLE_KEY_HGTITLE);
            tvTitle.setText(hgtitle);
            getPresenter().getHealthyGuidanceContent(idHealthyGuidanc);
        } else {
            int eqid = bundle.getInt(BUNDLE_KEY_EQID);
            getPresenter().getHealthyGuidance(eqid);
        }
        videoView.setMediaController(mediaController);
        videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                if (isFullscreen) {
                    fullScreen();
                } else {
                    nonFullScreen();
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // 视频暂停
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// 视频开始缓冲
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// 视频结束缓冲

            }

        });
    }

    private void fullScreen() {
        isFullscreen = true;
        hiddenTitleBar();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
        videoLayout.setLayoutParams(layoutParams);
        tvTitle.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
    }

    private void nonFullScreen() {
        isFullscreen = false;
        showTitleBar();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) videoLayout.getLayoutParams();
        layoutParams.setMargins(0, UiUtil.dip2px(5), 0, UiUtil.dip2px(10));
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = UiUtil.dip2px(175);
        videoLayout.setLayoutParams(layoutParams);
        tvTitle.setVisibility(View.VISIBLE);
        tvContent.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isFullscreen){
                videoView.setFullscreen(false);
            }else{
                back();
            }
        }
        return true;
    }

    public void showGetData() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void getHealthyGuidanceSuccess(FitnessGuidance fitnessGuidance) {
        if (fitnessGuidance != null) {
            tvTitle.setText(fitnessGuidance.getHgtitle());
            tvContent.setText(fitnessGuidance.getHgccontent());
            if(!TextUtils.isEmpty(fitnessGuidance.getHvideo())){
                videoView.setVideoPath(fitnessGuidance.getHvideo());
                videoView.start();
            }else{
                toast(R.string.guide_video_null);
            }
        }
    }

    public void getHealthyGuidanceContentSuccess(FitnessGuidanceContent content) {
        if (content != null) {
            tvContent.setText(content.getHgcContent());
            if(!TextUtils.isEmpty(content.getHvideo())){
                videoView.setVideoPath(content.getHvideo());
                videoView.start();
            }else{
                toast(R.string.guide_video_null);
            }
        }
    }

    public void getDataFailed(String err) {
        toast(err);
    }
}
