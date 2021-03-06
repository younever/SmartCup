package com.example.smartcup;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import com.example.smartcup.chatActivity;
import com.example.smartcup.MainActivity.ServerOrCilent;
import com.example.smartcup.chatActivity.deviceListItem;
import com.zxing.activity.CaptureActivity;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class ReceiveService extends Service {

	private static clientThread clientConnectThread = null;
	readThread mreadThread = null;;
	Context mContext;
	PublicMethod publicMethod =  new PublicMethod();
	int reConnect = 0;
	int firstConnect = 0;
	
	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
	
		super.onCreate();
		mContext = this;

	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根	
		
		chatActivity.address = MainActivity.BlueToothAddress;
		if (!chatActivity.address.equals("null")) {
			chatActivity.remoteDevice = chatActivity.mBluetoothAdapter.getRemoteDevice(chatActivity.address);
			// ////////////////--------注意连接服务器
			try {
				System.out.println(chatActivity.address);
				if (chatActivity.remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
					pair(chatActivity.address, "1234");
//					Message msg2 = new Message();                //0000000000000000000000
//					msg2.obj = "自动配对成功,请稍等";
//					msg2.what = 0;
//					chatActivity.LinkDetectedHandler.sendMessage(msg2);
//					Toast.makeText(this,"自动配对成功,请稍等" , Toast.LENGTH_LONG);
				} else if (chatActivity.remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
					clientConnectThread = new clientThread();
					clientConnectThread.start();
					MainActivity.isOpen = true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			//Toast.makeText(mContext, "address is null !",          ////4444444444444
			//		Toast.LENGTH_SHORT).show();
		}

		return super.onStartCommand(intent, flags, startId);
	}
	

	
	// 开启客户端
		private class clientThread extends Thread {
			public void run() {
				while(firstConnect==0)
				{
					try {
						sleep(1000);
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					try {
						System.out.println("匹配");
						// 创建一个Socket连接：只需要服务器在注册时的UUID号
						// socket =
						// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
						chatActivity.socket = chatActivity.remoteDevice.createRfcommSocketToServiceRecord(UUID
								.fromString("00001101-0000-1000-8000-00805F9B34FB"));
						Log.d("mylog", "正在连接水杯");
						chatActivity.socket.connect();
						firstConnect = 1;
						// 启动接受数据
						mreadThread = new readThread();
						mreadThread.start();
					} catch (Exception e) {
//						Log.e("connect", "", e);
						Log.e("errorConnect", "连接服务端异常！断开连接重新试一试。");
						firstConnect = 0;

//						Toast.makeText(mContext, "水杯端连接异常，无法连接", Toast.LENGTH_LONG);
					}
					
				}
				
			}
		};
		// 读取数据
		public class readThread extends Thread {
			public void run() {

				byte[] buffer = new byte[1024];
				int bytes;
				InputStream mmInStream = null;

				try {
					mmInStream = chatActivity.socket.getInputStream();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					
					e1.printStackTrace();
				}

				while (true) {
					if (reConnect ==1) {
						try {
							sleep(1000);
							chatActivity.socket.connect();
							reConnect = 0;
						} catch (IOException | InterruptedException e) {
							// TODO 自动生成的 catch 块
							reConnect = 1;
							e.printStackTrace();
						}
					}

					try {
						if ((bytes = mmInStream.read(buffer)) > 0) {
							Log.d("mylog", "接受到数据");
							byte[] buf_data = new byte[bytes];
							for (int i = 0; i < bytes; i++) {
								buf_data[i] = buffer[i];
							}
							String s = new String(buf_data);
//							int year = publicMethod.getYear();
							int date = publicMethod.getDate();
							int month = publicMethod.getMonth();
							int hour = publicMethod.getHour();
							if(s.length()>6)
							{
								switch (s.substring(0, 6)) {
								case "TTTTTT":          //T1234T 是蓝牙发送过来的关于温度的标志位
									String temperature = s.substring(6);
									try {
										double temperatureDoube = Double.valueOf(temperature).doubleValue();
										if (temperatureDoube>=-40.0&&temperatureDoube<=100.0) {
											publicMethod.writeToTxt(getApplicationContext(),"Temperture"+hour+".txt" , temperatureDoube+"");
											sendMessageHandle("Temperature");
										}
										else {
											sendMessageHandle("温度范围错误");
										}
									} catch (NumberFormatException e) {
										// TODO: handle exception
										sendMessageHandle("温度格式不正确，包含不合法字符");
									}

									break;
								case "PPPPPP":						//P4321P 是蓝牙模块发送过来关于饮品种类的标志位
									publicMethod.writeToTxt(getApplicationContext(), "Type"+hour+".txt", s.substring(6));
									sendMessageHandle("Type");
									break;
								case "DDDDDD":						//D5678D 是蓝牙模块发送过来关于当前喝水量的标志位
									try {
										String waterDrinkedString = publicMethod.readFromTxt(mContext, "Drinked"+date+".txt");
										int waterDrinkedInt  = Integer.valueOf(waterDrinkedString).intValue();
										int waterDrinkingInt = Integer.valueOf(s.substring(6)).intValue()+waterDrinkedInt;
										publicMethod.writeToTxt(getApplicationContext(), "Drinked"+date+".txt", ""+waterDrinkingInt);
										sendMessageHandle("Drink");
									} catch (NumberFormatException e) {
										// TODO: handle exception
										sendMessageHandle("喝水量格式不正确，包含不合法字符");
									}
									
									break;
								default:
									sendMessageHandle("错误数据，请重传");
									break;
								}	
							}
							else {
								sendMessageHandle("错误数据，请重传");
							}
						}
					} catch (IOException e) {
						Log.e("mylog", "远程服务器断开");
						reConnect = 1;
//								mmInStream.close();
					}
				}
			}
		}
		
		// 发送数据
		private void sendMessageHandle(String msg) {
			if (chatActivity.socket == null) {
				Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				OutputStream os = chatActivity.socket.getOutputStream();
				os.write(msg.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

		/* 停止客户端连接 */
		public  void shutdownClient() {
			new Thread() {
				public void run() {
					if (clientConnectThread != null) {
						clientConnectThread.interrupt();
						clientConnectThread = null;
					}
					if (mreadThread != null) {
						mreadThread.interrupt();
						mreadThread = null;
					}
					if (chatActivity.socket != null) {
						try {
							chatActivity.socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						chatActivity.socket = null;
					}
				};
			}.start();
		}
		
		
		
		public static boolean pair(String strAddr, String strPsw)
		{
			boolean result = false;
			//蓝牙设备适配器
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			//取消发现当前设备的过程
			bluetoothAdapter.cancelDiscovery();
			if (!bluetoothAdapter.isEnabled())
			{
				bluetoothAdapter.enable();
			}
			if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
			{ // 检查蓝牙地址是否有效
				Log.d("mylog", "devAdd un effient!");
			}
			//由蓝牙设备地址获得另一蓝牙设备对象
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
			if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			{
				try
				{
					Log.d("mylog", "NOT BOND_BONDED");
					ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
					ClsUtils.createBond(device.getClass(), device);
//					ClsUtils.cancelPairingUserInput(device.getClass(), device);
					chatActivity.remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
					result = true;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					Log.d("mylog", "setPiN failed!");
					e.printStackTrace();
				} //
				
			}
			else
			{
				Log.d("mylog", "HAS BOND_BONDED");
				try
				{
					//ClsUtils这个类的的以下静态方法都是通过反射机制得到需要的方法
					ClsUtils.createBond(device.getClass(), device);//创建绑定
					ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
					ClsUtils.createBond(device.getClass(), device);
//					ClsUtils.cancelPairingUserInput(device.getClass(), device);
					chatActivity.remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
					result = true;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					Log.d("mylog", "setPiN failed!");
					e.printStackTrace();
				}
			}
			return result;
		}
	

}
