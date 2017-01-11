/* Copyright (C) 2013 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package com.joyotime.qparking.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.joyotime.qparking.FwUpdateActivity;
import com.joyotime.qparking.db.AppSetting;

import java.io.File;
import java.util.List;
import java.util.UUID;

/** Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device. */
public class BluetoothLeClass
{
	public static byte[] data = new byte[2];
	private final static String TAG = "BluetoothLeClass";// BluetoothLeClass.class.getSimpleName();
	public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	public static UUID UUID_OAD_IMG_IDENTIFY = UUID.fromString("f000ffc1-0451-4000-b000-000000000000");

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	public String mBluetoothDeviceAddress;
	public BluetoothGatt mBluetoothGatt;

	public static String mFilePath = null;
	private int mTime = 0;
	public interface OnConnectListener
	{
		public void onConnect(BluetoothGatt gatt);
	}

	public interface OnDisconnectListener
	{
		public void onDisconnect(BluetoothGatt gatt);
	}

	public interface OnServiceDiscoverListener
	{
		public void onServiceDiscover(BluetoothGatt gatt);
	}

	public interface OnDataAvailableListener
	{
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                characteristic, int status);

		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
	}

	private OnConnectListener mOnConnectListener;
	private OnDisconnectListener mOnDisconnectListener;
	private OnServiceDiscoverListener mOnServiceDiscoverListener;
	private OnDataAvailableListener mOnDataAvailableListener;
	private Context mContext;

	public void setOnConnectListener(OnConnectListener l)
	{
		mOnConnectListener = l;
	}

	public void setOnDisconnectListener(OnDisconnectListener l)
	{
		mOnDisconnectListener = l;
	}

	public void setOnServiceDiscoverListener(OnServiceDiscoverListener l)
	{
		mOnServiceDiscoverListener = l;
	}

	public void setOnDataAvailableListener(OnDataAvailableListener l)
	{
		mOnDataAvailableListener = l;
	}

	public BluetoothLeClass(Context c)
	{
		mContext = c;
	}

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
	{
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
		{
			if (newState == BluetoothProfile.STATE_CONNECTED)
			{
				if (mOnConnectListener != null)
					mOnConnectListener.onConnect(gatt);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());// 鍦ㄨ繖閲屽紑濮嬪彂鐜皊ervice

			}
			else if (newState == BluetoothProfile.STATE_DISCONNECTED)
			{
				if (mOnDisconnectListener != null)
					mOnDisconnectListener.onDisconnect(gatt);
				Log.i(TAG, "Disconnected from GATT server.");
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status)
		{
			if (status == BluetoothGatt.GATT_SUCCESS && mOnServiceDiscoverListener != null)
			{
				mOnServiceDiscoverListener.onServiceDiscover(gatt);
			}
			else
			{
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
		{
			if (mOnDataAvailableListener != null)
				mOnDataAvailableListener.onCharacteristicRead(gatt, characteristic, status);
		}
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
		{

			
			if ((characteristic.getUuid()).toString().equals("0000fff1-0000-1000-8000-00805f9b34fb"))
			{
				if (mOnDataAvailableListener != null)
					mOnDataAvailableListener.onCharacteristicWrite(gatt, characteristic);
			}
			else
			{

				// broadcastUpdate(ACTION_DATA_NOTIFY,characteristic,BluetoothGatt.GATT_SUCCESS);
				if (characteristic.getUuid().equals(UUID_OAD_IMG_IDENTIFY))
				{
					Log.i(TAG, "please update imageA");
					byte[] b = new byte[2];
					b = characteristic.getValue();
					if (b[0] == 1)
					{
						mFilePath = getSDPath() + File.separator + "QParking/"+AppSetting.mHardWare + File.separator + "PL_imageA.bin";
						Log.i(TAG, "filepath =" + mFilePath);

					}
					else if (b[0] == 0)
					{
						mFilePath = getSDPath() + File.separator + "QParking/" +AppSetting.mHardWare+ File.separator + "PL_imageB.bin";
						Log.i(TAG, "filepath =" + mFilePath);

					}

				}
				else
				{
					Log.i("hzxlelp3", "update");
					Log.i(TAG, "value_is:" + characteristic.getValue() + "  uuid == " + characteristic.getUuid());
					data = characteristic.getValue();
					FwUpdateActivity.setValue(data);
					byte[] b = new byte[2];
					b = characteristic.getValue();
					Log.i(TAG, "value[0]== " + b[0] + " value[1]== " + b[1]);
				}
			}
		}

	};

	public String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 鍒ゆ柇sd鍗℃槸鍚﹀瓨鍦�
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();// 鑾峰彇璺熺洰褰�
		}
		return sdDir.toString();
	}
	/** Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful. */
	public boolean initialize()
	{
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null)
		{
			mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null)
			{
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null)
		{
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/** Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback. */
	public boolean connect(final String address)
	{
		if (mBluetoothAdapter == null || address == null)
		{
			Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null)
		{
			Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect())
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null)
		{
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		return true;
	}

	/** Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback. */
	public void disconnect()
	{
		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/** After using a given BLE device, the app must call this method to ensure
	 * resources are released properly. */
	public void close()
	{
		if (mBluetoothGatt == null)
		{
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/** Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from. */
	public boolean readCharacteristic(BluetoothGattCharacteristic characteristic)
	{
		boolean mRet = false;
		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.w(TAG, "BluetoothAdapter not initialized");
			return mRet;
		}
		mRet = mBluetoothGatt.readCharacteristic(characteristic);
		return mRet;
	}

	/** Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise. */
	public boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled)
	{

		boolean mRet = false;
		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.w(TAG, "BluetoothAdapter not initialized");
			return mRet;
		}
		if (enabled == true)
		{
			Log.i("bbbbbbb", characteristic.getUuid()+"设置回调成功");
			mBluetoothGatt.setCharacteristicNotification(characteristic, true);
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			if (descriptor != null)
			{
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(descriptor);
			}
			Log.i("bbbbbbb", characteristic.getUuid()+"设置回调成功");
		}
		else
		{
			Log.i(TAG, "Disable Notification");
			mBluetoothGatt.setCharacteristicNotification(characteristic, false);
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
		return mRet;
		// mBluetoothGatt.setCharacteristicNotification(characteristic,
		// enabled);
	}

	public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic)
	{
		return mBluetoothGatt.writeCharacteristic(characteristic);
	}

	/** Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services. */
	public List<BluetoothGattService> getSupportedGattServices()
	{
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}
}
