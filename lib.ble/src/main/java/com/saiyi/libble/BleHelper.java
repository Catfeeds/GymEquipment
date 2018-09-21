package com.saiyi.libble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class BleHelper {

    private static BleHelper instance;

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private ScanBleDeviceListener mScanBieDeviceListener;

    public BleHelper(Context context) {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }

        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
    }

    public static synchronized BleHelper getInstance(Context context) {
        if (instance == null) {
            instance = new BleHelper(context);
        }
        return instance;
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            String name = device.getName();
            String mac = device.getAddress();
            if (TextUtils.isEmpty(name)) return;
            if (TextUtils.isEmpty(mac)) return;
            if (device.getName().contains("JSQ")) {
                BleAddressBean addressBean = new BleAddressBean();
                addressBean.setBleMac(device.getAddress());
                addressBean.setBleName(device.getName());
                Log.e("blesearch", "名字：" + device.getName() + "--MAC:" + device.getAddress());
                if (mScanBieDeviceListener != null) {
                    mScanBieDeviceListener.scanResult(addressBean);
                }
            }
        }

    };


    /**
     * 开始扫描蓝牙
     */
    public void scanLeDevice() {

        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    /**
     * 停止扫描
     */
    public void stopScanLeDevice() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public interface ScanBleDeviceListener {
        void scanResult(BleAddressBean addressBean);
    }

    public void setScanBieDeviceListener(ScanBleDeviceListener mScanBleDeviceListener) {
        this.mScanBieDeviceListener = mScanBleDeviceListener;
    }

    public boolean isdiscover() {
        return mBluetoothAdapter.isDiscovering();
    }
}
