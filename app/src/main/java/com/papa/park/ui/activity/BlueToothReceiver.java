package com.papa.park.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.papa.libcommon.rx.RxBus;
import com.papa.libcommon.util.Logger;
import com.papa.park.entity.event.BluetoothConnectEvent;

public class BlueToothReceiver extends BroadcastReceiver {

    // private String
    // BLUETOOTH="android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED";
    // private BluetoothAdapter mBluetoothAdapter;
    // private BluetoothDevice btDevice;
    private String btMessage = "";
    // 监听蓝牙状态

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        // Toast.makeText(context, "蓝牙状态改变广播 !", Toast.LENGTH_LONG).show();

        Logger.d("接收到蓝牙状态改变广播！！");
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Toast.makeText(context, device.getName() + " 设备已发现！！",
            // Toast.LENGTH_LONG).show();
            // btMessage = device.getName() + "设备已发现！！";
        } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            // Toast.makeText(context, device.getName() + "已连接",
            // Toast.LENGTH_LONG).show();
            // btMessage = device.getName() + "设备已连接！！";
            // Message msg = new Message();
            // msg.what = 3002;
            // LockMain.handler.sendMessage(msg);
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            // Toast.makeText(context, device.getName() + "正在断开蓝牙连接。。。",
            // Toast.LENGTH_LONG).show();
            // btMessage = device.getName() + "正在断开蓝牙连接。。。";
            RxBus.getInstance().post(BluetoothConnectEvent.class);
//            Message msg = new Message();
//            msg.what = 3006;
//            LockMain.handler.sendMessage(msg);
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            // Toast.makeText(context, device.getName() + "蓝牙连接已断开！！！",
            // Toast.LENGTH_LONG).show();
            // btMessage = device.getName() + "蓝牙连接已断开！！";
            RxBus.getInstance().post(BluetoothConnectEvent.class);
//            Message msg = new Message();
//            msg.what = 3006;
//            LockMain.handler.sendMessage(msg);
        }

    }



}
