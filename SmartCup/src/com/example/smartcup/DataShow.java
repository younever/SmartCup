package com.example.smartcup;

import com.example.smartcup.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataShow extends Fragment {
	
	private TextView textView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.datashow, container,false);
		textView = (TextView) view.findViewById(R.id.textView1);
		textView.setText("Data page");
		
		
		return view;
	}

}
