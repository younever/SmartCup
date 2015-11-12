package com.example.smartcup;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Week extends Fragment {
	
	LinearLayout layout;
	private FileObserver mFileObserver;
	private GraphicalView mChartView; //��ʾͼ��
	static PublicMethod pMethod = new PublicMethod();
	static TextView textView_d;
	static GetTxtThread_d showThread_d;
	private static boolean updateflag;
	static Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View contactsLayout = inflater.inflate(R.layout.week,  
                container, false);  
        if (null == mFileObserver) {
			mFileObserver = new InFilesObserver(getActivity().getFilesDir().getAbsolutePath());
			mFileObserver.startWatching();
        }
        
        
        layout = (LinearLayout) contactsLayout.findViewById(R.id.tem2);
        textView_d = (TextView) contactsLayout.findViewById(R.id.drink);
        int date = pMethod.getDate();
        String drink = pMethod.readFromTxt(mContext,"Drinked"+date+".txt");
    	textView_d.setText(drink+" ml"); 
    	mChartView = draGraphicalViewDrink();
    	layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        showThread_d = new GetTxtThread_d();
        showThread_d.start();
        return contactsLayout;  
    } 
	
	public static class GetTxtThread_d extends Thread{
		public void run(){
			while(true)
			{
				synchronized (showThread_d) {
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
			// TODO �Զ����ɵķ������
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
	
	private static Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
        	int date = pMethod.getDate();
            if (msg.what == 1) {  
            	String drink = pMethod.readFromTxt(mContext,"Drinked"+date+".txt");
            	textView_d.setText(drink+" ml");            	
            }  
        }  
    }; 
	

	private GraphicalView draGraphicalViewDrink(){
		GraphicalView mChartView;
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        int min,max,hour;
        hour=pMethod.getHour();    //������������ʾ�ĳ�ʼ����
        if (hour>=7) {
			max=hour;
			min=hour-7;
		}
        else {
			min = 0;
			max = 7;
		} 
        Random r = new Random(); //��ʱ��ֵ
        for(int i=0;i<=24;i++)
        {
       	 pMethod.writeToTxt(getActivity(),i+":00.txt",r.nextInt()%100+"");
        }
	    XYSeries series = new XYSeries("");
	    // �������
	    for (int k = 0; k <= 24; k++) {
	        // ��x,yֵ
	        series.add(k, Double.valueOf(pMethod.readFromTxt(getActivity(), k+":00.txt"))); //20 + r.nextInt() % 100
	        renderer.addTextLabel(k,k+":00");               
	    }
	    // ��Ҫ���Ƶĵ�Ž�dataset��
	    dataset.addSeries(series);

        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();// �Ե�Ļ��ƽ�������
        xyRenderer.setColor(Color.GRAY);//������ɫ
        xyRenderer.setPointStyle(PointStyle.SQUARE);//���õ����ʽ
        xyRenderer.setDisplayChartValuesDistance(30);
        xyRenderer.setDisplayChartValues(true);
        renderer.addSeriesRenderer(xyRenderer);
        renderer.setXLabels(0);
        renderer.setYLabels(9);//����Y���ǩ�� 
        renderer.setXAxisMin(min);
        renderer.setXAxisMax(max);
        renderer.setShowLegend(false);
        renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        renderer.setZoomEnabled(false,false);
        renderer.setPanEnabled(true,false);
        renderer.setExternalZoomEnabled(false);
        renderer.setClickEnabled(false);
        renderer.setFitLegend(true);// �������ʵ�λ��
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.setChartTitleTextSize(0);
        renderer.setXLabelsColor(Color.BLACK);//����X��̶���ɫ
        renderer.setYLabelsColor(0, Color.BLACK);//����Y��̶���ɫ
        renderer.setYLabelsAlign(Align.RIGHT, 0);
        renderer.setAxesColor(Color.WHITE);//������������ɫ
        renderer.setLabelsColor(Color.WHITE);
        renderer.setAntialiasing(true); 
        renderer.setShowGridX(true);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return mChartView;
		
	}
}
