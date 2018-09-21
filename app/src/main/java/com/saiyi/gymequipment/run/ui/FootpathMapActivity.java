package com.saiyi.gymequipment.run.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.app.BKMVPActivity;
import com.saiyi.gymequipment.app.GymApplication;
import com.saiyi.gymequipment.run.bean.FootpathBean;
import com.saiyi.gymequipment.run.presenter.FootpathMapPresenter;
import com.saiyi.libfast.activity.view.NavBar;
import com.saiyi.libmap.LocationBean;
import com.saiyi.libmap.LocationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 步道列表 地图模式
 */
public class FootpathMapActivity extends BKMVPActivity<FootpathMapPresenter> implements AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, LocationUtil.LocationInfoListener {
    private final String TAG = FootpathMapActivity.class.getSimpleName();

    @BindView(R.id.mv_location)
    MapView mv_location;

    @BindView(R.id.search_et)
    EditText search_et;

    private AMap aMap;

    private LocationBean mLocationBean;
    private ArrayList<FootpathBean> list;
    private List<Marker> markerList;

    private Marker curShowWindowMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footpath_map);

        mv_location.onCreate(savedInstanceState);
        mv_location.onSaveInstanceState(savedInstanceState);
        markerList = new ArrayList<>();


        initMap();
        LocationUtil.getInstance(GymApplication.getContext()).startLoaction();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(this);

        if (getIntent() != null) {
            list = (ArrayList<FootpathBean>) getIntent().getSerializableExtra(FootpathActivity.FOOT_PATH_INFORMATION_LIST);
        }

        refreshUi();

    }

    private void initMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public FootpathMapPresenter initPresenter(Context context) {
        return new FootpathMapPresenter(context);
    }

    @Override
    protected void initView() {
        if (aMap == null) {
            aMap = mv_location.getMap();
        }
        mTitleBar.setTitle(R.string.foot_path);
        mTitleBar.hiddenRightIcon();

    }

    @Override
    protected void initListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);

        mTitleBar.setClickListener(new NavBar.NavBarOnClickListener() {
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

            }

            @Override
            public void onRightTxtClick(View view) {

            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s && s.length() > 0) {
                    if (mLocationBean == null) {
                        toast("当前位置信息获取失败");
                    } else {
                        getPresenter().getFootpathInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), s.toString());
                    }
                } else {
                    getPresenter().getFootpathInfo(mLocationBean.getLatitude(), mLocationBean.getLongitude(), "");
                }

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @OnClick({R.id.iv_to_list})
    public void onClick(View view) {
        back();
    }

    private void refreshUi() {

        if (null == list && list.size() == 0) {
            return;
        }
        Log.e(TAG, "当前步道个数：" + list.size());

        for (int i = 0; i < list.size(); i++) {
            LatLng latLng = new LatLng(list.get(i).getBroadcastPackets().get(0).getTbplatitude().doubleValue(),
                    list.get(i).getBroadcastPackets().get(0).getTbplongitude().doubleValue());
            StringBuffer sb = new StringBuffer();
            sb.append(list.get(i).getTname());
            sb.append("\t\t");
            sb.append(list.get(i).getBroadcastPackets().get(0).getDistance());

            addMarker(latLng, R.drawable.trails_1, sb.toString());
        }
    }


    /**
     * 添加起点终点Marker
     *
     * @param ll   起点终点 经纬度
     * @param icon 图标
     */
    public final void addMarker(LatLng ll, int icon, String info) {

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.title(info);

        markerOption.perspective(true);
        markerOption.zIndex(5);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), icon)));
        Marker marker = aMap.addMarker(markerOption);
        markerList.add(marker);
    }

    /**
     * 获取步道列表
     *
     * @param footpathBeanList
     */
    public void getFootpathInfo(List<FootpathBean> footpathBeanList) {
        dismissProgressDialog();
        remove();
        if (footpathBeanList != null && footpathBeanList.size() > 0) {
            list.addAll(footpathBeanList);
            refreshUi();
        }
    }

    public void getFootpathFailed(String msg){
        toast(msg);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(this).inflate(
                R.layout.map_info_window, null);
        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //点击其它地方隐藏infoWindow
        if (curShowWindowMarker != null) {
            curShowWindowMarker.hideInfoWindow();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        curShowWindowMarker = marker;
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 自定义infowinfow窗口，将自定义的infoWindow和Marker关联起来
     */
    public void render(Marker marker, View view) {
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.tv_map_info));
        titleUi.setText(title);
    }

    /**
     * 清除之前添加的marker
     */
    private void remove() {
        if (null != list && list.size() > 0) {
            list.clear();
            for (Marker marker : markerList) {
                marker.remove();
            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mv_location.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mv_location.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv_location.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mv_location.onDestroy();
        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @Override
    public void location(LocationBean bean) {
        this.mLocationBean = bean;
        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }

    @Override
    public void locationFail(String message) {
        LocationUtil.getInstance(GymApplication.getContext()).onDestory();
        LocationUtil.getInstance(GymApplication.getContext()).setLocationInfoListener(null);
    }
}
