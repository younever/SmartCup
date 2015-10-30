package com.example.smartcup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.example.smartcup.R;

import android.app.Fragment;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Today extends Fragment {
	public Today() {
		// TODO 自动生成的构造函数存根
	}
	private TextView textData;
	private TextView textTemp;
	private Button btnFresh;
		
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View todayData = inflater.inflate(R.layout.today, container, false);  
        
        textData = (TextView) todayData.findViewById(R.id.todaydata);
        textTemp = (TextView) todayData.findViewById(R.id.tempreture);
        btnFresh = (Button) todayData.findViewById(R.id.fresh);

        btnFresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				textData.setText(readFromTxt("allToday.txt"));
			}
		});
        
        
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
