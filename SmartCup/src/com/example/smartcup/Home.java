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
	private Temperature temperatureFragment;
    private Drinked drinkedFragment;  
    private Type typeFragment;  
    private Friend settingFragment;  
    private View temperatureLayout;
    private View drinkedlayout ;
    private View typeLayout;   
    private View settingLayout;  
    private ImageView messageImage;  
    private ImageView contactsImage;  
    private ImageView newsImage;  
    private ImageView settingImage;   
    private FragmentManager fragmentManager; 
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home,container,false);
		
		textView = (TextView) view.findViewById(R.id.textView1);
		temperatureLayout = view.findViewById(R.id.temperature_layout);  
		drinkedlayout = view.findViewById(R.id.drinked_layout);  
        typeLayout = view.findViewById(R.id.type_layout);  
        settingLayout = view.findViewById(R.id.setting_layout);  
        messageImage = (ImageView) view.findViewById(R.id.temperature_image);  
        contactsImage = (ImageView) view.findViewById(R.id.drinked_image);  
        newsImage = (ImageView) view.findViewById(R.id.type_image);  
        settingImage = (ImageView) view.findViewById(R.id.setting_image);  
        
        
        temperatureLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(0); 
			}
		});  
        drinkedlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTabSelection(1); 
			}
		});  
        typeLayout.setOnClickListener(new OnClickListener() {
			
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
        // ��һ������ʱѡ�е�1��tab  
        setTabSelection(2);
        setTabSelection(1); 
        setTabSelection(0);
        
		return view;	
		
	}
	  
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
            
//            messageText.setTextColor(Color.parseColor("#82858b"));  
            if (temperatureFragment == null) {  
                // ���MessageFragmentΪ�գ��򴴽�һ������ �ӵ�������  
            	temperatureFragment = new Temperature();  
                transaction.add(R.id.content_frame, temperatureFragment);  
            } else {  
                // ���MessageFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(temperatureFragment);  
            } 
            messageImage.setImageResource(R.drawable.temperture2);  
            break;  
        case 1:  
            // ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ  
            
//            contactsText.setTextColor(Color.parseColor("#82858b"));  
            if (drinkedFragment == null) {  
                // ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������  
            	drinkedFragment = new Drinked();  
                transaction.add(R.id.content_frame, drinkedFragment);  
            } else {  
                // ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(drinkedFragment);  
            }  
            contactsImage.setImageResource(R.drawable.cup2);  
            break;  
        case 2:  
            // ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ  
            newsImage.setImageResource(R.drawable.smartcup1);  
//            newsText.setTextColor(Color.parseColor("#82858b"));  
            if (typeFragment == null) {  
                // ���NewsFragmentΪ�գ��򴴽�һ������ӵ�������  
            	typeFragment = new Type();  
                transaction.add(R.id.content_frame, typeFragment);  
            } else {  
                // ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(typeFragment);  
            } 
            newsImage.setImageResource(R.drawable.smartcup2);
            break;  
        case 3:  
        default:  
            // �����������tabʱ���ı�ؼ���ͼƬ��������ɫ  
           
//            settingText.setTextColor(Color.parseColor("#82858b"));  
            if (settingFragment == null) {  
                // ���SettingFragmentΪ�գ��򴴽�һ������ӵ�������  
                settingFragment = new Friend();  
                transaction.add(R.id.content_frame, settingFragment);  
            } else {  
                // ���SettingFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
                transaction.show(settingFragment);  
            }  
            settingImage.setImageResource(R.drawable.private2);  
            break;  
        }  
        transaction.commit();  
    }  
  
    /** 
     * ��������е�ѡ��״̬�� 
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
     * �����е�Fragment����Ϊ����״̬�� 
     *  
     * @param transaction 
     *            ���ڶ�Fragmentִ�в��������� 
     */  
    private void hideFragments(FragmentTransaction transaction) {  
        if (temperatureFragment != null) {  
            transaction.hide(temperatureFragment);  
        }  
        if (drinkedFragment != null) {  
            transaction.hide(drinkedFragment);  
        }  
        if (typeFragment != null) {  
            transaction.hide(typeFragment);  
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
