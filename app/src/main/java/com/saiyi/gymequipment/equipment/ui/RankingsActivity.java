package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.common.tools.StringUtils;
import com.saiyi.gymequipment.common.view.XCRoundImageView;
import com.saiyi.gymequipment.equipment.adapter.RankingsAdapter;
import com.saiyi.gymequipment.equipment.model.bean.RankingUser;
import com.saiyi.gymequipment.equipment.presenter.RankingsPresenter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RankingsActivity extends BKMVPActivity<RankingsPresenter> {


    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.user_imageview)
    XCRoundImageView userImageview;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_time)
    TextView tvUserTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<RankingUser> list;
    private RankingUser me;
    private RankingsAdapter mRankingsAdapter;

    @Override
    protected void initView() {
        super.initView();
        getTitleBar().setTitle(R.string.ranking_list_title);
        list = new ArrayList<>();
        mRankingsAdapter = new RankingsAdapter(list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mRankingsAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getUserRankings();
    }

    @Override
    public RankingsPresenter initPresenter(Context context) {
        return new RankingsPresenter(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randkings);
    }

    public void getRankingsSuccess(List<RankingUser> list) {
        if (list != null) {
            me = list.get(list.size() - 1);
        }
        this.list.clear();
        for (int i = 0; i < list.size() - 1; i++) {
            this.list.add(list.get(i));
        }
        mRankingsAdapter.notifyDataSetChanged();
//        mRankingsAdapter.update(this.list);
        updateMe();
    }

    private void updateMe() {
        if (me == null) return;
        if (me.getRank() == 0) {
            tvNumber.setText("-");
        } else {
            tvNumber.setText(me.getRank() + "");
        }
        if (TextUtils.isEmpty(me.getUimg())) {
            userImageview.setImageResource(R.drawable.leftbar_info);
        } else {
            CropCircleTransformation circle = (CropCircleTransformation) userImageview.getTag();
            if (circle == null) {
                circle = new CropCircleTransformation();
                userImageview.setTag(circle);
            }
            Picasso.with(mContext).load(me.getUimg()).placeholder(R.drawable.leftbar_info).error(R.drawable.leftbar_info).transform(circle).into(userImageview);
        }
        if (me.getUnickname() != null) {
            tvUserName.setText(me.getUnickname());
        }
        tvUserTime.setText(StringUtils.secToHour(me.getFrduration().doubleValue()) + "h");
    }

    public void getRankingsFaild(String msg) {
        toast(msg);
    }

    public void showGetDataLoading() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }
}
