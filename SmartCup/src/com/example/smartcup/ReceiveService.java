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
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO �Զ����ɵķ������	
		
		chatActivity.address = MainActivity.BlueToothAddress;
		if (!chatActivity.address.equals("null")) {
			chatActivity.remoteDevice = chatActivity.mBluetoothAdapter.getRemoteDevice(chatActivity.address);
			// ////////////////--------ע�����ӷ�����
			try {
				System.out.println(chatActivity.address);
				if (chatActivity.remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
					pair(chatActivity.address, "1234");
					Message msg2 = new Message();
					msg2.obj = "�Զ���Գɹ�,���Ե�";
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
	

	
	// �����ͻ���
		private class clientThread extends Thread {
			public void run() {
				try {

					System.out.println("ƥ��");
					// ����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
					// socket =
					// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
					chatActivity.socket = chatActivity.remoteDevice.createRfcommSocketToServiceRecord(UUID
							.fromString("00001101-0000-1000-8000-00805F9B34FB"));
					// ����
					Message msg2 = new Message();
					msg2.obj = "���Ժ�������  �ӷ�����:" + MainActivity.BlueToothAddress;
					msg2.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg2);
					chatActivity.socket.connect();
					Message msg = new Message();
					msg.obj = "�Ѿ������Ϸ���ˣ����Է�����Ϣ��";
					msg.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg);
					// ������������
					mreadThread = new readThread();
					mreadThread.start();
				} catch (Exception e) {
					Log.e("connect", "", e);
					Message msg = new Message();
					msg.obj = "���ӷ�����쳣���Ͽ�����������һ�ԡ�";
					msg.what = 0;
					chatActivity.LinkDetectedHandler.sendMessage(msg);
				}
			}
		};
		
		/* ֹͣ�ͻ������� */
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
		
		// ��ȡ����
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
			//�����豸������
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			//ȡ�����ֵ�ǰ�豸�Ĺ���
			bluetoothAdapter.cancelDiscovery();
			if (!bluetoothAdapter.isEnabled())
			{
				bluetoothAdapter.enable();
			}
			if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
			{ // ���������ַ�Ƿ���Ч
				Log.d("mylog", "devAdd un effient!");
			}
			//�������豸��ַ�����һ�����豸����
			BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
			if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			{
				try
				{
					Log.d("mylog", "NOT BOND_BONDED");
					ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
					ClsUtils.createBond(device.getClass(), device);
//					ClsUtils.cancelPairingUserInput(device.getClass(), device);
					chatActivity.remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
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
					//ClsUtils�����ĵ����¾�̬��������ͨ��������Ƶõ���Ҫ�ķ���
					ClsUtils.createBond(device.getClass(), device);//������
					ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
					ClsUtils.createBond(device.getClass(), device);
//					ClsUtils.cancelPairingUserInput(device.getClass(), device);
					chatActivity.remoteDevice = device; // ����󶨳ɹ�����ֱ�Ӱ�����豸���󴫸�ȫ�ֵ�remoteDevice
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
