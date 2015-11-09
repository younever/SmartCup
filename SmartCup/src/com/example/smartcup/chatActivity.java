package com.example.smartcup;

import com.example.smartcup.R;
import com.example.smartcup.MainActivity.ServerOrCilent;
import com.example.smartcup.ReceiveService.readThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class chatActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	/** Called when the activity is first created. */
	static ListView mListView;
	static ArrayList<deviceListItem> list;
	private Button sendButton;
	private Button disconnectButton;
	private EditText editMsgView;
	static deviceListAdapter mAdapter;
	Context mContext;
	static String address = "";    ////
	/* һЩ��������������������� */
	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";

	private  BluetoothServerSocket mserverSocket = null;
	private  ServerThread startServerThread = null;
	
	PublicMethod publicMethod;
	
	readThread mreadThread = null;;
	
	static BluetoothSocket socket = null;
	static BluetoothDevice remoteDevice = null;
	
	static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		mContext = this;
		init();
	}

	private void init() {
		list = new ArrayList<deviceListItem>();
		mAdapter = new deviceListAdapter(this, list);
		mListView = (ListView) findViewById(R.id.list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setFastScrollEnabled(true);
		editMsgView = (EditText) findViewById(R.id.MessageText);
		editMsgView.clearFocus();

		sendButton = (Button) findViewById(R.id.btn_msg_send);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String msgText = editMsgView.getText().toString();
				if (msgText.length() > 0) {
					sendMessageHandle(msgText);
					editMsgView.setText("");
					editMsgView.clearFocus();
					// close InputMethodManager
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editMsgView.getWindowToken(), 0);
				} else
					Toast.makeText(mContext, "�������ݲ���Ϊ�գ�", Toast.LENGTH_SHORT)
							.show();
			}
		});

		disconnectButton = (Button) findViewById(R.id.btn_disconnect);
		disconnectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
					//ReceiveService.shutdownClient();
				} else if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
					shutdownServer();
				}
				MainActivity.isOpen = false;
				MainActivity.serviceOrCilent = ServerOrCilent.NONE;
				Toast.makeText(mContext, "�ѶϿ����ӣ�", Toast.LENGTH_SHORT).show();				
			}
		});
	}

	static Handler LinkDetectedHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				list.add(new deviceListItem((String) msg.obj, true));
			} else {
				list.add(new deviceListItem((String) msg.obj, false));
			}
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(list.size() - 1);
		}

	};

	@Override
	public synchronized void onPause() {
		super.onPause();
	}
	/**
	 * ��ʱ�������������
	 */
	@Override
	public synchronized void onResume() {
		super.onResume();
		if (MainActivity.isOpen) {
				Toast.makeText(mContext, "�����Ѿ��򿪣�����ͨ�š����Ҫ�ٽ������ӣ����ȶϿ���",
						Toast.LENGTH_SHORT).show();
				MainActivity.isOpen=false;
			return;
		}
		if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
			startService(new Intent(chatActivity.this,ReceiveService.class));
		} else if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
			startServerThread = new ServerThread();
			startServerThread.start();
			MainActivity.isOpen = true;
		}
	}

	
	// ����������
	private class ServerThread extends Thread {
		public void run() {
			try {
				/*
				 * ����һ������������ �����ֱ𣺷��������ơ�UUID
				 */
				mserverSocket = mBluetoothAdapter
						.listenUsingRfcommWithServiceRecord(
								PROTOCOL_SCHEME_RFCOMM,
								UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

				Log.d("server", "wait cilent connect...");
				Message msg = new Message();
				msg.obj = "���Ժ����ڵȴ��ͻ��˵�����...";
				msg.what = 0;
				LinkDetectedHandler.sendMessage(msg);

				/* ���ܿͻ��˵��������� */
				socket = mserverSocket.accept();
				Log.d("server", "accept success !");

				Message msg2 = new Message();
				String info = "�ͻ����Ѿ������ϣ����Է�����Ϣ��";
				msg2.obj = info;
				msg.what = 0;
				LinkDetectedHandler.sendMessage(msg2);
				// ������������
				mreadThread = new readThread();
				mreadThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	/* ֹͣ������ */
	private void shutdownServer() {
		new Thread() {
			public void run() {
				if (startServerThread != null) {
					startServerThread.interrupt();
					startServerThread = null;
				}
				if (mreadThread != null) {
					mreadThread.interrupt();
					mreadThread = null;
				}
				try {
					if (socket != null) {
						socket.close();
						socket = null;
					}
					if (mserverSocket != null) {
						mserverSocket.close();/* �رշ����� */
						mserverSocket = null;
					}
				} catch (IOException e) {
					Log.e("server", "mserverSocket.close()", e);
				}
			};
		}.start();
	}
	// ��������
	private void sendMessageHandle(String msg) {
		if (socket == null) {
			Toast.makeText(mContext, "û������", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			OutputStream os = socket.getOutputStream();
			os.write(msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		list.add(new deviceListItem(msg, false));
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(list.size() - 1);
	}
	
	// ��ȡ����
	public  class readThread extends Thread {
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
			synchronized (mreadThread) {
				
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

	
	@Override
	protected void onDestroy() {
		super.onDestroy();

//		if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
//			shutdownClient();
//		} else 
			if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
			shutdownServer();
			MainActivity.isOpen = false;
			MainActivity.serviceOrCilent = ServerOrCilent.NONE; 
		}
	
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}

	public static class deviceListItem {
		String message;
		boolean isSiri;

		public deviceListItem(String msg, boolean siri) {
			message = msg;
			isSiri = siri;
		}
	}
	
	

}










//package com.example.smartcup;
//import com.example.smartcup.R;
//import com.example.smartcup.MainActivity.ServerOrCilent;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.UUID;
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothSocket;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class chatActivity extends Activity implements OnItemClickListener,
//		OnClickListener {
//	/** Called when the activity is first created. */
//
//	private ListView mListView;
//	private ArrayList<deviceListItem> list;
//	private Button sendButton;
//	private Button disconnectButton;
//	private EditText editMsgView;
//	deviceListAdapter mAdapter;
//	Context mContext;
//	private String address = "";
//	private Timer time;
//	/* һЩ��������������������� */
//	public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
//	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
//	public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
//	public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcpobex";
//
//	private BluetoothServerSocket mserverSocket = null;
//	private ServerThread startServerThread = null;
//	private clientThread clientConnectThread = null;
//	private BluetoothSocket socket = null;
//	private static BluetoothDevice remoteDevice = null;
//	private readThread mreadThread = null;;
//	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
//			.getDefaultAdapter();
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.chat);
//		mContext = this;
//		init();
//	}
//
//	private void init() {
//		list = new ArrayList<deviceListItem>();
//		mAdapter = new deviceListAdapter(this, list);
//		mListView = (ListView) findViewById(R.id.list);
//		mListView.setAdapter(mAdapter);
//		mListView.setOnItemClickListener(this);
//		mListView.setFastScrollEnabled(true);
//		editMsgView = (EditText) findViewById(R.id.MessageText);
//		editMsgView.clearFocus();
//
//		sendButton = (Button) findViewById(R.id.btn_msg_send);
//		sendButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				String msgText = editMsgView.getText().toString();
//				if (msgText.length() > 0) {
//					sendMessageHandle(msgText);
//					editMsgView.setText("");
//					editMsgView.clearFocus();
//					// close InputMethodManager
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(editMsgView.getWindowToken(), 0);
//				} else
//					Toast.makeText(mContext, "�������ݲ���Ϊ�գ�", Toast.LENGTH_SHORT)
//							.show();
//			}
//		});
//
//		disconnectButton = (Button) findViewById(R.id.btn_disconnect);
//		disconnectButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
//					shutdownClient();
//				} else if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
//					shutdownServer();
//				}
//				MainActivity.isOpen = false;
//				MainActivity.serviceOrCilent = ServerOrCilent.NONE;
//				Toast.makeText(mContext, "�ѶϿ����ӣ�", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//
//	private Handler LinkDetectedHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// Toast.makeText(mContext, (String)msg.obj,
//			// Toast.LENGTH_SHORT).show();
//			if (msg.what == 1) {
//				list.add(new deviceListItem((String) msg.obj, true));
//			} else {
//				list.add(new deviceListItem((String) msg.obj, false));
//			}
//			mAdapter.notifyDataSetChanged();
//			mListView.setSelection(list.size() - 1);
//		}
//
//	};
//
//	@Override
//	public synchronized void onPause() {
//		super.onPause();
//	}
//
//
//	/**
//	 * ��ʱ�������������
//	 */
//	@Override
//	public synchronized void onResume() {
//		super.onResume();
//		if (MainActivity.isOpen) {
//			Toast.makeText(mContext, "�����Ѿ��򿪣�����ͨ�š����Ҫ�ٽ������ӣ����ȶϿ���",
//					Toast.LENGTH_SHORT).show();
//			MainActivity.isOpen=false;
//			return;
//		}
//		if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
//			address = MainActivity.BlueToothAddress;
//			if (!address.equals("null")) {
//				remoteDevice = mBluetoothAdapter.getRemoteDevice(address);
//				// ////////////////--------ע�����ӷ�����
//				try {
//					System.out.println(address);
//					if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
//						pair(address, "1234");
//						Message msg2 = new Message();
//						msg2.obj = "�Զ���Գɹ�,���Ե�";
//						msg2.what = 0;
//						LinkDetectedHandler.sendMessage(msg2);
//					} else if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
//						clientConnectThread = new clientThread();
//						clientConnectThread.start();
//						MainActivity.isOpen = true;
//					}
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			} else {
//				Toast.makeText(mContext, "address is null !",
//						Toast.LENGTH_SHORT).show();
//			}
//		} else if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
//			startServerThread = new ServerThread();
//			startServerThread.start();
//			MainActivity.isOpen = true;
//		}
//	}
//
//	// �����ͻ���
//	private class clientThread extends Thread {
//		public void run() {
//			try {
//
//				System.out.println("ƥ��");
//				// ����һ��Socket���ӣ�ֻ��Ҫ��������ע��ʱ��UUID��
//				// socket =
//				// device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
//				socket = remoteDevice.createRfcommSocketToServiceRecord(UUID
//						.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//				// ����
//				Message msg2 = new Message();
//				msg2.obj = "���Ժ��������ӷ�����:" + MainActivity.BlueToothAddress;
//				msg2.what = 0;
//				LinkDetectedHandler.sendMessage(msg2);
//
//				socket.connect();
//
//				Message msg = new Message();
//				msg.obj = "�Ѿ������Ϸ���ˣ����Է�����Ϣ��";
//				msg.what = 0;
//				LinkDetectedHandler.sendMessage(msg);
//				// ������������
//				mreadThread = new readThread();
//				mreadThread.start();
//			} catch (Exception e) {
//				Log.e("connect", "", e);
//				Message msg = new Message();
//				msg.obj = "���ӷ�����쳣���Ͽ�����������һ�ԡ�";
//				msg.what = 0;
//				LinkDetectedHandler.sendMessage(msg);
//			}
//		}
//	};
//
//	// ����������
//	private class ServerThread extends Thread {
//		public void run() {
//
//			try {
//				/*
//				 * ����һ������������ �����ֱ𣺷��������ơ�UUID
//				 */
//				mserverSocket = mBluetoothAdapter
//						.listenUsingRfcommWithServiceRecord(
//								PROTOCOL_SCHEME_RFCOMM,
//								UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//
//				Log.d("server", "wait cilent connect...");
//
//				Message msg = new Message();
//				msg.obj = "���Ժ����ڵȴ��ͻ��˵�����...";
//				msg.what = 0;
//				LinkDetectedHandler.sendMessage(msg);
//
//				/* ���ܿͻ��˵��������� */
//				socket = mserverSocket.accept();
//				Log.d("server", "accept success !");
//
//				Message msg2 = new Message();
//				String info = "�ͻ����Ѿ������ϣ����Է�����Ϣ��";
//				msg2.obj = info;
//				msg.what = 0;
//				LinkDetectedHandler.sendMessage(msg2);
//				// ������������
//				mreadThread = new readThread();
//				mreadThread.start();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	};
//
//	/* ֹͣ������ */
//	private void shutdownServer() {
//		new Thread() {
//			public void run() {
//				if (startServerThread != null) {
//					startServerThread.interrupt();
//					startServerThread = null;
//				}
//				if (mreadThread != null) {
//					mreadThread.interrupt();
//					mreadThread = null;
//				}
//				try {
//					if (socket != null) {
//						socket.close();
//						socket = null;
//					}
//					if (mserverSocket != null) {
//						mserverSocket.close();/* �رշ����� */
//						mserverSocket = null;
//					}
//				} catch (IOException e) {
//					Log.e("server", "mserverSocket.close()", e);
//				}
//			};
//		}.start();
//	}
//
//	/* ֹͣ�ͻ������� */
//	private void shutdownClient() {
//		new Thread() {
//			public void run() {
//				if (clientConnectThread != null) {
//					clientConnectThread.interrupt();
//					clientConnectThread = null;
//				}
//				if (mreadThread != null) {
//					mreadThread.interrupt();
//					mreadThread = null;
//				}
//				if (socket != null) {
//					try {
//						socket.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					socket = null;
//				}
//			};
//		}.start();
//	}
//
//	// ��������
//	private void sendMessageHandle(String msg) {
//		if (socket == null) {
//			Toast.makeText(mContext, "û������", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		try {
//			OutputStream os = socket.getOutputStream();
//			os.write(msg.getBytes());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		list.add(new deviceListItem(msg, false));
//		mAdapter.notifyDataSetChanged();
//		mListView.setSelection(list.size() - 1);
//	}
//
//	// ��ȡ����
//	private class readThread extends Thread {
//		public void run() {
//
//			byte[] buffer = new byte[1024];
//			int bytes;
//			InputStream mmInStream = null;
//
//			try {
//				mmInStream = socket.getInputStream();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			while (true) {
//				try {
//					// Read from the InputStream
//					if ((bytes = mmInStream.read(buffer)) > 0) {
//						byte[] buf_data = new byte[bytes];
//						for (int i = 0; i < bytes; i++) {
//							buf_data[i] = buffer[i];
//						}
//						String s = new String(buf_data);
//						Message msg = new Message();
//						msg.obj = s;
//						msg.what = 0;
//						System.out.println(s);
//						LinkDetectedHandler.sendMessage(msg);
//						
//					}
//				} catch (IOException e) {
//					try {
//						mmInStream.close();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					break;
//				}
//			}
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//
//		if (MainActivity.serviceOrCilent == ServerOrCilent.CILENT) {
//			shutdownClient();
//		} else if (MainActivity.serviceOrCilent == ServerOrCilent.SERVICE) {
//			shutdownServer();
//		}
//		MainActivity.isOpen = false;
//		MainActivity.serviceOrCilent = ServerOrCilent.NONE;
//	}
//
//	public class SiriListItem {
//		String message;
//		boolean isSiri;
//
//		public SiriListItem(String msg, boolean siri) {
//			message = msg;
//			isSiri = siri;
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//	}
//
//	public class deviceListItem {
//		String message;
//		boolean isSiri;
//
//		public deviceListItem(String msg, boolean siri) {
//			message = msg;
//			isSiri = siri;
//		}
//	}
//	
//	
//	public static boolean pair(String strAddr, String strPsw)
//	{
//		boolean result = false;
//		//�����豸������
//		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
//				.getDefaultAdapter();
//		//ȡ�����ֵ�ǰ�豸�Ĺ���
//		bluetoothAdapter.cancelDiscovery();
//		if (!bluetoothAdapter.isEnabled())
//		{
//			bluetoothAdapter.enable();
//		}
//		if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
//		{ // ���������ַ�Ƿ���Ч
//			Log.d("mylog", "devAdd un effient!");
//		}
//		//�������豸��ַ�����һ�����豸����
//		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);
//		if (device.getBondState() != BluetoothDevice.BOND_BONDED)
//		{
//			try
//			{
//				Log.d("mylog", "NOT BOND_BONDED");
//				ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
//				ClsUtils.createBond(device.getClass(), device);
////				ClsUtils.cancelPairingUserInput(device.getClass(), device);
//				remoteDevice = device; // �����ϾͰ�����豸���󴫸�ȫ�ֵ�remoteDevice
//				result = true;
//			}
//			catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				Log.d("mylog", "setPiN failed!");
//				e.printStackTrace();
//			} //
//
//		}
//		else
//		{
//			Log.d("mylog", "HAS BOND_BONDED");
//			try
//			{
//				//ClsUtils�����ĵ����¾�̬��������ͨ��������Ƶõ���Ҫ�ķ���
//				ClsUtils.createBond(device.getClass(), device);//������
//				ClsUtils.setPin(device.getClass(), device, strPsw); // �ֻ��������ɼ������
//				ClsUtils.createBond(device.getClass(), device);
////				ClsUtils.cancelPairingUserInput(device.getClass(), device);
//				remoteDevice = device; // ����󶨳ɹ�����ֱ�Ӱ�����豸���󴫸�ȫ�ֵ�remoteDevice
//				result = true;
//			}
//			catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				Log.d("mylog", "setPiN failed!");
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//}
//

























