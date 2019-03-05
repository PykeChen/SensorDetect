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
public class ShakeDetector extends AbsSensorDetector {

    private static final String TAG = "ShakeDetector";
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    public ShakeDetector(@NonNull Context context) {
        super(context);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    int getSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            Log.d(TAG, "onSensorChanged() called with: event = [" + x + "], [" + y + "], [" + z + "]");
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect
            Log.d(TAG, "onSensorChanged() called with: mAccel = [" + mAccel + "]");
            if(mAccel > 3){
                // do something
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
