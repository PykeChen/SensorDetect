package com.astana.cpy.sensordetect;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 运动传感器-静止传感器
 */
public class MotionActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mSigMotion;
    private Sensor mStatMotion;
    private SigTriggerListener mSigListener;
    private StateTriggerListener mStateListener;

    private TextView mTxtValue1;
    private TextView mTxtValue2;
    private TextView mTxtValue3;
    private TextView mTxtValue4;
    private TextView mTxtValue5;
    private TextView mTxtValue6;
    private TextView mTxtValue7;
    private TextView mTxtValue8;
    private TextView mTxtValue9;
    private TextView mTxtValue10;
    private TextView mTxtValue11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        mTxtValue1 = (TextView) findViewById(R.id.txt_value1);
        mTxtValue2 = (TextView) findViewById(R.id.txt_value2);
        mTxtValue3 = (TextView) findViewById(R.id.txt_value3);
        mTxtValue4 = (TextView) findViewById(R.id.txt_value4);
        mTxtValue5 = (TextView) findViewById(R.id.txt_value5);
        mTxtValue6 = (TextView) findViewById(R.id.txt_value6);
        mTxtValue7 = (TextView) findViewById(R.id.txt_value7);
        mTxtValue8 = (TextView) findViewById(R.id.txt_value8);
        mTxtValue9 = (TextView) findViewById(R.id.txt_value9);
        mTxtValue10 = (TextView) findViewById(R.id.txt_value10);
        mTxtValue11 = (TextView) findViewById(R.id.txt_value11);

        // 获取传感器管理对象
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mStatMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        mSigListener = new SigTriggerListener();
        mStateListener = new StateTriggerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStatMotion != null) {
            mSensorManager.requestTriggerSensor(mStateListener, mStatMotion);
        } else {
            Toast.makeText(this, "静态传感器为空", Toast.LENGTH_LONG).show();
        }
        if (mSigMotion != null) {
            mSensorManager.requestTriggerSensor(mStateListener, mSigMotion);
        } else {
            Toast.makeText(this, "运动传感器为空", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Call disable to ensure that the trigger request has been canceled.
        if (mStatMotion != null) {
            mSensorManager.cancelTriggerSensor(mStateListener, mStatMotion);
        }
        if (mSigMotion != null) {
            mSensorManager.cancelTriggerSensor(mSigListener, mSigMotion);
        }
    }

    class StateTriggerListener extends TriggerEventListener {

        public void onTrigger(TriggerEvent event) {
            // Do Work.

            // As it is a one shot sensor, it will be canceled automatically.
            // SensorManager.requestTriggerSensor(this, mSigMotion); needs to
            // be called again, if needed.
            float[] values = event.values;
            // 获取传感器类型
            int type = event.sensor.getType();
            StringBuilder sb = new StringBuilder();
            sb.append("静止传感器返回数据 type = ");
            sb.append(type);
            sb.append(":");
            sb.append("\nvalue[0]=");
            sb.append(values[0]);
            sb.append("\nvalue[1]=");
            sb.append(values[1]);
            sb.append("\nvalue[2]=");
            sb.append(values[2]);
            mTxtValue2.setText(sb.toString());
        }
    }

    class SigTriggerListener extends TriggerEventListener {

        public void onTrigger(TriggerEvent event) {
            float[] values = event.values;
            // 获取传感器类型
            int type = event.sensor.getType();
            StringBuilder sb = new StringBuilder();
            sb.append("运动传感器返回数据 type = ");
            sb.append(type);
            sb.append(":");
            sb.append("\nvalue[0]=");
            sb.append(values[0]);
            sb.append("\nvalue[1]=");
            sb.append(values[1]);
            sb.append("\nvalue[2]=");
            sb.append(values[2]);
            mTxtValue1.setText(sb.toString());
        }
    }
}