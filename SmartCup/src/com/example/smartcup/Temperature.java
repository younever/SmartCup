package com.example.smartcup;


import java.io.File;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.smartcup.R;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Temperature extends Fragment {
	
	LinearLayout layout;
	private GraphicalView mChartView; //显示图表
	static PublicMethod pMethod = new PublicMethod();
	String date[];
	static TextView textView_t;
	private FileObserver mFileObserver;
	static Context mContext ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View todayData = inflater.inflate(R.layout.temperature, container, false);  
        textView_t = (TextView) todayData.findViewById(R.id.textView2);
        
        
        int hour = pMethod.getHour();
        String pathString = mContext.getFilesDir().getAbsolutePath() + "/" + "Temperture"+hour+".txt";
		File file = new File(pathString);
		if (!file.exists())
		{
			pMethod.writeToTxt(mContext, "Temperture"+hour+".txt", "0");
		}
        String temperature = pMethod.readFromTxt(mContext,"Temperture"+hour+".txt");
    	textView_t.setText(temperature+"°C");         
		if (null == mFileObserver) {
//        	int hour = pMethod.getHour();
        	String path = getActivity().getFilesDir().getAbsolutePath()+"/Temperture"+hour+".txt";
			mFileObserver = new InFilesObserver(path);
			mFileObserver.startWatching();
        }
        mChartView = drawGraphicalViewTemperture();
        layout = (LinearLayout) todayData.findViewById(R.id.tem1);
        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        
        return todayData;  
    }
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		mFileObserver.stopWatching();
		int hour = pMethod.getHour();
    	String path = getActivity().getFilesDir().getAbsolutePath()+"/Temperture"+hour+".txt";
		mFileObserver = new InFilesObserver(path);
		mFileObserver.startWatching();
		Message message = new Message();
		message.what = 1;
		handler.sendMessage(message);

	}
	
//	
private class InFilesObserver extends FileObserver{
		
		public InFilesObserver(String path,int mask)
		{
			super(path,mask);
		}
		public InFilesObserver (String path) {
			super(path);
		}
		@Override
		public void onEvent(int event, String path) {
			// TODO 自动生成的方法存根
			final int action = event&FileObserver.ALL_EVENTS;
			switch (action) {
			case FileObserver.MODIFY:
//				updateflag = true;
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
				break;
			default:
				break;
			}
			
		}
	}

    private  Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	int hour = pMethod.getHour();
            if (msg.what == 1) {  
            	String temperture = pMethod.readFromTxt(mContext,"Temperture"+hour+".txt");
            	if (!temperture.equals("")) {//文件的更改包括擦除内容和重写，在擦写的时候也算是MODIFY，对擦除的监听快于写入的速度导致程序奔溃
            		textView_t.setText(temperture+"°C");  
                	Log.e("temperature", temperture);
                	layout.removeView(mChartView);
                    mChartView = drawGraphicalViewTemperture();
                    layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
                    drawGraphicalViewTemperture();
				}
            	
            }  
        }  
    }; 
//   

	private GraphicalView drawGraphicalViewTemperture()
	{
		
		int min,max;
//		int monthMax = pMethod.getMonthMax();
		if (pMethod.getHour()>=7) {
			max = pMethod.getHour();
			min = pMethod.getHour() - 7;
		}
		else {
			max = 7;
			min = 0;
		}
		GraphicalView mChartView;
		XYMultipleSeriesRenderer rendererT = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset datasetT = new XYMultipleSeriesDataset();
        XYSeries seriesT = new XYSeries("");
        for (int i = 0; i < 24; i++) {
        	try {                                        //
        		seriesT.add(i,Double.valueOf(pMethod.readFromTxt(getActivity(),"Temperture"+i+".txt")).doubleValue());
    			rendererT.addTextLabel(i, i+":00");
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
			
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
