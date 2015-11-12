package com.example.smartcup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




















import com.example.smartcup.ContentModel;
import com.example.smartcup.R;
import com.example.smartcup.R.style;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData.Item;
import android.content.Loader.ForceLoadContentObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


public class MainActivity extends Activity implements OnItemClickListener{

	enum ServerOrCilent{
		NONE,
		SERVICE,
		CILENT
	};
	
	private Context mContext;
	static String BlueToothAddress = "null";
	static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;
	static boolean isOpen = false;
	
	static String fileName_data = "allToday.txt";
	static String fileName_Temp = "tempreture.txt";
	
    private DrawerLayout mDrawerLayout ;
    private ListView mDrawerList;
    //private ArrayList<String> muneLists;
    //private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;
	
    private DrawerLayout drawerLayout;
    private List<ContentModel> list;
    private ContentAdapter adapter2;
    private static Boolean isExit = false;
    private Object activity;
	private boolean qrReady=false;

	public PublicMethod publicMethod;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);          //
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(this.getBaseContext().getResources().getDrawable(R.drawable.BackBar));
        getActionBar().show();
        
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		mContext = this;
        drawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        initData();
        adapter2 = new ContentAdapter(this, list);
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(adapter2);
        mDrawerList.setOnItemClickListener(this);  
        
       	
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.menu, R.string.Drawer_open, R.string.Drawer_close){
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		// TODO Auto-generated method stub
        		super.onDrawerOpened(drawerView);
        		getActionBar().setTitle("请选择");
        		invalidateOptionsMenu();  //Call onPrepareOptionsMenu();
        		
        	}
        	
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		// TODO Auto-generated method stub
        		super.onDrawerClosed(drawerView);
        		getActionBar().setTitle(R.string.app_name);
        		invalidateOptionsMenu();  //Call onPrepareOptionsMenu();
        	}
        }; 
       
        initTxt();
		
        String pathString = mContext.getFilesDir().getAbsolutePath() + "/" + "BlueToothAddress.txt" ;
		File addressFile = new File(pathString);
		if (!addressFile.exists()) {
			
			Builder ConnetDialog = new AlertDialog.Builder(this);
			ConnetDialog.setTitle("SmartCup");
			ConnetDialog.setMessage("是否进行二维码连接");
			ConnetDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					serviceOrCilent=ServerOrCilent.CILENT;
					Intent intent_qr = new Intent(mContext,CaptureActivity.class);
					startActivityForResult(intent_qr, 1);
				}
			});
			ConnetDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 自动生成的方法存根
					Toast.makeText(mContext, "请手动添加智能水杯", 1).show();
				}
			});
			ConnetDialog.show();
		}
		else {
			serviceOrCilent=ServerOrCilent.CILENT;
			try {
				FileInputStream fis1 = mContext.openFileInput("BlueToothAddress.txt");
				InputStreamReader is1 = new InputStreamReader(fis1, "UTF-8");
				char input1[] = new char[fis1.available()];
				is1.read(input1);
				is1.close();
				fis1.close();
				BlueToothAddress = new String(input1);
				startService(new Intent(MainActivity.this,ReceiveService.class));//000000000000000000
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
        
       
       
        Fragment contentFragment1 = new Home();			   //直接默认启动Home Page
		FragmentManager fm1 = getFragmentManager();
		fm1.beginTransaction().addToBackStack(null).replace(R.id.content_frame,contentFragment1).commit();
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);       
    }
	
	private void initData() {
		list=new ArrayList<ContentModel>();	
		list.add(new ContentModel(R.drawable.home, "主页"));
		list.add(new ContentModel(R.drawable.set, "设备列表"));
		list.add(new ContentModel(R.drawable.chat, "详细数据"));
		list.add(new ContentModel(R.drawable.scan, "二维码扫描"));
		list.add(new ContentModel(R.drawable.search, "公司主页"));
		list.add(new ContentModel(R.drawable.temp, "重置地址"));
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Toast.makeText(mContext, "address", Toast.LENGTH_SHORT);
		if (resultCode==Activity.RESULT_OK) {
			String result = data.getExtras().getString("result");
			if (result.substring(0,2).equals("Qr")&&result.substring(4, 5).equals(":")) {
				qrReady = true;
				BlueToothAddress = result.substring(2);
				serviceOrCilent=ServerOrCilent.CILENT;
				startService(new Intent(MainActivity.this,ReceiveService.class));//000000000000000000
				Toast.makeText(this, "地址成功获取", Toast.LENGTH_SHORT).show();
//				publicMethod.writeToTxt(mContext,"BlueToothAddress.txt" , "BlueToothAddress.txt");
				FileOutputStream fos1;
				try {
					fos1 = mContext.openFileOutput("BlueToothAddress.txt",Context.MODE_PRIVATE );
					OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
					osw1.write(BlueToothAddress);
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
			else {
				qrReady = false;	
				Toast.makeText(this, "非蓝牙地址", 1).show();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	 
	public class SiriListItem {
		String message;
		boolean isSiri;

		public SiriListItem(String msg, boolean siri) {
			message = msg;
			isSiri = siri;
		}
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(mDrawerToggle.onOptionsItemSelected(item))
			return true;
//		switch (item.getItemId()) {
//		case R.id.action_webserch:
//			Intent intent = new Intent();
//			intent.setAction("android.intent.action.VIEW");
//			Uri uri = Uri.parse("http://www.baidu.com");
//			intent.setData(uri);
//			startActivity(intent);
//			break;
//		case R.id.action_qrscan:
//			Intent intent_qr = new Intent(this,CaptureActivity.class);
//			startActivityForResult(intent_qr, 1);
//			break;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		boolean isDrawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_webserch).setVisible(!isDrawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		switch (position) {
		case 0:
			Fragment contentFragment1 = new Home();			
			FragmentManager fm1 = getFragmentManager();
			fm1.beginTransaction().replace(R.id.content_frame,contentFragment1).commit();
			break;
		case 1:			
			startActivity(new Intent(MainActivity.this,deviceActivity.class) );
			break;
		case 2:
			startActivity(new Intent(MainActivity.this,chatActivity.class));
			break;
		case 3:
			Intent intent_qr = new Intent(this,CaptureActivity.class);
			startActivityForResult(intent_qr, 1);
			break;
		case 4:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse("http://www.baidu.com");
			intent.setData(uri);
			startActivity(intent);
			break;
		case 5:
			String pathString = mContext.getFilesDir().getAbsolutePath() + "/" + "BlueToothAddress.txt" ;
			File addressFile = new File(pathString);
			if (addressFile.exists()) {
				addressFile.delete();
				Toast.makeText(mContext, "已经删除", Toast.LENGTH_LONG);
			}
			
		}

		mDrawerLayout.closeDrawer(mDrawerList);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{ 
		  exitBy2Click(); //调用双击退出函数
		}
		 return false;
	}
		 
	private void exitBy2Click() {
	 Timer tExit = null;
	 if (isExit == false) {
		 isExit = true; // 准备退出
		 Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		 tExit = new Timer();
		 tExit.schedule(new TimerTask() {
		  @Override
		  public void run() {
		  isExit = false; // 取消退出
		  }
	 }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
	 
	 } 
	 else {
		 finish();
		 System.exit(0);
	 }
	}
	private void initTxt(){
		for (int i = 0; i < 24; i++) {
			FileOutputStream fos1;
			FileOutputStream fos2;
			try {
				fos1 = mContext.openFileOutput("Type"+i+".txt",Context.MODE_PRIVATE );
				fos2 = mContext.openFileOutput("Temperture"+i+".txt", Context.MODE_PRIVATE);
				OutputStreamWriter osw1 = new OutputStreamWriter(fos1, "UTF-8");
				OutputStreamWriter osw2 = new OutputStreamWriter(fos2,"UTF-8");
				osw1.write("0");
				osw1.flush();
				fos1.flush();
				osw1.close();
				fos1.close();
				osw2.write("0");
				osw2.flush();
				fos2.flush();
				osw2.close();
				fos2.close();
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
			
//			publicMethod.writeToTxt(mContext, "Type"+i+".txt", "0");
//			publicMethod.writeToTxt(mContext, "Temperture"+i+".txt", "0");
		}
		for (int t = 0; t < 31; t++) {
//			publicMethod.writeToTxt(mContext, "Drink"+t+".txt", "0");
			FileOutputStream fos3;
			try {
				fos3 = mContext.openFileOutput("Drinked"+t+".txt",Context.MODE_PRIVATE );
				OutputStreamWriter osw3 = new OutputStreamWriter(fos3, "UTF-8");
				osw3.write("0");
				osw3.flush();
				fos3.flush();
				osw3.close();
				fos3.close();
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
		
	}
	
	
}
