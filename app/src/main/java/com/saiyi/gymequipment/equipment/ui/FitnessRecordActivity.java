package com.saiyi.gymequipment.equipment.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.loc.c;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.BKMVPFragment;
import com.saiyi.gymequipment.equipment.model.bean.FitnessRecord;
import com.saiyi.gymequipment.equipment.presenter.FitnessRecordPresenter;
import com.saiyi.gymequipment.home.model.bean.FitnessData;
import com.saiyi.libfast.activity.BaseActivity;
import com.saiyi.libfast.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 2018/4/28.
 * 跑步历史
 */

public class FitnessRecordActivity extends BKMVPActivity<FitnessRecordPresenter> {


    @BindView(R.id.order_viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_week)
    TextView tvWeek;
    @BindView(R.id.tv_month)
    TextView tvMonth;


    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_history);
    }

    @Override
    protected void initView() {
        addFragment();
        getTitleBar().setTitle(R.string.fitness_history);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
    }


    @Override
    public FitnessRecordPresenter initPresenter(Context context) {
        return new FitnessRecordPresenter(context);
    }

    private void addFragment() {
        //RankingSportFragment
        fragmentList.add(new FitnessRecordDayFragement());
        fragmentList.add(new FitnessRecordWeekFragement());
        fragmentList.add(new FitnessRecordMonthFragement());

//        viewpager.setPageTransformer(true, new ViewPagerTransformer());//设置滑动时的动画效果
        //给ViewPager设置适配器
        viewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changePageShow(position);
            }
        });
    }

    public void showGetDataLoading() {
        showCustomLoading(getString(R.string.get_dataing));
    }

    public void dismissLoading() {
        dismissProgressDialog();
    }

    public void changePageShow(int position) {
        if (position == 0) {
            tvDay.setTextColor(Color.WHITE);
            tvDay.setBackgroundResource(R.drawable.leftselect_shape_corner);

            tvWeek.setTextColor(Color.parseColor("#00E2A5"));
            tvWeek.setBackgroundResource(R.drawable.middle_unselect_shape_corner);

            tvMonth.setTextColor(Color.parseColor("#00E2A5"));
            tvMonth.setBackgroundResource(R.drawable.right_shape_corner);
        } else if (position == 1) {
            tvDay.setTextColor(Color.parseColor("#00E2A5"));
            tvDay.setBackgroundResource(R.drawable.left_shape_corner);

            tvWeek.setTextColor(Color.WHITE);
            tvWeek.setBackgroundResource(R.drawable.middle_shape_corner);

            tvMonth.setTextColor(Color.parseColor("#00E2A5"));
            tvMonth.setBackgroundResource(R.drawable.right_shape_corner);
        } else if (position == 2) {
            tvDay.setTextColor(Color.parseColor("#00E2A5"));
            tvDay.setBackgroundResource(R.drawable.left_shape_corner);

            tvWeek.setTextColor(Color.parseColor("#00E2A5"));
            tvWeek.setBackgroundResource(R.drawable.middle_unselect_shape_corner);

            tvMonth.setTextColor(Color.WHITE);
            tvMonth.setBackgroundResource(R.drawable.rightselect_shape_corner);
        }
    }

    @OnClick({R.id.tv_day, R.id.tv_week, R.id.tv_month})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_day:
                if (viewpager.getCurrentItem() != 0) {
                    changePageShow(0);
                    viewpager.setCurrentItem(0);
                }
                break;
            case R.id.tv_week:
                if (viewpager.getCurrentItem() != 1) {
                    changePageShow(1);
                    viewpager.setCurrentItem(1);
                }
                break;
            case R.id.tv_month:
                if (viewpager.getCurrentItem() != 2) {
                    changePageShow(2);
                    viewpager.setCurrentItem(2);
                }
                break;
        }
    }

//    //viewPager滑动动画
//    public class ViewPagerTransformer implements ViewPager.PageTransformer {
//        //滑动时的大小
//        private static final float MIN_SCALE = 0.8f;
//        private static final float MIN_ALPHA = 0.8f;
//
//        public void transformPage(final View view, float position) {
//            int pageWidth = view.getWidth();
//            int pageHeight = view.getHeight();
//            if (position < -1) { // [-Infinity,-1)             // 这个页面是关闭屏幕左边。
//                view.setAlpha(0);
//            } else if (position <= 1) { // [-1,1]             // 修改默认的幻灯片转换以缩小页面
//                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//                if (position < 0) {
//                    view.setTranslationX(horzMargin - vertMargin / 2);
//                } else {
//                    view.setTranslationX(-horzMargin + vertMargin / 2);
//                }
//                // 量表的下一页（min_scale和1之间）
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);              // 相对于页面的大小淡出页面。
//                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//            } else { // (1,+Infinity]             // 此页是关闭屏幕右侧。
//                view.setAlpha(0);
//            }
//        }
//    }
}