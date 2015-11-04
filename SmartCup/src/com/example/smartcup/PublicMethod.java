package com.example.smartcup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import android.R.integer;
import android.content.Context;
import android.text.format.Time;


public class PublicMethod {
	
	private int year;
	private int month;
	private int date;
	private int hour;
	private int minute;
	private int second;
	
	
	public int getYear()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.year;
	}
	public int getMonth()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.month+1;
	}
	public int getDate()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.monthDay;
	}
	public int getHour()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.hour;
	}
	public int getMinute()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.minute;
	}
	public int getSecond()
	{
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		t.setToNow(); // 取得系统时间。  
		return t.second;
	}
	
	public  void writeToTxt(Context context,String filename,String data)
	{
		FileOutputStream fos1;
		try {
			fos1 = context.openFileOutput(filename,Context.MODE_PRIVATE );
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
	
	public String readFromTxt(Context context,String filename) {
		String readed1 = null;
		try {
			FileInputStream fis1 = context.openFileInput(filename);
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
	
	public int getMonthMax() {
		int month = getMonth();
		int year  = getYear();
		int monthMax=30;
		if (year%100==0&&year%400!=0&&month==2) {
			monthMax = 28;
		}
		else if (year%400==0&&month==2) {
			monthMax = 29;
		}
		else if (year%100!=0&&month==2&&year%4==0) {
			monthMax = 29;
		}
		else if (year%4!=0) {
			monthMax = 28;
		}
		if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
			monthMax = 31;
		}
		if (month==4||month==6||month==9||month==11) {
			monthMax = 30;
		}
		return monthMax;
	}
	
}
