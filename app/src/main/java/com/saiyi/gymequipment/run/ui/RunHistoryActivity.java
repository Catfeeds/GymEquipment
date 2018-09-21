package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.run.fragment.DayInfoFragment;
import com.saiyi.gymequipment.run.fragment.MonthInfoFragment;
import com.saiyi.gymequipment.run.fragment.WeekInfoFragment;
import com.saiyi.gymequipment.run.presenter.HistoryPresenter;
import com.saiyi.libfast.adapter.PagerAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class RunHistoryActivity extends BKMVPActivity<HistoryPresenter> {

    @BindView(R.id.tv_day_info)
    TextView tv_day_info;

    @BindView(R.id.tv_week_info)
    TextView tv_week_info;

    @BindView(R.id.tv_month_info)
    TextView tv_month_info;

    @BindView(R.id.history_vp)
    ViewPager history_vp;

    private SparseArray<Fragment> fragments = new SparseArray<>();
    private PagerAdapter pagerAdapter;

    private TextView[] tableViews = new TextView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_history);
        tableViews[0] = tv_day_info;
        tableViews[1] = tv_week_info;
        tableViews[2] = tv_month_info;
        mTitleBar.setTitle(R.string.run_history);
        initFragments();
    }

    @Override
    public HistoryPresenter initPresenter(Context context) {
        return new HistoryPresenter(context);
    }

    private void initFragments() {
        fragments.append(0, DayInfoFragment.newInstance());
        fragments.append(1, WeekInfoFragment.newInstance());
        fragments.append(2, MonthInfoFragment.newInstance());
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        history_vp.setOffscreenPageLimit(fragments.size());
        history_vp.setAdapter(pagerAdapter);
        history_vp.setOnPageChangeListener(mOnPageChangeListener);
        setTabSelected(0);
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTabSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void setTabSelected(int position) {
        for (int i = 0; i < tableViews.length; i++) {
            boolean isChecked = (i == position);
            if (isChecked) {
                tableViews[i].setSelected(isChecked);
                tableViews[i].setTextColor(getResources().getColor(R.color.white));
            } else {
                tableViews[i].setSelected(isChecked);
                tableViews[i].setTextColor(getResources().getColor(R.color.colorAccent));
            }

        }
    }

    @OnClick({R.id.tv_day_info, R.id.tv_week_info, R.id.tv_month_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_day_info:
                history_vp.setCurrentItem(0);
                break;
            case R.id.tv_week_info:
                history_vp.setCurrentItem(1);
                break;
            case R.id.tv_month_info:
                history_vp.setCurrentItem(2);
                break;
        }
    }
}
