package com.example.smartcup;

import com.example.smartcup.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Friend extends Fragment {
	 @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View settingLayout = inflater.inflate(R.layout.friend,  
	                container, false);  
	        return settingLayout;  
	    }
}
