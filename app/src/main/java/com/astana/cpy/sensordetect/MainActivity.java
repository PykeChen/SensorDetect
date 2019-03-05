package com.astana.cpy.sensordetect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.astana.cpy.sensordetect.detect.AbsSensorDetector;
import com.astana.cpy.sensordetect.detect.SignalMotionDetector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AbsSensorDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mDetector = new MovementDetector(this, new MovementDetector.MovementDetectorListener() {
//            @Override
//            public void onPhoneMove() {
//                Log.d(TAG, "onPhoneMove() called");
//            }
//
//            @Override
//            public void onPhoneStationary() {
//                Log.d(TAG, "onPhoneStationary() called");
//            }
//        });

//        mDetector = new ShakeDetector2(this, new ShakeDetector2.OnShakeListener() {
//            @Override
//            public void onShake() {
//
//            }
//        });
        mDetector = new SignalMotionDetector(this, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDetector.stop();
    }

    public void gotoOrientationActivity(View view) {
        Intent intent = new Intent(this, OrientationActivity.class);
        startActivity(intent);
    }

    public void gotoAllSensorActivity(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    public void gotoMotionActivity(View view) {
        Intent intent = new Intent(this, MotionActivity.class);
        startActivity(intent);
    }

    public void gotoChatActivity(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
