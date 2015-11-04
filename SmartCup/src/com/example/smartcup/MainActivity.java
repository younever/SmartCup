package com.example.smartcup;

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
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
    private long exitTime = 0;
	private Object activity;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
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
        
       startService(new Intent(MainActivity.this,ReceiveService.class));
        
       
        
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
		list.add(new ContentModel(R.drawable.temp, "预留"));
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		Toast.makeText(mContext, "address", Toast.LENGTH_SHORT);
		if (resultCode==Activity.RESULT_OK) {
			String result = data.getExtras().getString("result");
			Toast.makeText(this, result, 1).show();
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
	
}
