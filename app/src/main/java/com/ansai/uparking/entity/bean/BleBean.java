package com.ansai.uparking.entity.bean;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Administrator on 2016/12/11.
 */

public class BleBean {

    private BluetoothDevice mDevice;
    private int mRssi;
    private byte[] mScanRecord;

    public BleBean(BluetoothDevice device, int rssi, byte[] scanRecord){
        mDevice = device;
        mRssi = rssi;
        mScanRecord = scanRecord;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public int getRssi() {
        return mRssi;
    }

    public byte[] getScanRecord() {
        return mScanRecord;
    }
}
