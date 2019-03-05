package com.astana.cpy.sensordetect.detect;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 摇晃检测器
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2019/2/27
 */
public class SignalMotionDetector extends AbsSensorDetector {

    private static final String TAG = "SignalMotionDetector";

    /**
     * 检测的时间间隔
     */
    private static final int UPDATE_INTERVAL = 100;
    /**
     * 上一次检测的时间
     */
    private long mLastUpdateTime;
    /**
     * 上一次检测时，加速度在x、y、z方向上的分量，用于和当前加速度比较求差。
     */
    private float mLastX, mLastY, mLastZ;
    /**
     * 摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感。
     */
    private int shakeThreshold = 5000;

    private OnShakeListener mListener;

    public SignalMotionDetector(@NonNull Context context, OnShakeListener onShakeListener) {
        super(context);
        mListener = onShakeListener;
    }

    @Override
    int getSensorType() {
        return Sensor.TYPE_SIGNIFICANT_MOTION;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
//        if (diffTime < UPDATE_INTERVAL)
//            return;
        mLastUpdateTime = currentTime;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        Log.d(TAG, "onSensorChanged() called with: x = [" + x + "], y = [" + y + "], z = [ " + z + "]");
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;
        mLastX = x;
        mLastY = y;
        mLastZ = z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected int getSamplingPeriodUs() {
        return SensorManager.SENSOR_DELAY_UI;
    }

    /**
     * 当摇晃事件发生时，接收通知
     */
    public interface OnShakeListener {
        /**
         * 当手机摇晃时被调用
         */
        void onShake();
    }
}
