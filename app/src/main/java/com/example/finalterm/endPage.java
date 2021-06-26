package com.example.finalterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class endPage extends AppCompatActivity {
    private TextView text;
    private Button btn_restart;
    private ArrayList<Integer> attentionList;
    private int allTime=0;

    private LineChartData lineChartData;
    private LineChart lineChart;
    private ArrayList<String> xData = new ArrayList<>();
    private ArrayList<Entry> yData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_page);

        findView();
        getValue();
        setListener();
        hideSystemNavigationBar();
        setLineChart();
    }

    private void findView() {
        lineChart = (LineChart) findViewById(R.id.lineChart);
        text = (TextView) findViewById(R.id.endText);
        btn_restart = (Button) findViewById(R.id.btn_restart);
    }

    private void getValue() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("text");
        text.setText(str);
        Typeface font = Typeface.createFromAsset(getAssets(), "ChakraPetch-Medium.ttf");
        text.setTypeface(font);

        attentionList = intent.getIntegerArrayListExtra("attention");
        allTime = attentionList.size();
        for (int i=0; i<allTime; i++) {
            xData.add(i+"");
            yData.add(new Entry(i, attentionList.get(i)));
        }
    }

    private void setListener() {
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(endPage.this, setPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setLineChart() {
        lineChartData = new LineChartData(lineChart,this);
        lineChartData.initX(xData);
        lineChartData.initY();
        lineChartData.initDataSet(yData);
    }
}