package com.joyotime.qparking;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

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

		Log.i("TAG---BlueTooth", "接收到蓝牙状态改变广播！！");
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
		}

		else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
			// Toast.makeText(context, device.getName() + "正在断开蓝牙连接。。。",
			// Toast.LENGTH_LONG).show();
			// btMessage = device.getName() + "正在断开蓝牙连接。。。";
			Message msg = new Message();
			msg.what = 3006;
			LockMain.handler.sendMessage(msg);
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
			// Toast.makeText(context, device.getName() + "蓝牙连接已断开！！！",
			// Toast.LENGTH_LONG).show();
			// btMessage = device.getName() + "蓝牙连接已断开！！";
			Message msg = new Message();
			msg.what = 3006;
			LockMain.handler.sendMessage(msg);
		}

		// DownloadManager manager = (DownloadManager)
		// context.getSystemService(Context.DOWNLOAD_SERVICE);
		// if
		// (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction()))
		// {
		// try
		// {
		//
		// DownloadManager.Query query = new DownloadManager.Query();
		// // 在广播中取出下载任务的id
		// long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		// query.setFilterById(id);
		// Cursor c = manager.query(query);
		// if (c.moveToFirst())
		// {
		// // 获取文件下载路径
		// String filename =
		// c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
		// // 如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
		// if (filename != null && filename.equals("QParking.apk"))
		// {
		// OpenApp oaApp = new OpenApp();
		// oaApp.update();
		//
		// }
		// }
		// }
		// catch (Exception e)
		// {
		// // TODO: handle exception
		// }
		// }
		// else if
		// (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction()))
		// {
		// long[] ids =
		// intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
		// // 点击通知栏取消下载
		// manager.remove(ids);
		// // ShowToastUtil.showShortToast(context, "已经取消下载");
		// }

	}

	// private class OpenApp extends Activity
	// {
	// void update()
	// {
	// Intent intentapp = new Intent(Intent.ACTION_VIEW);
	// intentapp.setDataAndType(Uri.fromFile(new
	// File(Environment.getExternalStorageDirectory(), "QParking.apk")),
	// "application/vnd.android.package-archive");
	// startActivity(intentapp);
	// }
	// }

}
