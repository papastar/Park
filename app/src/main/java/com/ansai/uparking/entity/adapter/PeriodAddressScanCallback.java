package com.ansai.uparking.entity.adapter;

import android.bluetooth.BluetoothDevice;

import com.litesuits.bluetooth.scan.PeriodScanCallback;
import com.ansai.uparking.entity.database.BleData;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/12/25.
 */

public abstract class PeriodAddressScanCallback extends PeriodScanCallback {

    private ArrayList<BleData> data;
    private AtomicBoolean hasFound = new AtomicBoolean(false);

    public PeriodAddressScanCallback(ArrayList<BleData> data, long timeoutMillis) {
        super(timeoutMillis);
        this.data = data;
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!hasFound.get()) {
            BleData item = checkData(data, device.getAddress());
            if (item != null) {
                hasFound.set(true);
                liteBluetooth.stopScan(PeriodAddressScanCallback.this);
                onDeviceFound(item, device, rssi, scanRecord);
            }
        }
    }


    private BleData checkData(ArrayList<BleData> data, String address) {
        for (BleData item : data) {
            if (address.equalsIgnoreCase(item.blueAddress))
                return item;
        }
        return null;
    }

    public abstract void onDeviceFound(BleData item, BluetoothDevice device, int rssi, byte[]
            scanRecord);

}
