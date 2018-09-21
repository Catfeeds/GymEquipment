package com.saiyi.gymequipment.common.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.run.ui.AddBroadcastActivity;
import com.saiyi.libble.BleAddressBean;
import com.saiyi.libble.BluetoothHelper;
import com.saiyi.libfast.activity.BaseActivity;
import com.saiyi.libfast.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanByQRActivity extends BaseActivity implements QRCodeView.Delegate, BluetoothHelper.SearchResultListener, BluetoothHelper.ConnectBleListener {

    public static final String BUNDLE_KEY_SCAN_RESULT = "scan_result";

    public static final int RESULT_SCAN_REQUEST = 11111;

    public static final int CAMERA_OK = 1000;

    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_TYPE = "type";
    public static final String BUNDLE_KEY_MESSAGE = "message";

    public static final int BUNDLE_VALUE_TYPE_BODYBUILD = 1;
    public static final int BUNDLE_VALUE_TYPE_BROADCAST = 2;

    @BindView(R.id.zxingview)
    ZXingView zxingview;

    private String title;
    private String message;
    private int type = 0;
    private String scanResult;
    private boolean isScaned = false;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void initView() {
        initPermission();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = (String) bundle.get(BUNDLE_KEY_TITLE);
            message = (String) bundle.get(BUNDLE_KEY_MESSAGE);
            type = bundle.getInt(BUNDLE_KEY_TYPE, 0);
            if (!TextUtils.isEmpty(title)) {
                mTitleBar.setTitle(title);
            }
            if (!TextUtils.isEmpty(message)) {
                zxingview.getScanBoxView().setQRCodeTipText(message);
                //调用此行，上一行才能生效，坑
                zxingview.getScanBoxView().setIsBarcode(false);
            }
        } else {
            mTitleBar.setTitle(R.string.qrcode_tips);
        }
        mTitleBar.noEndLine();
        dialog = new ProgressDialog(this);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
        } else {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingview.startCamera();
        zxingview.startSpotAndShowRect();
    }

    @Override
    protected void initListener() {
        zxingview.setDelegate(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    toast("请手动打开相机权限");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        zxingview.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zxingview.onDestroy();
        BluetoothHelper.getInstance(this).setSearchResultListener(null);    //扫描回调
        BluetoothHelper.getInstance(this).setConnectBleListener(null);
        dismissDialog();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (type == BUNDLE_VALUE_TYPE_BROADCAST) {
            Intent intent = new Intent(this, AddBroadcastActivity.class);
            intent.putExtra(BUNDLE_KEY_SCAN_RESULT, result);
            startActivity(intent);
            back();
        } else if (type == BUNDLE_VALUE_TYPE_BODYBUILD) {
            BluetoothHelper.getInstance(this).setSearchResultListener(this);    //扫描回调
            BluetoothHelper.getInstance(this).setConnectBleListener(this);
            scanResult = result;
            startScanBle();
        } else {
            Intent intent = new Intent();
            intent.putExtra(BUNDLE_KEY_SCAN_RESULT, result);
            setResult(RESULT_SCAN_REQUEST, intent);
            back();
        }
    }

    private void startScanBle() {
        showDialog();
        if (!BluetoothHelper.getInstance(this).isBlueToothOpened()) {
            BluetoothHelper.getInstance(this).openBlueTooth();
        }
        BluetoothHelper.getInstance(this).startSearch();                     //开始扫描
    }

    private void showDialog() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.setMessage(getString(R.string.scan_ble));
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    BluetoothHelper.getInstance(ScanByQRActivity.this).stopSearch();
                    if (!TextUtils.isEmpty(scanResult)) {
                        BluetoothHelper.getInstance(ScanByQRActivity.this).disconnect(scanResult);
                        zxingview.startSpot();
                    }
                    dialog.dismiss();
                }
                return false;
            }
        });
        dialog.show();
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void searchResult(BleAddressBean bean) {
        if (bean.getBleMac().equals(scanResult)) {
            BluetoothHelper.getInstance(this).stopSearch();
            BluetoothHelper.getInstance(this).connectBle(bean.getBleMac());
            isScaned = true;
        }
    }

    @Override
    public void searchStopped() {
        if (!isScaned) {
            toast(R.string.not_device);
            dismissDialog();
            zxingview.startSpot();
        }

    }

    @Override
    public void searchCanceled() {
        if (!isScaned) {
            toast(R.string.not_device);
            dismissDialog();
            zxingview.startSpot();
        }
    }

    @Override
    public void connectResult(int code, BleGattProfile data, String mac) {
        switch (code) {
            case Code.REQUEST_SUCCESS:
                Intent intent = new Intent();
                intent.putExtra(BUNDLE_KEY_SCAN_RESULT, scanResult);
                setResult(RESULT_SCAN_REQUEST, intent);
                back();
                break;
            case Code.REQUEST_FAILED:
                if (!isScaned) {
                    toast(R.string.connect_failed);
                    dismissDialog();
                    zxingview.startSpot();
                }
                break;
        }
    }
}
