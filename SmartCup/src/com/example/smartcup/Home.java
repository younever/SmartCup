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
    private TextView messageText;  
    private TextView contactsText;  
    private TextView newsText;  
    private TextView settingText;  
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
        messageText = (TextView) view.findViewById(R.id.message_text);  
        contactsText = (TextView) view.findViewById(R.id.contacts_text);  
        newsText = (TextView) view.findViewById(R.id.news_text);  
        settingText = (TextView) view.findViewById(R.id.setting_text);  
        
//        mFmanager = getFragmentManager();
//        if (savedInstanceState != null) {
//        	messageFragment = (Today) mFmanager.findFragmentByTag("messageFragment"); 
//        	contactsFragment = (Week) mFmanager.findFragmentByTag("contactsFragment"); 
//        	newsFragment = (AllData) mFmanager.findFragmentByTag("newsFragment"); 
//        	settingFragment = (Friend) mFmanager.findFragmentByTag("settingFragment"); 
//		}
//        super.onCreate(savedInstanceState); 
        
        
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
        // ��һ������ʱѡ�е�0��tab  
        setTabSelection(0);  
		return view;	
		
	}
	  
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// TODO Auto-generated method stub
//		Log.v("LH","onSaveInstanceState"+outState);
//		super.onSaveInstanceState(outState);
//	}
	


	
	
/*    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.message_layout:  
            // ���������Ϣtabʱ��ѡ�е�1��tab  
            setTabSelection(0);  
            break;  
        case R.id.contacts_layout:  
            // ���������ϵ��tabʱ��ѡ�е�2��tab  
            setTabSelection(1);  
            break;  
        case R.id.news_layout:  
            // ������˶�̬tabʱ��ѡ�е�3��tab  
            setTabSelection(2);  
            break;  
        case R.id.setting_layout:  
            // �����������tabʱ��ѡ�е�4��tab  
            setTabSelection(3);  
            break;  
        default:  
            break;  
        }  
    }  
  */
    /** 
     * ���ݴ����index����������ѡ�е�tabҳ�� 
     *  
     * @param index 
     *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��Ϣ��1��ʾ��ϵ�ˣ�2��ʾ��̬��3��ʾ���á� 
     */  
    private void setTabSelection(int index) {  
        // ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬  
        clearSelection();  
        // ����һ��Fragment����  
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        // �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����  
        hideFragments(transaction);  
        switch (index) {  
        case 0:  
            // ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ  
            messageImage.setImageResource(R.drawable.today1);  
            messageText.setTextColor(Color.WHITE);  
            if (messageFragment == null) {  
                // ���MessageFragmentΪ�գ��򴴽�һ������ �ӵ�������  
                messageFragment = new Today();  
                transaction.add(R.id.content_frame, messageFragment);  
            } else {  
                // ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(messageFragment);  
            }  
            break;  
        case 1:  
            // ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ  
            contactsImage.setImageResource(R.drawable.week1);  
            contactsText.setTextColor(Color.WHITE);  
            if (contactsFragment == null) {  
                // ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������  
                contactsFragment = new Week();  
                transaction.add(R.id.content_frame, contactsFragment);  
            } else {  
                // ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(contactsFragment);  
            }  
            break;  
        case 2:  
            // ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ  
            newsImage.setImageResource(R.drawable.year1);  
            newsText.setTextColor(Color.WHITE);  
            if (newsFragment == null) {  
                // ���NewsFragmentΪ�գ��򴴽�һ������ӵ�������  
                newsFragment = new AllData();  
                transaction.add(R.id.content_frame, newsFragment);  
            } else {  
                // ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(newsFragment);  
            }  
            break;  
        case 3:  
        default:  
            // �����������tabʱ���ı�ؼ���ͼƬ��������ɫ  
            settingImage.setImageResource(R.drawable.huihua1);  
            settingText.setTextColor(Color.WHITE);  
            if (settingFragment == null) {  
                // ���SettingFragmentΪ�գ��򴴽�һ������ӵ�������  
                settingFragment = new Friend();  
                transaction.add(R.id.content_frame, settingFragment);  
            } else {  
                // ���SettingFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(settingFragment);  
            }  
            break;  
        }  
        transaction.commit();  
    }  
  
    /** 
     * ��������е�ѡ��״̬�� 
     */  
    private void clearSelection() {  
        messageImage.setImageResource(R.drawable.today);  
        messageText.setTextColor(Color.parseColor("#82858b"));  
        contactsImage.setImageResource(R.drawable.week);  
        contactsText.setTextColor(Color.parseColor("#82858b"));  
        newsImage.setImageResource(R.drawable.year);  
        newsText.setTextColor(Color.parseColor("#82858b"));  
        settingImage.setImageResource(R.drawable.huihua);  
        settingText.setTextColor(Color.parseColor("#82858b"));  
    }  
  
    /** 
     * �����е�Fragment����Ϊ����״̬�� 
     *  
     * @param transaction 
     *            ���ڶ�Fragmentִ�в��������� 
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
