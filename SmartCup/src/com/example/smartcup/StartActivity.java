package com.example.smartcup;

import java.util.Timer;
import java.util.TimerTask;

import com.example.smartcup.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.startactivity);
		
		Thread mThread = new Thread();
		mThread.start();		
	

		Timer timer=new Timer();
		timer.schedule(new TimerTask() {
	
		@Override
		public void run() {
		// TODO Auto-generated method stub
		Intent it=new Intent(StartActivity.this,MainActivity.class);
		startActivity(it);
		finish();
		}
		}, 4000);
	
	//	private void run() {
//		// TODO Auto-generated method stub
//		try {
//			mThread.sleep(3000);
//			Intent intent = new Intent();
//			intent.setClass(StartActivity.this, MainActivity.class);
//			startActivity(intent);
//			} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
	}
}
