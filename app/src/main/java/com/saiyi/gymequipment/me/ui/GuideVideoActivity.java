package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.constans.Constant;
import com.saiyi.gymequipment.me.model.bean.EquipmentInfo;
import com.saiyi.libfast.mvp.PresenterImpl;
import com.saiyi.libfast.utils.UiUtil;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JingNing on 2018-08-23 16:22
 */
public class GuideVideoActivity extends BKMVPActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;

    @BindView(R.id.videoView)
    UniversalVideoView mVideoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mMediaController;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;

    private EquipmentInfo equipmentInfo;
    private boolean isFullscreen = false;

    @Override
    public PresenterImpl initPresenter(Context context) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_video);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.guide_video);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            equipmentInfo = (EquipmentInfo) bundle.get(Constant.BUNDLE_KEY_DATA);
            if(equipmentInfo == null){
                toast(R.string.error);
                return;
            }
        }
        tvId.setText(equipmentInfo.getEmac());
        tvName.setText(equipmentInfo.getEtname());
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
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
        mVideoView.setVideoPath(equipmentInfo.getHvideo());
        mVideoView.start();
    }

    private void fullScreen() {
        isFullscreen = true;
        hiddenTitleBar();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoLayout.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        videoLayout.setLayoutParams(layoutParams);
        bottomLayout.setVisibility(View.GONE);
    }

    private void nonFullScreen() {
        isFullscreen = false;
        showTitleBar();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoLayout.getLayoutParams();
        layoutParams.setMargins(UiUtil.dip2px(15), UiUtil.dip2px(15), UiUtil.dip2px(15), UiUtil.dip2px(9));
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = UiUtil.dip2px(175);
        videoLayout.setLayoutParams(layoutParams);
        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isFullscreen){
                mVideoView.setFullscreen(false);
            }else{
                back();
            }
        }
        return true;
    }
}
