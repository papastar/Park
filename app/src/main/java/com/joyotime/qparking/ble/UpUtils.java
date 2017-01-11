package com.joyotime.qparking.ble;
import android.bluetooth.BluetoothGattCharacteristic;

public class UpUtils
{
	static BluetoothLeClass mBluetoothLeClass = null;
	static BluetoothGattCharacteristic gattCharacteristic_oadimgidentify = null;
	static BluetoothGattCharacteristic gattCharacteristic_oadimablock = null;

	public static void set_Oadimgidentif(BluetoothGattCharacteristic mIdentify)
	{
		gattCharacteristic_oadimgidentify = mIdentify;
	}

	public static BluetoothGattCharacteristic get_Oadimgidentif()
	{
		return gattCharacteristic_oadimgidentify;
	}

	public static void set_Oadimablock(BluetoothGattCharacteristic mBlock)
	{
		gattCharacteristic_oadimablock = mBlock;
	}

	public static BluetoothGattCharacteristic get_Oadimablock()
	{
		return gattCharacteristic_oadimablock;
	}

	public static void set_BluetoothLeClass(BluetoothLeClass mBle)
	{
		mBluetoothLeClass = mBle;
	}

	public static BluetoothLeClass get_BluetoothLeClass()
	{
		return mBluetoothLeClass;
	}
}