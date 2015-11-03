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
	private GraphicalView mChartView; //显示图表
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View contactsLayout = inflater.inflate(R.layout.week,  
                container, false);  
//        setContentView(R.layout.activity_main);
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        // 2,进行显示
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // 2.1, 构建数据
        Random r = new Random();
        for (int i = 0; i < 1; i++) {
            XYSeries series = new XYSeries("");
            // 填充数据
            for (int k = 1; k < 8; k++) {
                // 填x,y值
                series.add(k, 20 + r.nextInt() % 100); //20 + r.nextInt() % 100
            }
            // 需要绘制的点放进dataset中
            dataset.addSeries(series);
        }
        // 3, 对点的绘制进行设置
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        // 3.1设置颜色
        xyRenderer.setColor(Color.GRAY);
        // 3.2设置点的样式
        xyRenderer.setPointStyle(PointStyle.SQUARE);
        // 3.3, 将要绘制的点添加到坐标绘制中
//        xyRenderer.setFillBelowLine(true);
//        xyRenderer.setFillBelowLineColor(Color.RED);
        xyRenderer.setDisplayChartValuesDistance(30);
        xyRenderer.setDisplayChartValues(true);
        renderer.addSeriesRenderer(xyRenderer);
        renderer.setXLabels(7);
        renderer.setYLabels(9);//设置Y轴标签数 
        renderer.setShowLegend(false);
//        renderer.setApplyBackgroundColor(true);
//        renderer.setBackgroundColor(Color.GREEN);
        renderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        renderer.setZoomEnabled(false,false);
        renderer.setPanEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setClickEnabled(false);
        renderer.setFitLegend(true);// 调整合适的位置
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        renderer.setChartTitleTextSize(0);
        renderer.setXLabelsColor(Color.BLACK);//设置X轴刻度颜色
        renderer.setYLabelsColor(0, Color.BLACK);//设置Y轴刻度颜色
        renderer.setYLabelsAlign(Align.RIGHT, 0);
//        renderer.setYTitle("全天喝水量");
//        renderer.setYTitle("sss", Color.BLUE);
        renderer.setAxesColor(Color.WHITE);//设置坐标轴颜色
        renderer.setLabelsColor(Color.WHITE);
        renderer.setAntialiasing(true); 
//        renderer.setGridColor(Color.BLUE);
//        renderer.setShowGrid(true);//设置是否在图表中显示网格
        
        
        
        layout = (LinearLayout) contactsLayout.findViewById(R.id.tem2);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
        layout.addView(mChartView,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        return contactsLayout;  
    }  
}
