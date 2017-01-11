package com.joyotime.qparking;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansai.uparking.R;
import com.joyotime.qparking.ble.BluetoothLeClass;
import com.joyotime.qparking.ble.BluetoothLeClass.OnServiceDiscoverListener;
import com.joyotime.qparking.ble.iBeaconClass;
import com.joyotime.qparking.ble.iBeaconClass.iBeacon;

import java.util.List;

public class Automation extends Activity
{
//	TypegifView openview;
//	TypegifView closeview;

	private Boolean sendbz = true;

	private ImageView goback = null;
	static public BluetoothLeClass mBLE;
	public static String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
	public static String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
	static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
	static BluetoothGattCharacteristic gattCharacteristic_char3 = null;
	public String bluetoothAddress;
	public String bluetoothName;
	String dataString;
	String srtState;

	TextView auto_rssi;
	TextView auto_text;

	Boolean fristLock = true;
	Boolean StayAway = false;
	private BluetoothAdapter mBluetoothAdapter;
	int countstay = 0;
	Button closebtn;

	Boolean connectBZ = true;

	int[] Manage_ArrRssi = new int[10];
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_automation);
//		openview = (TypegifView) findViewById(R.id.gifViewopen);
//		closeview = (TypegifView) findViewById(R.id.gifViewclose);
		auto_rssi = (TextView) findViewById(R.id.auto_rssi);
		auto_text = (TextView) findViewById(R.id.auto_text);
		mBLE = new BluetoothLeClass(this);
		if (!mBLE.initialize())
		{
			finish();
		}
		for (int i = 0; i < Manage_ArrRssi.length; i++)
		{
			Manage_ArrRssi[i] = -70;
		}

		// 发现BLE终端的Service时回�?
		mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);

		mBLE.setOnDataAvailableListener(mOnDataAvailable);

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		mBluetoothAdapter.enable();

		Intent dataIntent = getIntent();
		Bundle dataBundle = dataIntent.getExtras();
		dataString = dataBundle.getString("macaddr");

		goback = (ImageView) findViewById(R.id.auto_goback);
		goback.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		StartLock(dataString, "joyo");

	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback()
	{

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
		{

			final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{

					if (ibeacon.bluetoothAddress.equals(bluetoothAddress))
					{
						test3(ibeacon.rssi);
						//
					}

				}
			});
		}
	};

	private synchronized void test3(int rssi)
	{
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		//BNTTSPlayer.initPlayer();

		int sss = test(rssi);

		if (sss < -75)
		{
			countstay++;
		}
		if (-68 < sss)
		{
			countstay--;
		}

		Message msg = new Message();
		msg.what = sss;
		handler.sendMessage(msg);

		if (countstay >= 5 && !StayAway)
		{
			countstay = 0;
			//BNTTSPlayer.playTTSText("您已远离车位锁", -1);
			connectBZ = mBLE.connect(bluetoothAddress);

			StayAway = true;
			return;
		}
		else if (countstay <= -5 && StayAway)
		{
			countstay = 0;
			//BNTTSPlayer.playTTSText("您已靠近车位锁", -1);
			connectBZ = mBLE.connect(bluetoothAddress);

			StayAway = false;
			return;
		}
		if (countstay < -8 || countstay > 8)
		{
			countstay = 0;
		}
		mBluetoothAdapter.startLeScan(mLeScanCallback);
	}

	private synchronized int test(int newRssi)
	{
		int average = 0;

		for (int i = 0; i < Manage_ArrRssi.length - 1; i++)
		{
			Manage_ArrRssi[i] = Manage_ArrRssi[i + 1];
		}
		Manage_ArrRssi[9] = newRssi;

		for (int i = 0; i < Manage_ArrRssi.length; i++)
		{
			average += Manage_ArrRssi[i];
		}
		average = average / 10;
		return average;

	}

	private void SetCarLock()
	{

		//BNTTSPlayer.initPlayer();
		if (srtState.equals("-16"))
		{
			sendbz = false;
			writeChar1("0F");
//			closeview.setVisibility(View.GONE);
//			openview.setVisibility(View.VISIBLE);
//			openview.setStart();
//			closeview.setStop();

			//BNTTSPlayer.playTTSText("正在关闭车位锁", -1);
			srtState = "15";
		}
		else if (srtState.equals("15"))
		{
			writeChar1("F0");
			//BNTTSPlayer.playTTSText("正在开启车位锁", -1);
			srtState = "-16";
//			closeview.setVisibility(View.VISIBLE);
//			openview.setVisibility(View.GONE);
//			openview.setStop();
//			closeview.setStart();

		}
		else if (srtState.equals("1"))
		{
			writeChar1("F0");
			//BNTTSPlayer.playTTSText("正在初始化车位锁", -1);
			srtState = "-16";

		}
		Message msg = new Message();

		if (srtState.equals("15"))
		{
			msg.what = 1;
		}
		else if (srtState.equals("-16"))
		{
			msg.what = 2;

		}
		handler.sendMessage(msg);
		if (mBLE != null)
		{
			mBLE.disconnect();
			mBLE.close();
		}
		mBluetoothAdapter.startLeScan(mLeScanCallback);

	}
	private void StartLock(String device, String name)
	{
		// BNTTSPlayer.initPlayer();
		if (device == null)
			return;

		bluetoothAddress = device;
		bluetoothName = name;
		connectBZ = mBLE.connect(bluetoothAddress);

	}
	private OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener()
	{

		@Override
		public void onServiceDiscover(BluetoothGatt gatt)
		{
			displayGattServices(mBLE.getSupportedGattServices());
		}

	};
	/** 收到BLE终端数据交互的事�? */
	private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener()
	{
		/** BLE终端数据被读的事�? */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
		{
			// 执行 mBLE.readCharacteristic(gattCharacteristic); 后就会收到数�? if
			// (status == BluetoothGatt.GATT_SUCCESS)

			char_display(com.joyotime.qparking.ble.Utils.bytesToString(characteristic.getValue()), characteristic.getValue(), characteristic.getUuid().toString());
		}

		/** 收到BLE终端写入数据回调 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
		{
			char_display(com.joyotime.qparking.ble.Utils.bytesToString(characteristic.getValue()), characteristic.getValue(), characteristic.getUuid().toString());
		}
	};

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 1)
			{
				auto_text.setText("车锁处于关闭状态");
//				closeview.setVisibility(View.VISIBLE);
//				openview.setVisibility(View.GONE);
			}
			else if (msg.what == 2)
			{
				auto_text.setText("车锁处于开启状态");
//				closeview.setVisibility(View.GONE);
//				openview.setVisibility(View.VISIBLE);
			}
			else
			{
				auto_rssi.setText(msg.what + "");
			}
//			openview.setStop();
//			closeview.setStop();
		}
	};
	private void char_display(String str, byte[] data, String uuid)
	{

		srtState = data[0] + "";
		if (fristLock && (srtState.equals("15") || srtState.equals("1")))
		{
			fristLock = false;
			SetCarLock();
			Message msgMessage = new Message();
			msgMessage.what = 2;
			handler.sendMessage(msgMessage);
		}

		if (srtState.equals("-16"))
		{
			fristLock = false;
			if (mBLE != null)
			{
				Message msgMessage = new Message();
				msgMessage.what = 2;
				handler.sendMessage(msgMessage);
				mBLE.disconnect();
				mBLE.close();
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		}

	}
	static public void writeChar1(String str)
	{
		if (gattCharacteristic_char1 != null)
		{
			boolean bRet = gattCharacteristic_char1.setValue(StringToByteArray(str));
			mBLE.writeCharacteristic(gattCharacteristic_char1);
		}
	}

	private void Read_Char3()
	{
		mBLE.readCharacteristic(gattCharacteristic_char1);
	}

	static byte[] StringToByteArray(String str)
	{
		String[] str_ary = str.split(" ");
		int n = str_ary.length;
		byte[] bt_ary = new byte[n];
		for (int i = 0; i < n; i++)
			bt_ary[i] = (byte) Integer.parseInt(str_ary[i], 16);
		return bt_ary;
	}

	private void displayGattServices(List<BluetoothGattService> gattServices)
	{

		//BNTTSPlayer.initPlayer();

		if (gattServices == null)
		{
			//BNTTSPlayer.playTTSText("设备连接失败", -1);
			return;
		}
		else
		{
			//BNTTSPlayer.playTTSText("设备已连接", -1);
			// mBluetoothAdapter.startLeScan(mLeScanCallback);
		}

		for (BluetoothGattService gattService : gattServices)
		{
			// -----Service的字段信�?----//
			int type = gattService.getType();
			// -----Characteristics的字段信�?----//
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics)
			{
				if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR1))
				{
					// 把char1 保存起来�?以方便后面读写数据时使用
					gattCharacteristic_char1 = gattCharacteristic;

				}
				else if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR3))
				{
					// 把char1 保存起来�?以方便后面读写数据时使用
					gattCharacteristic_char3 = gattCharacteristic;
				}
			}
		}//

		if (!fristLock && !StayAway)
		{
			// StayAway = true;
			// srtState = "15";
			SetCarLock();

		}
		else if (!fristLock && StayAway)
		{
			// StayAway = false;
			// srtState = "-16";
			SetCarLock();
		}
		if (fristLock)
		{
			Read_Char3();
		}

	}
	

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		if (mBLE != null)
		{
			mBLE.disconnect();
			mBLE.close();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		setContentView(R.layout.activity_null);
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		if (mBLE != null)
		{
			mBLE.disconnect();
			mBLE.close();
		}
	}
}
