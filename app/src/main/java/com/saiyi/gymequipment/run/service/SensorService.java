package com.saiyi.gymequipment.run.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.saiyi.gymequipment.run.event.StepEvent;
import com.saiyi.libfast.event.EventAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SensorService extends Service implements SensorEventListener {

    //当前步数
    private int current_step;
    //传感器
    private SensorManager mSensorManager;
    //计步传感器类型 0-counter 1-detector
    private static int stepSensor = -1;
    //是否有当天的记录
    private boolean hasRecord;
    //未记录之前的步数
    private int hasStepCount;

    private StepThread mStepThread;

    private MyBinder myBinder = new MyBinder();

    private Sensor countSensor;
    private Sensor detectorSensor;
    private boolean isThreadStart = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mStepThread = new StepThread();
        EventBus.getDefault().register(this);
    }


    public void start() {
        if (mStepThread == null) {
            mStepThread = new StepThread();
        }

        if (mStepThread != null && !isThreadStart) {
            mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
            mStepThread.start();
            // 获取传感器管理器的实例
            isThreadStart = true;

        }
    }

    public void stop() {
        if (mStepThread != null) {
            try {
                mStepThread.exit = true;
                mStepThread.join();
                current_step = 0;
                hasStepCount = 0;
                isThreadStart = false;
                if (countSensor != null && detectorSensor != null) {
                    mSensorManager.unregisterListener(SensorService.this, countSensor);
                    mSensorManager.unregisterListener(SensorService.this, detectorSensor);
                    hasRecord = false;
                    mSensorManager = null;
                    mStepThread = null;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取传感器实例
     */
    private void getStepDetector() {
        if (mSensorManager == null) {
            return;
        }

        //android4.4以后可以使用计步传感器
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        }
    }

    /**
     * 添加传感器监听
     */
    private void addCountStepListener() {
        countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        detectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensor = 0;
            mSensorManager.registerListener(SensorService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensor = 1;
            mSensorManager.registerListener(SensorService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensor == 0) {
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                current_step = tempStep - hasStepCount;
            }
        } else if (stepSensor == 1) {
            if (event.values[0] == 1.0) {
                current_step++;
            }
        } else {

        }
        EventBus.getDefault().post(new StepEvent(current_step));
        Log.e("step", "当前步数：" + current_step);
    }


    /**
     * event bus事件接收
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventAction event) {
        /* Do something */
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class StepThread extends Thread {
        public volatile boolean exit = false;

        @Override
        public void run() {
            super.run();
            while (!exit) {
                getStepDetector();
            }
        }
    }

    public class MyBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }
}
