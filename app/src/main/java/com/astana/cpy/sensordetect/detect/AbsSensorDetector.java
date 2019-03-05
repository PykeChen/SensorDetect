package com.astana.cpy.sensordetect.detect;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.AnyThread;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

/**
 * sensor感应器
 * @author wsq 2017/12/14 19:21
 * @see
 */

public abstract class AbsSensorDetector implements SensorEventListener{

    private Context mContext;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    AbsSensorDetector(@NonNull Context context) {
        this.mContext = context;
        mSensorManager = (SensorManager) mContext.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * 开始监听
     */
    @AnyThread
    @CallSuper
    public void start() {
        if(mSensor != null) {
            stop();
            return;
        }
        mSensor = mSensorManager.getDefaultSensor(getSensorType());
        mSensorManager.registerListener(this, mSensor, getSamplingPeriodUs());
    }

    /**
     * 结束监听
     */
    @AnyThread
    @CallSuper
    public void stop(){
        if(mSensor != null && mSensorManager != null) {
            mSensorManager.unregisterListener(this, mSensor);
        }
        if(mSensor != null) {
            mSensor = null;
        }
    }

    /**
     * 获取传感器值的速率
     *
     * @return
     */
    protected int getSamplingPeriodUs(){
        return SensorManager.SENSOR_DELAY_GAME;
    }

    /**
     * 获取要监听的感应器类型
     * @return
     */
    abstract int getSensorType();



}
