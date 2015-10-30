package com.example.smartcup;

import com.example.smartcup.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Device extends Fragment {
	private TextView textView ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.device, container,false);
		textView  = (TextView) view.findViewById(R.id.textView1);
		textView.setText("Device page");
		
		return view;
	}

}

//public class Home extends Fragment {
//	private TextView textView;
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		View view = inflater.inflate(R.layout.home,container,false);
//		textView = (TextView) view.findViewById(R.id.textView1);
//		String text = "Home page";
//		//text = getArguments().getString("data");
//		textView.setText(text);
//		return view;
//		
//	}
//}
