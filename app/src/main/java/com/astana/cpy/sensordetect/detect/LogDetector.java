package com.astana.cpy.sensordetect.detect;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author cpy
 * @Description:
 * @version:
 * @date: 2019/2/27
 */
public class LogDetector extends AbsSensorDetector {

    private static final String TAG = "LogDetector";

    private LogDetectorListener mListener;
    /**
     * 检测的时间间隔
     */
    private static final int UPDATE_INTERVAL = 100;
    /**
     * 上一次检测的时间
     */
    private long mLastUpdateTime;

    public LogDetector(@NonNull Context context, LogDetectorListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    int getSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    protected int getSamplingPeriodUs() {
        return SensorManager.SENSOR_DELAY_UI;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        long diffTime = currentTime - mLastUpdateTime;
//        if (diffTime < UPDATE_INTERVAL)
//            return;
        mLastUpdateTime = currentTime;
        final int factor = 100;
        float x = event.values[0] * factor;
        float y = event.values[1] * factor;
        float z = event.values[2] * factor;
        Log.d(TAG, "onSensorChanged() called with: diffTime = [" + diffTime + "]");
        mListener.onSensorChanged(x, y, z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @AnyThread
    public interface LogDetectorListener {

        void onSensorChanged(float x, float y, float z);
    }

}
