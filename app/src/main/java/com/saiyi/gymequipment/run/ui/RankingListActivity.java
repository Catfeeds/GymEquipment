package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.common.view.XCRoundImageView;
import com.saiyi.gymequipment.run.adapter.RankAdapter;
import com.saiyi.gymequipment.run.bean.RankingBean;
import com.saiyi.gymequipment.run.presenter.RankingPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libfast.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * 排行榜页面
 */
public class RankingListActivity extends BKMVPActivity<RankingPresenter> {

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.user_image_view)
    XCRoundImageView user_image_view;

    @BindView(R.id.tv_user_name)
    TextView tv_user_name;

    @BindView(R.id.tv_user_distance)
    TextView tv_user_distance;

    @BindView(R.id.rl_ranking_list)
    RecyclerView rl_ranking_list;

    private List<RankingBean> rankList;
    private RankAdapter mRankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);

        mTitleBar.setTitle(R.string.run_ranking_list);
        mTitleBar.setClickListener(navBarOnClickListener);
        rankList = new ArrayList<>();
        initAdapterData();
        getPresenter().rankingData();
        showProgressDialog();
    }

    protected NavBar.NavBarOnClickListener navBarOnClickListener = new NavBar.NavBarOnClickListener() {
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
            openActivity(AddFootpathActivity.class);
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };


    @Override
    public RankingPresenter initPresenter(Context context) {
        return new RankingPresenter(context);
    }

    private void initAdapterData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRankAdapter = new RankAdapter(this, rankList);
        rl_ranking_list.setLayoutManager(layoutManager);
        rl_ranking_list.setAdapter(mRankAdapter);
    }

    public void rankingInfo(List<RankingBean> list) {
        dismissProgressDialog();
        if (list != null && list.size() > 0) {
            int number = list.size() - 1;
            Number rank = list.get(number).getRank();
            if (rank == null) {
                tv_number.setText("-");
            } else {
                tv_number.setText(String.valueOf(rank.intValue()));
            }
            if (null == list.get(number).getUnickname()) {
                tv_user_name.setText("");
            } else {
                tv_user_name.setText(list.get(number).getUnickname());
            }

            if (null == list.get(number).getDistance()) {
                tv_user_distance.setText("0" + getString(R.string.km));
            } else {
                tv_user_distance.setText(String.valueOf(list.get(number).getDistance().doubleValue()) + getString(R.string.km));
            }

            if (TextUtils.isEmpty(list.get(number).getUimg())) {
                user_image_view.setImageResource(R.drawable.leftbar_info);
            } else {
                CropCircleTransformation circle = (CropCircleTransformation) user_image_view.getTag();
                if (circle == null) {
                    circle = new CropCircleTransformation();
                    user_image_view.setTag(circle);
                }
//                Log.e("xzc", "user.getUimg()=" + ((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + list.get(number).getUimg());
                Picasso.with(this).load(((GymApplication) GymApplication.getInstance()).getBuildConfig().BASE_HTTP_URL_IMAGE_URL + list.get(number).getUimg()).transform(circle).error(R.drawable.leftbar_info).into(user_image_view);
            }

            list.remove(number);
            rankList.addAll(list);
            mRankAdapter.setNewData(rankList);
        }
    }

    public void rankingInfoFali(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
