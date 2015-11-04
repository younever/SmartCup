package com.example.smartcup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.smartcup.R;

import android.Manifest.permission;
import android.R.layout;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.FileObserver;
import android.text.format.Time;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Today extends Fragment {
	
	LinearLayout layout;
	private GraphicalView mChartView; //显示图表
	PublicMethod pMethod = new PublicMethod();
	String date[];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View todayData = inflater.inflate(R.layout.today, container, false);  
        
//        FileObserver mFileObserver = new 
        
        mChartView = drawGraphicalViewTemperture();
        layout = (LinearLayout) todayData.findViewById(R.id.tem1);
        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));;
        return todayData;  
    }

	private GraphicalView drawGraphicalViewTemperture()
	{
		
		int min,max;
		int monthMax = pMethod.getMonthMax();
		if (pMethod.getDate()>=7) {
			max = pMethod.getDate();
			min = pMethod.getDate() - 7;
		}
		else {
			max = 7;
			min = 1;
		}
		GraphicalView mChartView;
		XYMultipleSeriesRenderer rendererT = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset datasetT = new XYMultipleSeriesDataset();
        XYSeries seriesT = new XYSeries("");
        Random r2= new Random();
        for (int i = 1; i <= monthMax; i++) {
        	pMethod.writeToTxt(getActivity(), i+"日.txt",r2.nextInt()%100+""); //
		}
        for (int i = 1; i <= monthMax; i++) {
			seriesT.add(i,Double.valueOf(pMethod.readFromTxt(getActivity(), i+"日.txt")));
			rendererT.addTextLabel(i, i+"日");
		}
        datasetT.addSeries(seriesT);
        XYSeriesRenderer xyRendererT =  new XYSeriesRenderer();
        xyRendererT.setColor(Color.GRAY);
        xyRendererT.setPointStyle(PointStyle.CIRCLE);
        xyRendererT.setDisplayChartValuesDistance(30);
        xyRendererT.setDisplayChartValues(true);
        xyRendererT.setChartValuesTextSize(15f);
        rendererT.addSeriesRenderer(xyRendererT);
        rendererT.setXLabels(0);
        rendererT.setYLabels(10);
        rendererT.setShowLegend(false);
        rendererT.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        rendererT.setPanEnabled(true);
        rendererT.setZoomEnabled(false, false);
        rendererT.setPanEnabled(true, false);
        rendererT.setExternalZoomEnabled(true);
        rendererT.setXAxisMin(min);
        rendererT.setXAxisMax(max);
        rendererT.setLabelsTextSize(15);
        rendererT.setClickEnabled(false);
        rendererT.setFitLegend(true);// 调整合适的位置
        rendererT.setChartTitleTextSize(0);
        rendererT.setXLabelsColor(Color.BLACK);
        rendererT.setYLabelsColor(0, Color.BLACK);
        rendererT.setYLabelsAlign(Align.RIGHT,0);
        rendererT.setAxesColor(Color.WHITE);
        rendererT.setLabelsColor(Color.WHITE);
        rendererT.setAntialiasing(true);
        rendererT.setXLabelsPadding(1);
        rendererT.setShowGridX(true);        
        mChartView = ChartFactory.getLineChartView(getActivity(), datasetT, rendererT);
		return mChartView;
	}
	
	
	
}
