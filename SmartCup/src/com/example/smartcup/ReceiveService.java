package com.example.smartcup;



import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.UUID;

import com.example.smartcup.chatActivity;
import com.example.smartcup.chatActivity.deviceListItem;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class ReceiveService extends Service {

	private static clientThread clientConnectThread = null;
	readThread mreadThread = null;;
	
	PublicMethod publicMethod =  new PublicMethod();
	
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
					Message msg2 = new Message();
					msg2.obj = "自动配对成功,请稍等";
					msg2.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg2);
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
				try {

					System.out.println("匹配");
					// 创建一个Socket连接：只需要服务器在注册时的UUID号
					// socket =
					// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
					chatActivity.socket = chatActivity.remoteDevice.createRfcommSocketToServiceRecord(UUID
							.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					// 连接
					Message msg2 = new Message();
					msg2.obj = "请稍候，正在连  接服务器:" + MainActivity.BlueToothAddress;
					msg2.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg2);
					chatActivity.socket.connect();
					Message msg = new Message();
					msg.obj = "已经连接上服务端！可以发送信息。";
					msg.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg);
					// 启动接受数据
					mreadThread = new readThread();
					mreadThread.start();
				} catch (Exception e) {
					Log.e("connect", "", e);
					Message msg = new Message();
					msg.obj = "连接服务端异常！断开连接重新试一试。";
					msg.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg);
				}
			}
		};
		
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
					try {
							if ((bytes = mmInStream.read(buffer)) > 0) {
								byte[] buf_data = new byte[bytes];
								for (int i = 0; i < bytes; i++) {
									buf_data[i] = buffer[i];
								}
								String s = new String(buf_data);
								Message msg = new Message();		
								publicMethod.writeToTxt(getApplicationContext(), "test.txt", s);
//								String temp = publicMethod.readFromTxt(getApplicationContext(), "test.txt");
								msg.obj = s;
								msg.what = 0;
								chatActivity.LinkDetectedHandler.sendMessage(msg);
						}
					} catch (IOException e) {
						try {
							mmInStream.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
				}
			}
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
