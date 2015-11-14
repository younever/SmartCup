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
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
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

public class AllData extends Fragment {
	
	
	LinearLayout layout;
	private GraphicalView mChartView; //��ʾͼ��
	static PublicMethod pMethod = new PublicMethod();
	public static TextView textView_en;
	public static TextView textView_cn;
//	static GetTxtThread showThread;
//	public static boolean updateflag;
	private FileObserver mFileObserver;
	static Context mContext ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View newsLayout = inflater.inflate(R.layout.alldata, container,  
                false);  
        textView_en = (TextView) newsLayout.findViewById(R.id.textView1);
        textView_cn = (TextView) newsLayout.findViewById(R.id.textView2);
        
        int hour = pMethod.getHour();
        String pathString = mContext.getFilesDir().getAbsolutePath() + "/" + "Type"+hour+".txt";
		File file = new File(pathString);
		if (!file.exists())
		{
			pMethod.writeToTxt(mContext, "Type"+hour+".txt", "δ֪");
		}
		showInit();
        if (null == mFileObserver) {
        	String path = getActivity().getFilesDir().getAbsolutePath()+"/Type"+hour+".txt";
			mFileObserver = new InFilesObserver(path);
			mFileObserver.startWatching();
        }
        
        
        mChartView = draGraphicalViewType();
        layout = (LinearLayout) newsLayout.findViewById(R.id.tem3);
        layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
//        
        return newsLayout;  
    } 
	@Override
	public void onResume() {
		// TODO �Զ����ɵķ������
		super.onResume();
		mFileObserver.stopWatching();
		int hour = pMethod.getHour();
		String path = getActivity().getFilesDir().getAbsolutePath()+"/Type"+hour+".txt";
		mFileObserver = new InFilesObserver(path);
		mFileObserver.startWatching();
		Message message = new Message();
		message.what = 3;
		handler.sendMessage(message);
        
	}
	
//	@Override
//	public void onDestroy() {
//		if(null != mFileObserver) mFileObserver.stopWatching();
//	}
	
	
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
			// TODO �Զ����ɵķ������
			final int action = event&FileObserver.ALL_EVENTS;
			switch (action) {
			case FileObserver.MODIFY:
//				updateflag = true;
				Message msg = new Message();
				msg.what = 3;
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
            if (msg.what == 3) {  
            	String type = pMethod.readFromTxt(mContext,"Type"+hour+".txt");
            	Log.e("type", type);
            	switch (type) {
				case "ORANGE":
				case "orange":
					textView_en.setText("ORANGE");
					textView_cn.setText("���ʳ�֭");
					break;
				case "APPLE":
				case "apple":
					textView_en.setText("APPLE JUICE");
					textView_cn.setText("����ƻ��֭");
					break;
				case "TEA":
				case "tea":
					textView_en.setText("TEA");
					textView_cn.setText("��");
					break;
				case "coffee":
				case "COFFEE":
					textView_en.setText("COFFEE");
					textView_cn.setText("����");
					break;
				case "COCACOLA":
				case "cocacola":
					textView_en.setText("COCACOLA");
					textView_cn.setText("�ɿڿ���");
					break;
				default:
					textView_en.setText("Unknown");
					textView_cn.setText("δ֪��Ʒ");
					break;
				}
            	layout.removeView(mChartView);
            	mChartView = draGraphicalViewType();
                layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
            }  
        }  
    }; 
//    
    private void showInit(){
    	
    	int hour = pMethod.getHour();
    	String type = pMethod.readFromTxt(mContext,"Type"+hour+".txt");
    	switch (type) {
		case "ORANGE":
		case "orange":
			textView_en.setText("ORANGE");
			textView_cn.setText("���ʳ�֭");
			break;
		case "APPLE":
		case "apple":
			textView_en.setText("APPLE JUICE");
			textView_cn.setText("����ƻ��֭");
			break;
		case "TEA":
		case "tea":
			textView_en.setText("TEA");
			textView_cn.setText("��");
			break;
		case "coffee":
		case "COFFEE":
			textView_en.setText("COFFEE");
			textView_cn.setText("����");
			break;
		case "COCACOLA":
		case "cocacola":
			textView_en.setText("COCACOLA");
			textView_cn.setText("�ɿڿ���");
			break;
		default:
			textView_en.setText("Unknown");
			textView_cn.setText("δ֪��Ʒ");
			break;
		}
    }

	
	private GraphicalView draGraphicalViewType(){
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
	    XYSeries series = new XYSeries("");
	    // �������
	    int Calorie;
	    for (int k = 0; k < 24; k++) {
	        // ��x,yֵ
	    	switch (pMethod.readFromTxt(getActivity(), "Type"+k+".txt")) {
			case "TEA":
			case "tea":
				Calorie = 100;
				break;
			case "APPLE":
			case "apple":
				Calorie = 60;
				break;
			case "COCACOLA":
			case "cocacola":
				Calorie = 80;
				break;
			case "COFFEE":
			case "coffee":
				Calorie = 120;
				break;
			case "ORANGE":
			case "orange":
				Calorie = 20;
				break;
			default:
				Calorie = 0;
				break;
			}
	        series.add(k, Calorie); //20 + r.nextInt() % 100
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
        renderer.setLabelsTextSize(15);
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
