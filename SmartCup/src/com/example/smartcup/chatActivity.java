package com.example.smartcup;

import com.example.smartcup.R;
import com.example.smartcup.MainActivity.ServerOrCilent;
import com.example.smartcup.ReceiveService.readThread;

import java.io.IOException;
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

	private BluetoothServerSocket mserverSocket = null;
	private ServerThread startServerThread = null;
	
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
					ReceiveService.shutdownClient();
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
				ReceiveService.mreadThread = new readThread();
				ReceiveService.mreadThread.start();
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
				if (ReceiveService.mreadThread != null) {
					ReceiveService.mreadThread.interrupt();
					ReceiveService.mreadThread = null;
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