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
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Week extends Fragment {
	
	LinearLayout layout;
	private GraphicalView mChartView; //��ʾͼ��
	PublicMethod pMethod = new PublicMethod();
	TextView textView;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View contactsLayout = inflater.inflate(R.layout.week,  
                container, false);  
        mChartView = draGraphicalViewDrink();
        layout = (LinearLayout) contactsLayout.findViewById(R.id.tem2);
        layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        return contactsLayout;  
    } 

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
