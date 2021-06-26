package com.example.finalterm;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class LineChartData {
    Context context;
    LineChart lineChart;

    public LineChartData(LineChart lineChart, Context context){
        this.context = context;
        this.lineChart = lineChart;
    }

    public void initDataSet(ArrayList<Entry> valuesY) {
        if(valuesY.size()>0){
            for(int i = 0 ;i<valuesY.size();i++){
                final LineDataSet set;
                set = new LineDataSet(valuesY, "");
                set.setMode(LineDataSet.Mode.LINEAR);
                set.setColor(context.getResources().getColor(R.color.yellow));
                set.setCircleColor(context.getResources().getColor(R.color.yellow));
                set.setCircleRadius(4);
                set.setDrawCircleHole(false);
                set.setLineWidth(1.5f);
                set.setDrawValues(true);
                set.setValueTextSize(8);
                set.setValueTextColor(Color.WHITE);

                set.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        int IValue = (int) value;
                        return String.valueOf(IValue);
                    }
                });


                Legend legend = lineChart.getLegend();
                legend.setEnabled(false);
                Description description = lineChart.getDescription();
                description.setEnabled(false);

                LineData data = new LineData(set);
                lineChart.setData(data);
            }
        }else{
            lineChart.setNoDataText("暫時沒有數據");
            lineChart.setNoDataTextColor(Color.WHITE);
        }
        lineChart.invalidate();
    }

    public void initX(ArrayList dateList) {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);

        int count = dateList.size();
        if(count>10)
            count = 10;
        xAxis.setLabelCount(count);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);

        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateList));
    }

    public void initY() {
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setLabelCount(20, true);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);

        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(100);

        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "";
            }
        });
    }
}
