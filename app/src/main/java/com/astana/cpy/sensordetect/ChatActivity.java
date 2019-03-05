package com.astana.cpy.sensordetect;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.astana.cpy.sensordetect.detect.AbsSensorDetector;
import com.astana.cpy.sensordetect.detect.LogDetector;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * 图表activity
 *
 * @author cpy
 * @Description:
 * @version:
 * @date: 2019/3/5
 */
public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private AbsSensorDetector mDetector;

    private ArrayList<Float> valueX = new ArrayList<>();
    private ArrayList<Float> valueY = new ArrayList<>();
    private ArrayList<Float> valueZ = new ArrayList<>();
    private static final int UPDATE_INTERVAL = 100;
    private long mLastUpdateTime;
    private Object lock = new Object();
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mDetector = new LogDetector(this, new LogDetector.LogDetectorListener() {
            @Override
            public void onSensorChanged(float x, float y, float z) {
                Log.d(TAG, "onSensorChanged() called with: x = [" + x + "], y = [" + y + "], z = [" + z + "]");
                valueX.add(x);
                valueY.add(y);
                valueZ.add(z);
                long currentTime = System.currentTimeMillis();
                long diffTime = currentTime - mLastUpdateTime;
                if (diffTime < UPDATE_INTERVAL)
                    return;
                mLastUpdateTime = currentTime;
                ArrayList<Float> valuxCloneX = (ArrayList<Float>) valueX.clone();
                ArrayList<Float> valuxCloneY = (ArrayList<Float>) valueY.clone();
                ArrayList<Float> valuxCloneZ = (ArrayList<Float>) valueZ.clone();
                valueX.clear();
                valueY.clear();
                valueZ.clear();
                updateData(valuxCloneX, valuxCloneY, valuxCloneZ);
            }
        });

        chart = findViewById(R.id.line_chat);
        // background color
        chart.setBackgroundColor(Color.WHITE);

    }

    private void setData(ArrayList<Float> valueX, ArrayList<Float> valueY, ArrayList<Float> valueZ) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < valueX.size(); i++) {
            values.add(new Entry(i, valueX.get(i)));
        }
        LineDataSet set1 = new LineDataSet(values, "ValueX");
        set1.setDrawIcons(false);
        set1.enableDashedLine(10f, 5f, 0);
        set1.setColor(Color.RED);
        set1.setLineWidth(1f);
        set1.setCircleColor(Color.RED);

        ArrayList<Entry> values2 = new ArrayList<>();
        for (int i = 0; i < valueY.size(); i++) {
            values2.add(new Entry(i, valueY.get(i)));
        }
        LineDataSet set2 = new LineDataSet(values2, "ValueY");
        set2.setDrawIcons(false);
        set2.enableDashedLine(10f, 5f, 0);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.BLUE);
        set2.setLineWidth(1f);

        ArrayList<Entry> values3 = new ArrayList<>();
        for (int i = 0; i < valueZ.size(); i++) {
            values3.add(new Entry(i, valueZ.get(i)));
        }
        LineDataSet set3 = new LineDataSet(values3, "ValueZ");
        set3.setDrawIcons(false);
        set3.enableDashedLine(10f, 5f, 0);
        set3.setColor(Color.GREEN);
        set3.setLineWidth(1f);
        set3.setCircleColor(Color.GREEN);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        // add the data sets
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        // set data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);

    }

    private void updateData(ArrayList<Float> valueX, ArrayList<Float> valueY, ArrayList<Float> valueZ){
        LineData lineData = chart.getData();
        if (lineData != null && lineData.getDataSetByIndex(0) != null) {
            ArrayList<Entry> newValuesX = new ArrayList<>();
            LineDataSet setX = (LineDataSet) chart.getData().getDataSetByIndex(0);
            ArrayList<Entry> oriValuesX = (ArrayList<Entry>) setX.getValues();
            newValuesX.addAll(oriValuesX);
            int oriSize = oriValuesX.size();
            int newSize = valueX.size() + oriSize;
            for (int i = oriSize; i < newSize; i++) {
                newValuesX.add(new Entry(i, valueX.get(i - oriSize)));
            }
            setX.setValues(newValuesX);

            ArrayList<Entry> newValuesY = new ArrayList<>();
            LineDataSet setY = (LineDataSet) chart.getData().getDataSetByIndex(1);
            ArrayList<Entry> oriValuesY = (ArrayList<Entry>) setY.getValues();
            newValuesY.addAll(oriValuesY);
            int oriSizeY = oriValuesY.size();
            int newSizeY = valueY.size() + oriSizeY;
            for (int i = oriSizeY; i < newSizeY; i++) {
                newValuesY.add(new Entry(i, valueY.get(i - oriSizeY)));
            }
            setY.setValues(newValuesY);

            ArrayList<Entry> newValuesZ = new ArrayList<>();
            LineDataSet setZ = (LineDataSet) chart.getData().getDataSetByIndex(2);
            ArrayList<Entry> oriValuesZ = (ArrayList<Entry>) setZ.getValues();
            newValuesZ.addAll(oriValuesZ);
            int oriSizeZ = oriValuesZ.size();
            int newSizeZ = valueZ.size() + oriSizeZ;
            for (int i = oriSizeZ; i < newSizeZ; i++) {
                newValuesZ.add(new Entry(i, valueZ.get(i - oriSizeZ)));
            }
            setZ.setValues(newValuesZ);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {
            setData(valueX, valueY, valueZ);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void startDetector(View view) {
        mDetector.start();
    }

    public void stopDetector(View view) {
        mDetector.stop();
    }
}
