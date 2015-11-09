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
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllData extends Fragment {
	
	private FileObserver mFileObserver;
	LinearLayout layout;
	private GraphicalView mChartView; //显示图表
	static PublicMethod pMethod = new PublicMethod();
	public static TextView textView;
	static GetTxtThread showThread;
	public static boolean updateflag;
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
        View newsLayout = inflater.inflate(R.layout.alldata, container,  
                false);  
        pMethod.writeToTxt(getActivity(), "test.txt", "ORANGE"); ///初始化
        if (null == mFileObserver) {
			mFileObserver = new InFilesObserver(getActivity().getFilesDir().getAbsolutePath());
			mFileObserver.startWatching();
        }
        
        textView = (TextView) newsLayout.findViewById(R.id.textView1);
        mChartView = draGraphicalViewType();
        layout = (LinearLayout) newsLayout.findViewById(R.id.tem3);
        layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        showThread = new GetTxtThread();
        showThread.start();
        
        return newsLayout;  
    } 
	
	@Override
	public void onDestroy() {
		if(null != mFileObserver) mFileObserver.stopWatching();
	}
	
//	@Override
//	public void onPause() {
//		// TODO 自动生成的方法存根
//		super.onPause();
//		showThread.stop();;
//	}
	
	static class InFilesObserver extends FileObserver{
		
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
				updateflag = true;
				break;
			default:
				break;
			}
			
		}
	}
	
	public static class GetTxtThread extends Thread{
		public void run(){
			
			while(true)
			{
				synchronized (showThread) {
					if(updateflag)
					{
//						showThread.sleep(100);
						updateflag = false;
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
						
					}
				}
				
			}
		}
		
	}
	
    private static Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 1) {  
            	String temp = pMethod.readFromTxt(mContext,"test.txt");
				textView.setText(temp);
            }  
        }  
    }; 
    

	
	private GraphicalView draGraphicalViewType(){
		GraphicalView mChartView;
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        int min,max,hour;
        hour=pMethod.getHour();    //设置坐标轴显示的初始坐标
        if (hour>=7) {
			max=hour;
			min=hour-7;
		}
        else {
			min = 0;
			max = 7;
		} 
        Random r = new Random(); //临时数值
        for(int i=0;i<=24;i++)
        {
       	 pMethod.writeToTxt(getActivity(),i+":11.txt",r.nextInt()%100+"");
        }
	    XYSeries series = new XYSeries("");
	    // 填充数据
	    for (int k = 0; k <= 24; k++) {
	        // 填x,y值
	        series.add(k, Double.valueOf(pMethod.readFromTxt(getActivity(), k+":11.txt"))); //20 + r.nextInt() % 100
	        renderer.addTextLabel(k,k+":11");               
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
