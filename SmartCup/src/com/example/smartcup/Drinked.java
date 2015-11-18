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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Drinked extends Fragment {
	
	LinearLayout layout;
	private FileObserver mFileObserver;
	private GraphicalView mChartView; //显示图表
	static PublicMethod pMethod = new PublicMethod();
	static TextView textView_d;
	static Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View contactsLayout = inflater.inflate(R.layout.drinked,  
                container, false);  
        layout = (LinearLayout) contactsLayout.findViewById(R.id.tem2);
        textView_d = (TextView) contactsLayout.findViewById(R.id.drink);
        
        int date = pMethod.getDate();
        String pathString = mContext.getFilesDir().getAbsolutePath() + "/" + "Drinked"+date+".txt";
		File file = new File(pathString);
		if (!file.exists())
		{
			pMethod.writeToTxt(mContext, "Drinked"+date+".txt", "0");
		}

        String drink = pMethod.readFromTxt(mContext,"Drinked"+date+".txt");
    	textView_d.setText(drink+"ml"); 
        
		if (null == mFileObserver) {
        	String path = getActivity().getFilesDir().getAbsolutePath()+"/Drinked"+date+".txt";
			mFileObserver = new InFilesObserver(path);
			mFileObserver.startWatching();
//			Log.d("firsttime", mFileObserver.toString());
        }
    	mChartView = draGraphicalViewDrink();
    	layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        
    	return contactsLayout;  
    } 
	
	@Override
	public void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
//		mFileObserver.stopWatching();
		Log.d("test", "onPause");
	}
	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		mFileObserver.stopWatching();
		Log.d("test", "onDestroy");
	}

	
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
//		if (null == mFileObserver) {
			mFileObserver.stopWatching();
			int date = pMethod.getDate();
        	String path = getActivity().getFilesDir().getAbsolutePath()+"/Drinked"+date+".txt";
			mFileObserver = new InFilesObserver(path);
			mFileObserver.startWatching();
			String drink = pMethod.readFromTxt(mContext, "Drinked"+date+".txt");
			
			
//        }
//		Log.d("test", "onResume");
		
	}

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
				msg.what = 2;
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
        	int date = pMethod.getDate();
            if (msg.what == 2) {  
            	String drink = pMethod.readFromTxt(mContext,"Drinked"+date+".txt");
            	if (!drink.equalsIgnoreCase("")) {
            		textView_d.setText(drink+" ml");
                	Log.e("drink", drink);
                	layout.removeView(mChartView);
                	mChartView = draGraphicalViewDrink();
                	layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
				}
            	
            }  
        }  
    }; 
//	

	private GraphicalView draGraphicalViewDrink(){
		GraphicalView mChartView;
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        int min,max,monthMax;
        monthMax=pMethod.getMonthMax();    //设置坐标轴显示的初始坐标
        if (pMethod.getDate()>=7) {
			max=pMethod.getDate();
			min=pMethod.getDate()-7;
		}
        else {
			min = 1;
			max = 7;
		} 
	    XYSeries series = new XYSeries("");
	    // 填充数据
	    for (int k = 1; k <= pMethod.getMonthMax(); k++) {
	        // 填x,y值
	        series.add(k, Double.valueOf(pMethod.readFromTxt(getActivity(), "Drinked"+k+".txt"))); //20 + r.nextInt() % 100
	        renderer.addTextLabel(k,k+"日");               
	    }
	    // 需要绘制的点放进dataset中
	    dataset.addSeries(series);

        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();// 对点的绘制进行设置
        xyRenderer.setColor(Color.GRAY);//设置颜色
        xyRenderer.setPointStyle(PointStyle.SQUARE);//设置点的样式
        xyRenderer.setDisplayChartValuesDistance(30);
        xyRenderer.setDisplayChartValues(true);
        renderer.addSeriesRenderer(xyRenderer);
        renderer.setXLabels(0);
        renderer.setYLabels(9);//设置Y轴标签数 
        renderer.setXAxisMin(min);
        renderer.setXAxisMax(max);
        renderer.setLabelsTextSize(15);
        renderer.setShowLegend(false);
        renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        renderer.setZoomEnabled(false,false);
        renderer.setPanEnabled(true,false);
        renderer.setExternalZoomEnabled(false);
        renderer.setClickEnabled(false);
        renderer.setFitLegend(true);// 调整合适的位置
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.setChartTitleTextSize(0);
        renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
        renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
        renderer.setYLabelsAlign(Align.RIGHT, 0);
        renderer.setAxesColor(Color.WHITE);//设置坐标轴颜色
        renderer.setLabelsColor(Color.WHITE);
        renderer.setAntialiasing(true); 
        renderer.setShowGridX(true);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return mChartView;
		
	}
}
