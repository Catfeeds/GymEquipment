package com.saiyi.gymequipment.me.tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * 控制闪光灯开关
 */
public class FlashLightHelper {

    private Camera camera;

    private Context context;

    public FlashLightHelper(Context context){
        this.context = context;
    }

    /*    *
     * 是否开启了闪光灯
     * @return
     */
    public boolean isFlashlightOn() {
        try {
            Camera.Parameters parameters = camera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 打开手电筒
     */
    public void openFlashLight() {
        try {
            if(flashLightAvailable() && !isFlashlightOn()){
                camera = Camera.open();
                camera.startPreview();
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭手电筒
     */
    public void closeFlashLight() {
        try {
            if(camera == null)return;
            if(flashLightAvailable() && isFlashlightOn()){
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean flashLightAvailable() {
        boolean flashAvailable = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        return flashAvailable;
    }
}
