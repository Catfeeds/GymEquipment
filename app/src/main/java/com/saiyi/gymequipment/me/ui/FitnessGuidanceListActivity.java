package com.saiyi.gymequipment.me.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.me.adapter.FitnessNewsAdapter;
import com.saiyi.gymequipment.me.model.bean.Article;
import com.saiyi.gymequipment.me.model.bean.HealthyGuidances;
import com.saiyi.gymequipment.me.presenter.FitnessNewsPresenter;

import butterknife.BindView;


public class FitnessGuidanceListActivity extends BKMVPActivity<FitnessNewsPresenter> {

    private static final int pageSize = 7;

    private PullableListView lvList;

    @BindView(R.id.ptrl_main)
    PullToRefreshLayout ptrlMain;

    private FitnessNewsAdapter adapter;

    private int pageNum = 1;    //默认第一页
    private boolean isEnd = false;

    @Override
    public FitnessNewsPresenter initPresenter(Context context) {
        return new FitnessNewsPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitnessnews);
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setTitle(R.string.fitness_guidance);
        adapter = new FitnessNewsAdapter(this);

        lvList = (PullableListView) ptrlMain.getPullableView();
        ptrlMain.setOnPullListener(pullListener);
        lvList.setAdapter(adapter);
        lvList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        lvList.setOnItemClickListener(onItemClickListener);
        pageNum = 1;
        isEnd = false;
        getPresenter().getHealthyGuidances(pageNum, pageSize);
    }

    protected AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Article article = (Article) adapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putInt(FitnessGuidanceActivity.BUNDLE_KEY_TYPE, FitnessGuidanceActivity.BUNDLE_VALUE_LIST);
            bundle.putString(FitnessGuidanceActivity.BUNDLE_KEY_HGIMG, article.getHgimg());
            bundle.putString(FitnessGuidanceActivity.BUNDLE_KEY_HGTITLE, article.getHgtitle());
            bundle.putInt(FitnessGuidanceActivity.BUNDLE_KEY_IDHEALTHYGUIDANCE, article.getIdHealthyGuidance());
            openActivity(FitnessGuidanceActivity.class, bundle);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        ptrlMain.autoRefresh();
    }

    private PullToRefreshLayout.OnPullListener pullListener = new PullToRefreshLayout.OnPullListener() {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            isEnd = false;
            getPresenter().getHealthyGuidances(pageNum, pageSize);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            getPresenter().getHealthyGuidances(pageNum, pageSize);
        }
    };

//    public void showLoading() {
//        showCustomLoading(getString(R.string.get_dataing));
//    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void getHealthyGuidancesSuccess(HealthyGuidances healthyGuidances) {
        if (pageNum == 1) { //刷新
            adapter.update(healthyGuidances.getList());
        } else {
            if (!isEnd) {
                adapter.addData(healthyGuidances.getList());
            } else {
                toast(R.string.already_loaded);
            }
        }
        if (healthyGuidances.getList() != null && healthyGuidances.getList().size() >= pageSize) {
            pageNum++;
        } else {
            isEnd = true;
        }
        ptrlMain.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    public void getHealthyGuidancesFailed(String msg) {
//        dismissLoading();
        ptrlMain.refreshFinish(PullToRefreshLayout.SUCCEED);
        toast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ptrlMain.setOnPullListener(null);
    }
}
