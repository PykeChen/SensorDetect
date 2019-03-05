package com.astana.cpy.sensordetect.detect;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 感应手机的移动
 *
 * @author wsq 2017/12/14 19:26
 * @see
 */

public class MovementDetector extends AbsSensorDetector {

    private static final String TAG = "MovementDetector";
    /**
     * 检测轻微移动
     */
    private static final float MOVE_THRESHOLD_LIGHT = 0.399F;
    /**
     * 检测普通移动
     */
    private static final float MOVE_THRESHOLD_MEDIUM = 0.599F;
    /**
     * 检测快速移动
     */
    private static final float MOVE_THRESHOLD_HARD = 0.799F;

    private MovementDetectorListener mDetectorListener;

    /**
     * 判断手机是否在移动
     */
    private boolean isPhoneMoving = false;

    private long mLastTimeOfDetectMove = System.currentTimeMillis();

    /**
     * 当前的感应加速度
     */
    private float mCurrentAccelerate = SensorManager.GRAVITY_EARTH;

    /**
     * 当前指定检测移动的threshold
     */
    private final float mAccelerateIsMoveThreshold = MOVE_THRESHOLD_LIGHT;

    /**
     * move到stationary的默认延长时间
     */
    private final long DEFAULT_MOVE_TO_STATIONARY = 2000;//毫秒

    private final long mTimeOfMoveToStationary = DEFAULT_MOVE_TO_STATIONARY;


    public MovementDetector(@NonNull Context context, @Nullable MovementDetectorListener l) {
        super(context);
        this.mDetectorListener = l;
    }

    @Override
    int getSensorType() {
        return Sensor.TYPE_ACCELEROMETER;
    }

    @MainThread//这个方法是在main线程执行
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        //如果没有外力施加在手机上，accelerometer将会是重力加速度的值，可以通过比较两次值的变化，判断手机是否在移动。
        float mAccelLast = mCurrentAccelerate;
        mCurrentAccelerate = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = Math.abs(mCurrentAccelerate - mAccelLast);
        Log.d(TAG, "delta:" + delta);
        // Make this higher or lower according to how much
        // motion you want to detect
        if (delta > mAccelerateIsMoveThreshold) {
            Log.d(TAG, "isMoving, delta:" + delta + ", mAccelerateIsMoveThreshold:" + mAccelerateIsMoveThreshold);
            mLastTimeOfDetectMove = System.currentTimeMillis();
            isPhoneMoving = true;
            if (mDetectorListener != null) {
                mDetectorListener.onPhoneMove();
            }
        } else {
            long timeDelta = (System.currentTimeMillis() - mLastTimeOfDetectMove);
            Log.d(TAG, "timeDelta:" + timeDelta + ", mLastTimeOfDetectMove:" + mLastTimeOfDetectMove + ", isPhoneMoving:" + isPhoneMoving);
            if (timeDelta > mTimeOfMoveToStationary && isPhoneMoving) {
                isPhoneMoving = false;
                if (mDetectorListener != null) {
                    mDetectorListener.onPhoneStationary();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @AnyThread
    public interface MovementDetectorListener {
        /**
         * 手机正在移动
         */
        void onPhoneMove();

        /**
         * 手机停止移动
         */
        void onPhoneStationary();
    }
}
