package com.joyotime.qparking;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;

public class about extends Activity {

	ImageView about_goback;
	ImageView about_rl_about_red;
	TextView about3_text;
	TextView about6_text;
	MethodDate mdb = new MethodDate(this);
	LinearLayout about1;
	LinearLayout about2;
	LinearLayout about3;
	LinearLayout about4;
	LinearLayout about5;
	LinearLayout about6;
	LinearLayout about7;
	LinearLayout about8;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		context = this;
		about1 = (LinearLayout) findViewById(R.id.about1);
		about2 = (LinearLayout) findViewById(R.id.about2);
		about3 = (LinearLayout) findViewById(R.id.about3);
		about4 = (LinearLayout) findViewById(R.id.about4);
		about5 = (LinearLayout) findViewById(R.id.about5);
		about6 = (LinearLayout) findViewById(R.id.about6);
		about7 = (LinearLayout) findViewById(R.id.about7);
		about8 = (LinearLayout) findViewById(R.id.about8);
		about1.setOnClickListener(ClickListener);
		about2.setOnClickListener(ClickListener);
		about3.setOnClickListener(ClickListener);
		about4.setOnClickListener(ClickListener);
		about5.setOnClickListener(ClickListener);
		about7.setOnClickListener(ClickListener);
		about8.setOnClickListener(ClickListener);
		about3_text = (TextView) findViewById(R.id.about3_text);
		about6_text = (TextView) findViewById(R.id.about6_text);
		about_goback = (ImageView) findViewById(R.id.about_goback);
		about_rl_about_red = (ImageView) findViewById(R.id.about_rl_about_red);

		about_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		if (AppSetting.AppVCode > mdb.getVersion() && AppSetting.AppVCode > 0) {
			about3_text.setText("最新版  " + AppSetting.AppVName);
			// about3_text.setTextColor(com.joyotime.qparking.R.color.redabout);
			about_rl_about_red.setVisibility(View.VISIBLE);
		} else {
			String nameString = mdb.getVersionName();
			about3_text.setText(nameString);
			about_rl_about_red.setVisibility(View.GONE);
			// about3_text.setTextColor(color.black);
		}
		if (AppSetting.mSoftWare > 0) {
			about6.setVisibility(View.VISIBLE);
			about6_text.setText("Fac_V" + AppSetting.mSoftWare);
		} else {
			about6.setVisibility(View.GONE);
			;
		}
	}

	private void ShowToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	private OnClickListener ClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!ConnectionChangeReceiver.IsNetWork) {
				ShowToast("网络已断开，请检查网络");
				return;
			}
			LinearLayout llv = (LinearLayout) v;
			switch (llv.getId()) {
			case R.id.about1:
				// Intent intent = new Intent();
				// intent.setClass(about.this, UpdateApp.class);
				// startActivity(intent);

				Intent intent = new Intent();
				intent.putExtra("buglog", "");
				intent.setClass(about.this, FeedBack.class);
				startActivity(intent);

				break;
			case R.id.about2:
				Intent intent2 = new Intent();
				intent2.setClass(about.this, About_WebView.class);
				intent2.putExtra("title", "使用帮助");
				intent2.putExtra("url", AppSetting.about_url);
				startActivity(intent2);
				break;
			case R.id.about3:
				if (AppSetting.AppVCode > mdb.getVersion() && AppSetting.AppVCode > 0) {
					DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
					DownloadManager.Request request = new DownloadManager.Request(Uri.parse(AppSetting.Url_Android));
					request.setDestinationInExternalPublicDir("QParking/", "QParking.apk");
					request.setVisibleInDownloadsUi(true);
					downloadManager.enqueue(request);
					ShowToast("即将开始下载...");
					finish();
				} else {
					ShowToast("已是最新版");
				}
				break;
			case R.id.about4:
				Intent intent4 = new Intent();
				intent4.setClass(about.this, About_WebView.class);
				intent4.putExtra("title", "服务条款");
				intent4.putExtra("url", AppSetting.clause_url);
				startActivity(intent4);
				break;
			case R.id.about5:
				Intent intent5 = new Intent();
				intent5.setClass(about.this, About_WebView.class);
				intent5.putExtra("title", "联系我们");
				intent5.putExtra("url", AppSetting.contactus_url);
				startActivity(intent5);
				break;
			case R.id.about7:
				if (LockMain.mBLE != null && LockMain.gattCharacteristic_oadimgidentify != null
						&& LockMain.gattCharacteristic_oadimablock != null) {
					Intent intenta = new Intent();
					intenta.setClass(about.this, FwUpdateActivity.class);
					startActivityForResult(intenta, 1);
				} else {
					ShowToast("请先连接车锁");
				}
				break;
			case R.id.about8:
				Intent inten = new Intent();
				inten.setClass(about.this, About_WebView.class);
				inten.putExtra("title", "购买车锁");
				inten.putExtra(
						"url",
						"https://item.taobao.com/item.htm?spm=a230r.1.14.1.syElve&id=45763485020&ns=1&abbucket=12#detail");
				startActivity(inten);
				break;
			default:
				break;
			}
		}
	};

}
