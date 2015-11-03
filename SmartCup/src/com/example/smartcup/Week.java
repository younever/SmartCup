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

public class Week extends Fragment {
	
	LinearLayout layout;
	private GraphicalView mChartView; //��ʾͼ��
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View contactsLayout = inflater.inflate(R.layout.week,  
                container, false);  
//        setContentView(R.layout.activity_main);
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        // 2,������ʾ
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // 2.1, ��������
        Random r = new Random();
        for (int i = 0; i < 1; i++) {
            XYSeries series = new XYSeries("");
            // �������
            for (int k = 1; k < 8; k++) {
                // ��x,yֵ
                series.add(k, 20 + r.nextInt() % 100); //20 + r.nextInt() % 100
            }
            // ��Ҫ���Ƶĵ�Ž�dataset��
            dataset.addSeries(series);
        }
        // 3, �Ե�Ļ��ƽ�������
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        // 3.1������ɫ
        xyRenderer.setColor(Color.GRAY);
        // 3.2���õ����ʽ
        xyRenderer.setPointStyle(PointStyle.SQUARE);
        // 3.3, ��Ҫ���Ƶĵ���ӵ����������
//        xyRenderer.setFillBelowLine(true);
//        xyRenderer.setFillBelowLineColor(Color.RED);
        xyRenderer.setDisplayChartValuesDistance(30);
        xyRenderer.setDisplayChartValues(true);
        renderer.addSeriesRenderer(xyRenderer);
        renderer.setXLabels(7);
        renderer.setYLabels(9);//����Y���ǩ�� 
        renderer.setShowLegend(false);
//        renderer.setApplyBackgroundColor(true);
//        renderer.setBackgroundColor(Color.GREEN);
        renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        renderer.setZoomEnabled(false,false);
        renderer.setPanEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setClickEnabled(false);
        renderer.setFitLegend(true);// �������ʵ�λ��
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.setChartTitleTextSize(0);
        renderer.setXLabelsColor(Color.BLACK);//����X��̶���ɫ
        renderer.setYLabelsColor(0, Color.BLACK);//����Y��̶���ɫ
        renderer.setYLabelsAlign(Align.RIGHT, 0);
//        renderer.setYTitle("ȫ���ˮ��");
//        renderer.setYTitle("sss", Color.BLUE);
        renderer.setAxesColor(Color.WHITE);//������������ɫ
        renderer.setLabelsColor(Color.WHITE);
        renderer.setAntialiasing(true); 
//        renderer.setGridColor(Color.BLUE);
//        renderer.setShowGrid(true);//�����Ƿ���ͼ������ʾ����
        
        
        
        layout = (LinearLayout) contactsLayout.findViewById(R.id.tem2);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
        layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        return contactsLayout;  
    }  
}
