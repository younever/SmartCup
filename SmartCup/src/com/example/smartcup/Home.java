package com.example.smartcup;

import java.util.zip.Inflater;

import com.example.smartcup.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends Fragment {
	private TextView textView;
	private Today messageFragment;
    private Week contactsFragment;  
    private AllData newsFragment;  
    private Friend settingFragment;  
    private View messageLayout;   
    private View contactsLayout;  
    private View newsLayout;   
    private View settingLayout;  
    private ImageView messageImage;  
    private ImageView contactsImage;  
    private ImageView newsImage;  
    private ImageView settingImage;   
//    private TextView messageText;  
//    private TextView contactsText;  
//    private TextView newsText;  
//    private TextView settingText;  
    private FragmentManager fragmentManager; 
//    private FragmentManager mFmanager;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home,container,false);
		textView = (TextView) view.findViewById(R.id.textView1);
//		String text = "Home page";
//		textView.setText(text);
		
		messageLayout = view.findViewById(R.id.message_layout);  
		contactsLayout = view.findViewById(R.id.contacts_layout);  
        newsLayout = view.findViewById(R.id.news_layout);  
        settingLayout = view.findViewById(R.id.setting_layout);  
        messageImage = (ImageView) view.findViewById(R.id.message_image);  
        contactsImage = (ImageView) view.findViewById(R.id.contacts_image);  
        newsImage = (ImageView) view.findViewById(R.id.news_image);  
        settingImage = (ImageView) view.findViewById(R.id.setting_image);  
        
        
        messageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(0); 
			}
		});  
        contactsLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(1); 
			}
		});  
        newsLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(2); 
			}
		});  
        settingLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(3); 
			}
		}); 
		
		
 
        fragmentManager = getFragmentManager();  
        // 第一次启动时选中第1个tab  
        setTabSelection(2);
        setTabSelection(1); 
        setTabSelection(0);
        
		return view;	
		
	}
	  
    /** 
     * 根据传入的index参数来设置选中的tab页。 
     *  
     * @param index 
     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。 
     */  
    private void setTabSelection(int index) {  
        // 每次选中之前先清楚掉上次的选中状态  
        clearSelection();  
        // 开启一个Fragment事务  
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
        hideFragments(transaction);  
        switch (index) {  
        case 0:  
            // 当点击了消息tab时，改变控件的图片和文字颜色  
            
//            messageText.setTextColor(Color.parseColor("#82858b"));  
            if (messageFragment == null) {  
                // 如果MessageFragment为空，则创建一个并添 加到界面上  
                messageFragment = new Today();  
                transaction.add(R.id.content_frame, messageFragment);  
            } else {  
                // 如果MessageFragment不为空，则直接将它显示出来  
                transaction.show(messageFragment);  
            } 
            messageImage.setImageResource(R.drawable.temperture2);  
            break;  
        case 1:  
            // 当点击了联系人tab时，改变控件的图片和文字颜色  
            
//            contactsText.setTextColor(Color.parseColor("#82858b"));  
            if (contactsFragment == null) {  
                // 如果ContactsFragment为空，则创建一个并添加到界面上  
                contactsFragment = new Week();  
                transaction.add(R.id.content_frame, contactsFragment);  
            } else {  
                // 如果ContactsFragment不为空，则直接将它显示出来  
                transaction.show(contactsFragment);  
            }  
            contactsImage.setImageResource(R.drawable.cup2);  
            break;  
        case 2:  
            // 当点击了动态tab时，改变控件的图片和文字颜色  
            newsImage.setImageResource(R.drawable.smartcup1);  
//            newsText.setTextColor(Color.parseColor("#82858b"));  
            if (newsFragment == null) {  
                // 如果NewsFragment为空，则创建一个并添加到界面上  
                newsFragment = new AllData();  
                transaction.add(R.id.content_frame, newsFragment);  
            } else {  
                // 如果NewsFragment不为空，则直接将它显示出来  
                transaction.show(newsFragment);  
            } 
            newsImage.setImageResource(R.drawable.smartcup2);
            break;  
        case 3:  
        default:  
            // 当点击了设置tab时，改变控件的图片和文字颜色  
           
//            settingText.setTextColor(Color.parseColor("#82858b"));  
            if (settingFragment == null) {  
                // 如果SettingFragment为空，则创建一个并添加到界面上  
                settingFragment = new Friend();  
                transaction.add(R.id.content_frame, settingFragment);  
            } else {  
                // 如果SettingFragment不为空，则直接将它显示出来  
                transaction.show(settingFragment);  
            }  
            settingImage.setImageResource(R.drawable.private2);  
            break;  
        }  
        transaction.commit();  
    }  
  
    /** 
     * 清除掉所有的选中状态。 
     */  
    private void clearSelection() {  
        messageImage.setImageResource(R.drawable.temperture1);  
//        messageText.setTextColor(Color.WHITE);  
        contactsImage.setImageResource(R.drawable.cup1);  
//        contactsText.setTextColor(Color.WHITE);  
        newsImage.setImageResource(R.drawable.smartcup1);  
//        newsText.setTextColor(Color.WHITE);  
        settingImage.setImageResource(R.drawable.private1);  
//        settingText.setTextColor(Color.WHITE);  
    }  
  
    /** 
     * 将所有的Fragment都置为隐藏状态。 
     *  
     * @param transaction 
     *            用于对Fragment执行操作的事务 
     */  
    private void hideFragments(FragmentTransaction transaction) {  
        if (messageFragment != null) {  
            transaction.hide(messageFragment);  
        }  
        if (contactsFragment != null) {  
            transaction.hide(contactsFragment);  
        }  
        if (newsFragment != null) {  
            transaction.hide(newsFragment);  
        }  
        if (settingFragment != null) {  
            transaction.hide(settingFragment);  
        }  
    }  
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	
    }

}
