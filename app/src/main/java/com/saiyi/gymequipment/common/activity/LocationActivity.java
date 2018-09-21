package com.saiyi.gymequipment.common.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.common.view.dialog.EditLocationDialog;
import com.saiyi.libfast.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity implements LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    public static final String BUNDLE_KEY_LOCATION_RESULT = "location_result";
    public static final String BUNDLE_KEY_LONGITUDE_RESULT = "longitude_result";
    public static final String BUNDLE_KEY_LATITUDE_RESULT = "latitude_result";

    public static final int RESULT_LOCATION_REQUEST = 11112;

    //显示地图需要的变量
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.location_tv)
    TextView locationTv;
    @BindView(R.id.tv_edit)
    TextView tv_edit;
    @BindView(R.id.tv_longitude)
    TextView tv_longitude;
    @BindView(R.id.tv_latitude)
    TextView tv_latitude;


    private AMap aMap;//地图对象

    private GeocodeSearch mGeocodeSearch;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;


    private String location;
    private double longitude, latitude;

    private Marker marker;
    private boolean isCanFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);

        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        mTitleBar.setTitle(R.string.current_location);

        //获取地图对象
        aMap = mapView.getMap();


        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);


        //定位的小图标 默认是蓝点 这里自定义一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.nothing));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        //开始定位
        initLoc();
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
//                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
//                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
//                amapLocation.getCity();//城市信息
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码

                latitude = amapLocation.getLatitude();//获取纬度
                longitude = amapLocation.getLongitude();//获取经度
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点

                    LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);//添加图钉
                    addMarker(latLng, R.drawable.punctuation, null);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    isFirstLoc = false;
                    location = buffer.toString();
                    locationTv.setText(location);
                    isCanFinish = true;
                    refreshUI();
                    setResultAfter();
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.punctuation));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
//        //子标题
//        options.snippet("");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

    private void setResultAfter() {
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_KEY_LOCATION_RESULT, location);
        intent.putExtra(BUNDLE_KEY_LATITUDE_RESULT, latitude);
        intent.putExtra(BUNDLE_KEY_LONGITUDE_RESULT, longitude);
        setResult(RESULT_LOCATION_REQUEST, intent);
    }

    private void refreshUI() {
        StringBuffer sbLo = new StringBuffer();
        sbLo.append(getString(R.string.longitude));
        sbLo.append(":");
        sbLo.append(longitude);
        tv_longitude.setText(sbLo.toString());
        StringBuffer sbLa = new StringBuffer();
        sbLa.append(getString(R.string.latitude));
        sbLa.append(":");
        sbLa.append(latitude);
        tv_latitude.setText(sbLa.toString());
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    @OnClick(R.id.tv_edit)
    public void onClick(View v) {
        final EditLocationDialog eld = new EditLocationDialog(this);
        eld.setSureClickListener(new EditLocationDialog.SureClickListener() {
            @Override
            public void sure(String longitudeStr, String latitudeStr) {
                if (TextUtils.isEmpty(longitudeStr) || TextUtils.isEmpty(latitudeStr)) {
                    Toast.makeText(LocationActivity.this, getText(R.string.please_enter_right_data), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(longitudeStr) && !TextUtils.isEmpty(latitudeStr)) {
                    eld.dismiss();
                    longitude = Double.parseDouble(longitudeStr);
                    latitude = Double.parseDouble(latitudeStr);
                    refreshUI();
                    LatLng latLng = new LatLng(latitude, longitude);

                    // 设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    addMarker(latLng, R.drawable.punctuation, null);
                    getAddressByLatlng(latLng);
                }
            }

            @Override
            public void cancel() {
                isCanFinish = true;
            }
        });
        eld.show();
        isCanFinish = false;
    }

    /**
     * 添加Marker
     *
     * @param ll   起点终点 经纬度
     * @param icon 图标
     */
    public final void addMarker(LatLng ll, int icon, String info) {
        if (marker != null) {
            marker.remove();
        }
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(ll);
        markerOption.title(info);

        markerOption.perspective(true);
        markerOption.zIndex(5);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), icon)));
        marker = aMap.addMarker(markerOption);
    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        mGeocodeSearch.getFromLocationAsyn(query);
        isCanFinish = true;
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            if (isCanFinish) {
                finish();
            } else {
                Toast.makeText(this, "请等定位后按下返回按键", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        location = regeocodeAddress.getFormatAddress();
        locationTv.setText(location);
        setResultAfter();
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
