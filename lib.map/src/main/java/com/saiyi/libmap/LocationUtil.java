package com.saiyi.libmap;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationUtil {
    private static LocationUtil instance;
    private Context context;

    private AMapLocationClient mapLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private LocationInfoListener mLocationInfoListener;

    public LocationUtil(Context context) {
        this.context = context;
        initLocation();
    }


    public static synchronized LocationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LocationUtil(context);
        }
        return instance;
    }

    /**
     * 初始化定位信息
     */
    private void initLocation() {

        if (mapLocationClient != null) {
            return;
        }

        // 初始化定位
        mapLocationClient = new AMapLocationClient(context);
        // 初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 获取一次定位结果：
        // 该方法默认为false。
        mLocationOption.setOnceLocation(true);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);

        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);

        mapLocationClient.setLocationListener(mAMapLocationListener);

    }

    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                LocationBean lb = new LocationBean();
                lb.setLatitude(aMapLocation.getLatitude());//获取纬度
                lb.setLongitude(aMapLocation.getLongitude());//获取经度
                lb.setAltitude(aMapLocation.getAltitude());//获取GPS的当前状态
                lb.setStatus(aMapLocation.getGpsAccuracyStatus());//获取海拔
                lb.setTime(aMapLocation.getTime());//定位时间
                StringBuffer buffer = new StringBuffer();
                buffer.append(aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                lb.setAddress(buffer.toString());
                if (mLocationInfoListener != null) {
                    mLocationInfoListener.location(lb);
                }

                double latitude = aMapLocation.getLatitude();//获取纬度
                double longitude = aMapLocation.getLongitude();//获取经度
                int status = aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                double altitude = aMapLocation.getAltitude();//获取海拔
                float speed = aMapLocation.getSpeed();//获取速度
                long time = aMapLocation.getTime();//定位时间

                Log.e("----->data", "纬度:" + latitude + "   经度:" + longitude + "   GPS的当前状态:" + status + "  海拔:" + altitude + "  速度" + speed + "  time:" + time + " address:" + aMapLocation.getAddress());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("----->data", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

                mLocationInfoListener.locationFail(aMapLocation.getErrorInfo());
            }
        }
    };

    /**
     * 开始定位
     */
    public void startLoaction() {
        if (null != mapLocationClient) {
            // 给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mLocationOption);
            mapLocationClient.stopLocation();
            // 启动定位
            mapLocationClient.startLocation();
        }
    }

    /**
     * 注销定位
     */
    public void onDestory() {
        if (mapLocationClient != null) {
            mapLocationClient.onDestroy();
            instance = null;
        }
    }

    public interface LocationInfoListener {
        void location(LocationBean bean);

        void locationFail(String message);
    }

    public void setLocationInfoListener(LocationInfoListener mLocationInfoListener) {
        this.mLocationInfoListener = mLocationInfoListener;
    }
}
