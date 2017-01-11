package com.joyotime.qparking.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	public static Boolean IsNetWork = false;
	private static Context icontext;

	@Override
	public void onReceive(Context context, Intent intent) {
		icontext = context;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			ShowToast("网络已断开");
			// 改变背景或者 处理网络的全局变量
			IsNetWork = false;
		} else {
			// 改变背景或者 处理网络的全局变量
			IsNetWork = true;
		}
	}

	private static void ShowToast(String msg) {
		Toast.makeText(icontext, msg, 1000).show();
	}
}