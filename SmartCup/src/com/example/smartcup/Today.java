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

import android.R.layout;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Paint.Align;
import android.os.Bundle;
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
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View todayData = inflater.inflate(R.layout.today, container, false);  
        
        XYMultipleSeriesRenderer rendererT = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset datasetT = new XYMultipleSeriesDataset();
        Random r2= new Random();
        XYSeries seriesT = new XYSeries("");
        for (int k = 1; k < 31; k++) {
        	seriesT.add(k, r2.nextInt()%100);
		}
        datasetT.addSeries(seriesT);
        
        XYSeriesRenderer xyRendererT =  new XYSeriesRenderer();
        xyRendererT.setColor(Color.GRAY);
        xyRendererT.setPointStyle(PointStyle.CIRCLE);
        xyRendererT.setDisplayChartValuesDistance(30);
        xyRendererT.setDisplayChartValues(true);
        rendererT.addSeriesRenderer(xyRendererT);
        rendererT.setXLabels(10);
        rendererT.setYLabels(10);
        rendererT.setShowLegend(false);
        rendererT.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        rendererT.setZoomEnabled(true);
        rendererT.setPanEnabled(true);
        rendererT.setZoomEnabled(true, false);
        rendererT.setPanEnabled(true, false);
        rendererT.setExternalZoomEnabled(true);
        rendererT.setZoomInLimitX(3);
        rendererT.setZoomRate(3.0f);
        rendererT.setXAxisMin(4);
        rendererT.setXAxisMax(10);
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
        
        layout = (LinearLayout) todayData.findViewById(R.id.tem1);
        mChartView = ChartFactory.getLineChartView(getActivity(), datasetT, rendererT);
        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));;
        return todayData;  
    }


	public  void writeToTxt(String filename,String data)
	{
		FileOutputStream fos1;
		try {
			fos1 = getActivity().openFileOutput(filename,Context.MODE_PRIVATE );
			OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
			osw1.write(data);
			osw1.flush();
			fos1.flush();
			osw1.close();
			fos1.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	
	public String readFromTxt(String filename) {
		String readed1 = null;
		try {
			FileInputStream fis1 = getActivity().openFileInput(MainActivity.fileName_data);
			InputStreamReader is1 = new InputStreamReader(fis1, "UTF-8");
			char input1[] = new char[fis1.available()];
			is1.read(input1);
			is1.close();
			fis1.close();
			readed1 = new String(input1);
						
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return readed1;
		
	}



}
