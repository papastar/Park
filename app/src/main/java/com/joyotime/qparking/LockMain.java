package com.joyotime.qparking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.ble.BluetoothLeClass;
import com.joyotime.qparking.ble.iBeaconClass;
import com.joyotime.qparking.ble.iBeaconClass.iBeacon;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.view.DragLayout;
import com.joyotime.qparking.view.DragLayout.DragListener;
import com.joyotime.qparking.view.MyRelativeLayout;
import com.joyotime.qparking.view.PubileUI;
import com.joyotime.qparking.view.Util;
import com.joyotime.qparking.wxapi.WXEntryActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class LockMain extends Activity implements SensorEventListener {
	final int lockmain_getAddSuccess = 0;
	final int lockmain_getAddFail = 1;

	private DragLayout lockmain_dl;
	private static ListView lockmain_lv;

	private ImageView lockmain_iv_icon;
	private static ImageView lockmain_iv_icon_red;

	static public String lockmain_wx_codeString;
	static public Boolean lockmain_IsLogoWX = true;

	static Context lockmain_contextmian;

	static TextView lockmain_username;
	static ImageView lockmain_headimage;

	static MethodDate mdb;
	// 用户信息
	static public String _OpenID = "";
	static public String _UserName = "";
	static public String _AddressString = "";
	static public String _Token = "";
	static public Bitmap _headimage = null;
	static public String _phonenum = "";
	static public String _LockTaken = "";
	public static Boolean lockmain_LoadUserData = true;
	private static ImageView iv_lock;
	private static AnimationDrawable _lockanimat;
	private static ImageButton lockmain_lockofforob;
	private Button lockmain_change;
	private Button lockmain_bell;

	private static TextView battery_num;

	// 车锁信息
	static public String _locksn = "";
	static public String _lockkey = "";
	static public String _lockBlueAddress = "";
	static public String _lockName = "";
	static public String _lockBlueAddressLocad = "";
	static public String _parkingname = "";

	// bluelock
	static public BluetoothLeClass mBLE = null;
	public static String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
	// public static String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
	static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
	// static BluetoothGattCharacteristic gattCharacteristic_char3 = null;
	// oad service uuid and oad chara uuid
	public static UUID UUID_OAD_SERVICE = UUID
			.fromString("f000ffc0-0451-4000-b000-000000000000");
	public static UUID UUID_OAD_IMG_BLOCK = UUID
			.fromString("f000ffc2-0451-4000-b000-000000000000");
	public static UUID UUID_OAD_IMG_IDENTIFY = UUID
			.fromString("f000ffc1-0451-4000-b000-000000000000");

	// oad chara
	static BluetoothGattCharacteristic gattCharacteristic_oadimgidentify = null;
	static BluetoothGattCharacteristic gattCharacteristic_oadimablock = null;

	private static TextView tv_tomattext;
	private static RelativeLayout lockmain_tv_about;
	private static ImageView img_battery;
	private static Resources Resourcesimg;
	public static ImageView lockmain_link;

	private static Boolean lockBoolea = true;
	public static BluetoothAdapter mBluetoothAdapter;
	private static SimpleAdapter adapter = null;

	private static Boolean ISLIANJIE = false;
	private static Boolean IsOpen = false;
	// private static Boolean IsRead = true;

	private static Boolean isWriteOkBoolean = false;

	private static RelativeLayout lockmain_login_ll;
	private static LinearLayout lockmain_battery;
	private static TextView battrry_count;
	private static ImageView bat1, bat2, bat3, bat4, bat5;

	private static Boolean FirstTime = true;
	private MyRelativeLayout lockmain_mainshow;

	private static TextView lockmain_bluename, lockmain_connect;
	private static TextView lockmain_parkingaddress;

	private static Boolean IsLoadNewData = true;
	private static String IsAction = "";

	private static Dialog progressDialog;
	private static Dialog popDialog;

	static ImageView lockmain_rl_about_red;
	private static Boolean IsLoadFile = false;

	// private static String sendDataSum = "";
	// private static String sendDataType = "";
	// private static int sendDataCount = -1;

	private static String[] dataSumStringArr = null;
	private Boolean isclike = false;

	// private ImageView lockmain_iv_lock = null;

	// 更新变量
	Button m_btnCheckNewestVersion;
	String m_appNameStr; // 下载到本地要给这个APP命的名字
	Handler m_mainHandler;
	ProgressDialog m_progressDlg;
	private Boolean updateshow = true;

	Handler actionHandler = null;
	Runnable actionRunnable = null;

	// 调用距离传感器，控制屏幕
	// private SensorManager mManager;// 传感器管理对象
	// // 屏幕开关
	// private PowerManager localPowerManager = null;// 电源管理对象
	// private PowerManager.WakeLock localWakeLock = null;// 电源锁

	static String _IsChanges = "true";

	private void initVariable() {
		m_btnCheckNewestVersion = (Button) findViewById(R.id.chek_newest_version);
		m_mainHandler = new Handler();
		m_progressDlg = new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		m_progressDlg.setIndeterminate(false);
		m_appNameStr = "Qparking.apk";
	}

	class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (AppSetting.AppVCode > mdb.getVersion()
					&& AppSetting.AppVCode > 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {// 如果有最新版本
				doNewVersionUpdate(); // 更新新版本
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

	/** 提示更新新版本 */
	private void doNewVersionUpdate() {

		String str = "当前版本：" + mdb.getVersionName() + " ,发现新版本："
				+ AppSetting.AppVName + " ,是否更新？";
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle("软件更新")
				.setCancelable(false)
				.setMessage(str)
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								m_progressDlg.setTitle("正在下载");
								m_progressDlg.setMessage("请稍候...");
								downFile(AppSetting.Url_Android); // 开始下载
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								// finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	/** 提示更新新版本 */
	private void hardNewVersionUpdate() {

		String str = "当前版本：Fac_V" + AppSetting.mSoftWare + " ,发现新版本：Fac_V"
				+ AppSetting.serviceSoftVersion + " ,是否更新？";
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle("固件更新")
				.setCancelable(false)
				.setMessage(str)
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (mBLE != null
										&& gattCharacteristic_oadimgidentify != null
										&& gattCharacteristic_oadimablock != null) {
									Intent intenta = new Intent();
									intenta.setClass(LockMain.this,
											FwUpdateActivity.class);
									startActivityForResult(intenta, 1);

								} else {
									ShowToast("请先连接车锁");
								}
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								// finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void downFile(final String url) {
		m_progressDlg.setCancelable(false);
		m_progressDlg.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();

					m_progressDlg.setMax((int) length);// 设置进度条的最大值

					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								m_appNameStr);
						if (file.exists()) {
							file.delete();
						}
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								m_progressDlg.setProgress(count);
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down(); // 告诉HANDER已经下载完成了，可以安装了
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					// ShowToast("下载失败");
					// m_progressDlg.cancel();
					// m_progressDlg.dismiss();
					//
					// doNewVersionUpdate();
				} catch (IOException e) {
					e.printStackTrace();
					// ShowToast("下载失败");
					// m_progressDlg.cancel();
					// m_progressDlg.dismiss();
				}
			}
		}.start();
	}

	/** 告诉HANDER已经下载完成了，可以安装了 */
	private void down() {
		m_mainHandler.post(new Runnable() {
			public void run() {
				m_progressDlg.cancel();
				update();
			}
		});
	}

	/** 安装程序 */
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), m_appNameStr)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	public class WorkTheard extends Thread {

		@Override
		public void run() {

			Cursor bluedata = mdb.GetIsUseBlue(_OpenID);
			int bluecount = bluedata.getCount();
			if (bluecount > 0) {
				for (bluedata.moveToFirst(); !bluedata.isAfterLast(); bluedata
						.moveToNext()) {
					int snColumn = bluedata.getColumnIndex("sn");
					int blueaddressColumn = bluedata
							.getColumnIndex("blueaddress");
					int keyColumn = bluedata.getColumnIndex("key");
					int locktokenColumn = bluedata.getColumnIndex("locktoken");
					int bluenameColumn = bluedata.getColumnIndex("bluename");
					int notenameColumn = bluedata.getColumnIndex("notename");
					int parkingnameColumn = bluedata
							.getColumnIndex("parkingname");

					_locksn = bluedata.getString(snColumn);
					_lockBlueAddressLocad = bluedata
							.getString(blueaddressColumn);
					_lockkey = bluedata.getString(keyColumn);
					_LockTaken = bluedata.getString(locktokenColumn);
					_lockName = bluedata.getString(notenameColumn).equals("") ? bluedata
							.getString(bluenameColumn) : bluedata
							.getString(notenameColumn);
					_parkingname = bluedata.getString(parkingnameColumn);

				}
				if (_IsChanges.equals("true")) {
					Message msg1 = new Message();
					if (_lockBlueAddress == null) {
						msg1.what = 3008;
					} else {
						if (ISLIANJIE) {
							msg1.what = 3002;
						} else {
							msg1.what = 3009;
						}
					}
					handler.sendMessage(msg1);
				}
				if (mBluetoothAdapter != null)
					mBluetoothAdapter.startLeScan(mLeScanCallback);

			} else {
				Message msg1 = new Message();
				msg1.what = 3008;

				handler.sendMessage(msg1);

				_locksn = "";
				_lockBlueAddressLocad = "";
				_lockkey = "";
				_LockTaken = "";
				_lockName = "";
				_parkingname = "";
				if (mBLE != null) {
					mBLE.disconnect();
					mBLE.close();
				}
				if (mBluetoothAdapter != null)
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				ISLIANJIE = false;

				Message msg2 = new Message();
				msg2.what = 1;
				showhandler.sendMessage(msg2);

			}

		}

	}

	private long showTime = 0;

	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockmain);
		// mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// // 获取系统服务POWER_SERVICE，返回一个PowerManager对象
		// localPowerManager = (PowerManager)
		// getSystemService(Context.POWER_SERVICE);
		// // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		// localWakeLock = this.localPowerManager.newWakeLock(32, "MyPower");//
		// 第一个参数为电源锁级别，

		// 初始化更新相关变量
		initVariable();

		lockmain_mainshow = (MyRelativeLayout) findViewById(R.id.lockmain_mainshow);
		lockmain_contextmian = this;
		lockmain_bluename = (TextView) findViewById(R.id.lockmain_bluename);
		lockmain_connect = (TextView) findViewById(R.id.lockmain_connect);
		lockmain_parkingaddress = (TextView) findViewById(R.id.lockmain_parkingaddress);
		mdb = new MethodDate(lockmain_contextmian);
		lockmain_username = (TextView) findViewById(R.id.lockmain_username);
		lockmain_headimage = (ImageView) findViewById(R.id.lockmain_headimage);
		iv_lock = (ImageView) findViewById(R.id.lockmain_iv_lock);
		// iv_lock.setBackgroundResource(R.anim.lockopen);
		// lockmain_iv_lock = (ImageView) findViewById(R.id.lockmain_iv_lock);

		lockmain_battery = (LinearLayout) findViewById(R.id.battery_icon_container);
		battrry_count = (TextView) findViewById(R.id.battery_count);
		bat1 = (ImageView) findViewById(R.id.bat_1);
		bat2 = (ImageView) findViewById(R.id.bat_2);
		bat3 = (ImageView) findViewById(R.id.bat_3);
		bat4 = (ImageView) findViewById(R.id.bat_4);
		bat5 = (ImageView) findViewById(R.id.bat_5);
		tv_tomattext = (TextView) findViewById(R.id.lockmain_tomattext);
		lockmain_tv_about = (RelativeLayout) findViewById(R.id.lockmain_rl_about);
		lockmain_tv_about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LockMain.this, about.class));
			}
		});
		Resourcesimg = getResources();

		lockmain_link = (ImageView) findViewById(R.id.lockmain_link);

		lockmain_login_ll = (RelativeLayout) findViewById(R.id.lockmain_login_ll);

		lockmain_lockofforob = (ImageButton) findViewById(R.id.lockmain_lockofforob);
		lockmain_lockofforob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!lockBoolea) {
					Log.i("hzxlelp1", " 操作车锁");
					if (!IsOpen) {

						popDialog = PubileUI.MsgDialog(lockmain_contextmian,
								"正在开启车锁...");
						isclike = true;
						Message msg = new Message();
						writeCharAction("02", "F0", _lockkey);
						msg.what = 3003;
						handler.sendMessage(msg);

					} else {
						isclike = true;
						popDialog = PubileUI.MsgDialog(lockmain_contextmian,
								"正在关闭车锁...");
						Message msg = new Message();
						writeCharAction("02", "0F", _lockkey);
						msg.what = 3004;
						handler.sendMessage(msg);
					}
					popDialog.show();
					StatrTimer();
				} else {
					if ((System.currentTimeMillis() - showTime) > 2000) {
						ShowToast("通信失败,请先连接车锁!");
						showTime = System.currentTimeMillis();
					}
				}
			}
		});

		iv_lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// startActivity(new Intent(LockMain.this, Main.class));
				// TODO Auto-generated method stub
				if (!lockBoolea) {
					Log.i("hzxlelp1", " 操作车锁");
					if (!IsOpen) {
						popDialog = PubileUI.MsgDialog(lockmain_contextmian,
								"正在开启车锁...");
						isclike = true;
						Message msg = new Message();
						writeCharAction("02", "F0", _lockkey);
						msg.what = 3003;
						handler.sendMessage(msg);
					} else {
						isclike = true;
						popDialog = PubileUI.MsgDialog(lockmain_contextmian,
								"正在关闭车锁...");
						Message msg = new Message();
						writeCharAction("02", "0F", _lockkey);
						msg.what = 3004;
						handler.sendMessage(msg);
					}
					popDialog.show();
					StatrTimer();
				} else {
					if ((System.currentTimeMillis() - showTime) > 2000) {
						ShowToast("通信失败,请先连接车锁!");
						showTime = System.currentTimeMillis();
					}

				}
			}
		});
		lockmain_change = (Button) findViewById(R.id.lockmain_change);
		lockmain_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// Message msgq = new Message();
				// msgq.what = 3006;
				// handler.sendMessage(msgq);
				// if (mBLE != null)
				// {
				// mBLE.disconnect();
				// mBLE.close();
				// gattCharacteristic_oadimgidentify = null;
				// gattCharacteristic_oadimablock = null;
				// }
				if (mBluetoothAdapter != null)
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				// ISLIANJIE = false;
				// startActivity(new Intent(LockMain.this, MyCarLock.class));

				Intent intent = new Intent();
				intent.setClass(LockMain.this, MyCarLock.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 1);
			}
		});
		lockmain_bell = (Button) findViewById(R.id.lockmain_bell);
		lockmain_bell.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				writeCharAction("02", "55", _lockkey);
			}
		});

		Message msgq = new Message();
		msgq.what = 521;
		handler.sendMessage(msgq);
		if (Login.isNetLogin) {
			Message msg = new Message();
			msg.what = 520;
			LockMain.handler.sendMessage(msg);
		}

		Login.isLogin = true;

		lockmain_login_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (lockmain_username.getText().equals("点击登陆")) {
					startActivity(new Intent(LockMain.this,
							WXEntryActivity.class));
					// lockmain_dl.close();
				} else if (!lockmain_username.getText().equals("加载中...")) {
					startActivity(new Intent(LockMain.this, UserSetting.class));
					// lockmain_dl.close();
				} else if (lockmain_username.getText().equals("加载中...")) {
					ShowToast("加载登陆...");
				}
			}
		});

		Util.initImageLoader(this);
		initDragLayout();

		lockmain_iv_icon = (ImageView) findViewById(R.id.lockmain_iv_icon);
		lockmain_iv_icon_red = (ImageView) findViewById(R.id.lockmain_iv_icon_red);
		lockmain_rl_about_red = (ImageView) findViewById(R.id.lockmain_rl_about_red);

		lockmain_lv = (ListView) findViewById(R.id.lockmain_lv);

		lockmain_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:

					if (mBluetoothAdapter != null)
						mBluetoothAdapter.stopLeScan(mLeScanCallback);
					Intent intent = new Intent();
					intent.setClass(LockMain.this, SearchCarLock.class);
					intent.putExtra("automatic", "false");
					startActivityForResult(intent, 1);

					break;
				case 1:
					Intent intent1 = new Intent();
					intent1.setClass(LockMain.this, LockManage.class);
					startActivityForResult(intent1, 3);
					// IsLoadNewData=true;
					// startActivity(new Intent(LockMain.this,
					// LockManage.class));

					break;
				// case 2 :
				// // startActivity(new Intent(LockMain.this,
				// // VideoPlayer.class));
				// startActivity(new Intent(LockMain.this, ShareLock.class));
				// break;
				/*case 2:
					if (mBLE != null
							&& gattCharacteristic_oadimgidentify != null
							&& gattCharacteristic_oadimablock != null) {
						Intent intenta = new Intent();
						intenta.setClass(LockMain.this, FwUpdateActivity.class);
						startActivityForResult(intenta, 1);
					} else {
						ShowToast("请先连接车锁");
					}
					break;
				case 3:
					// Intent i = new Intent(Intent.ACTION_VIEW);
					// i.setData(Uri.parse("https://item.taobao.com/item.htm?spm=a230r.1.14.1.syElve&id=45763485020&ns=1&abbucket=12#detail"));
					// startActivity(i);
					Intent intent5 = new Intent();
					intent5.setClass(LockMain.this, About_WebView.class);
					intent5.putExtra("title", "购买车锁");
					intent5.putExtra(
							"url",
							"https://item.taobao.com/item.htm?spm=a230r.1.14.1.syElve&id=45763485020&ns=1&abbucket=12#detail");
					startActivity(intent5);
					break;*/
				default:
					break;
				}

			}
		});
		lockmain_iv_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				lockmain_dl.open();

			}
		});
		initView(getData());

		mBLE = new BluetoothLeClass(this);
		if (!mBLE.initialize()) {
			// 蓝牙不存在
			ShowToast("蓝牙未开启");
			return;
		}

		new Handler().postDelayed(new Runnable() {
			public void run() {
				mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
				mBLE.setOnDataAvailableListener(mOnDataAvailable);

			}
		}, 1000);
		progressDialog = PubileUI.MsgDialog(this, "正在初始化车锁...");
		popDialog = PubileUI.MsgDialog(this, "正在执行中...");
		// new Login.RequestTaskAppData().execute();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (AppSetting.AppVCode > 0
						&& ConnectionChangeReceiver.IsNetWork) {
					new checkNewestVersionAsyncTask().execute();
				} else {
					handler.postDelayed(this, 5000);
				}
			}
		}, 3000);
		
		
		/*final Message msg1 = new Message();
		msg1.what = 30031;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				_parkingname = "下沙村";
				handler.sendMessage(msg1);
			}
		}, 5000);*/
	}

	static void setBatteryCount(int num){
		if(num == 0){
			bat1.setImageResource(R.drawable.battery_n);
			bat2.setImageResource(R.drawable.battery_n);
			bat3.setImageResource(R.drawable.battery_n);
			bat4.setImageResource(R.drawable.battery_n);
			bat5.setImageResource(R.drawable.battery_n);
		}else if(num == 1 || num == 2){
			bat1.setImageResource(R.drawable.battery_y);
			bat2.setImageResource(R.drawable.battery_n);
			bat3.setImageResource(R.drawable.battery_n);
			bat4.setImageResource(R.drawable.battery_n);
			bat5.setImageResource(R.drawable.battery_n);
		} else if(num == 3 || num == 4){
			bat1.setImageResource(R.drawable.battery_y);
			bat2.setImageResource(R.drawable.battery_y);
			bat3.setImageResource(R.drawable.battery_n);
			bat4.setImageResource(R.drawable.battery_n);
			bat5.setImageResource(R.drawable.battery_n);
		} else if(num == 5 || num == 6){
			bat1.setImageResource(R.drawable.battery_y);
			bat2.setImageResource(R.drawable.battery_y);
			bat3.setImageResource(R.drawable.battery_y);
			bat4.setImageResource(R.drawable.battery_n);
			bat5.setImageResource(R.drawable.battery_n);
		} else if(num == 7 || num == 8){
			bat1.setImageResource(R.drawable.battery_y);
			bat2.setImageResource(R.drawable.battery_y);
			bat3.setImageResource(R.drawable.battery_y);
			bat4.setImageResource(R.drawable.battery_y);
			bat5.setImageResource(R.drawable.battery_n);
		} else if(num == 9 || num == 10){
			bat1.setImageResource(R.drawable.battery_y);
			bat2.setImageResource(R.drawable.battery_y);
			bat3.setImageResource(R.drawable.battery_y);
			bat4.setImageResource(R.drawable.battery_y);
			bat5.setImageResource(R.drawable.battery_y);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:

			if (resultCode == 100) {
				Bundle bundle = data.getExtras();
				// 显示蓝牙地址
				_lockBlueAddress = bundle.getString("macaddr");
				_lockName = bundle.getString("bluename");
				String software = bundle.getString("software");
				String hardware = bundle.getString("hardware");

				// _parkingname = bundle.getString("parkingname");
				Intent intent = new Intent();
				intent.setClass(LockMain.this, LockSetting.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("macaddr", _lockBlueAddress);
				intent.putExtra("taken", _Token);
				intent.putExtra("bluename", _lockName);
				intent.putExtra("software", software);
				intent.putExtra("hardware", hardware);
				startActivityForResult(intent, 2);
				IsLoadNewData = false;

			} else if (resultCode == 500) {
				Bundle bundle = data.getExtras();
				// 是否切换
				_IsChanges = bundle.getString("istrue");
				if (_IsChanges.equals("true")) {
					if (mBLE != null) {
						mBLE.disconnect();
						mBLE.close();
						gattCharacteristic_oadimgidentify = null;
						gattCharacteristic_oadimablock = null;
					}
					ISLIANJIE = false;
					IsLoadNewData = true;
					updateshow = true;
				}

			} else if (resultCode == 1000) {
				ShowToast("即将重新连接车锁");
				if (AppSetting.mSoftWare < 4) {
					finish();
				}
			}

			break;
		case 2:
			if (resultCode == 200) {
				Bundle bundle = data.getExtras();
				// 是否切换
				_IsChanges = bundle.getString("istrue");
				if (_IsChanges.equals("true")) {
					if (mBLE != null) {
						mBLE.disconnect();
						mBLE.close();
						gattCharacteristic_oadimgidentify = null;
						gattCharacteristic_oadimablock = null;
					}
					ISLIANJIE = false;
					IsLoadNewData = true;
					updateshow = true;
				}
			}
			break;
		case 3:
			if (resultCode == 100) {
				Bundle bundle = data.getExtras();
				// 是否切换
				_IsChanges = bundle.getString("istrue");
				if (_IsChanges.equals("true")) {
					if (mBLE != null) {
						mBLE.disconnect();
						mBLE.close();
						gattCharacteristic_oadimgidentify = null;
						gattCharacteristic_oadimablock = null;
					}
					ISLIANJIE = false;
					IsLoadNewData = true;
					updateshow = true;
				}
			}

			break;
		}
	}

	private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			displayGattServices(mBLE.getSupportedGattServices());
		}

	};
	/** 收到BLE终端数据交互的事�? */
	private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {
		/** BLE终端数据被读的事�? */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			String dataString = SimpleCrypto.bytesToHexString(characteristic
					.getValue());
			// char_display(dataString, characteristic.getValue(),
			// characteristic.getUuid().toString());

		}

		/** 收到BLE终端写入数据回调 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			if (characteristic.getUuid().toString().equals(UUID_CHAR1)) {
				Log.i("bbbbbbb", "写入回调开始");
				String dataString = SimpleCrypto
						.bytesToHexString(characteristic.getValue());
				int dataStringCount = dataString.split(" ").length;
				Log.i("bbbbbbb",
						"写入回调" + dataString.split(" ")[0] + "|"
								+ dataString.split(" ")[1] + "|"
								+ dataString.split(" ")[2]);
				if (dataStringCount == 3) {
					Log.i("bbbbbbb", "写入回调1");
					char_display(dataString.split(" ")[0],
							dataString.split(" ")[1], dataString.split(" ")[2]);
					Log.i("bbbbbbb", "写入回调2");
				}
			}
		}
	};
	private static Timer getTimer;

	private void displayGattServices(List<BluetoothGattService> gattServices) {
		Message msg = new Message();
		if (gattServices == null) {
			msg.what = 3005;
			handler.sendMessage(msg);
			return;
		} else {
			msg.what = 3002;
			handler.sendMessage(msg);
		}

		for (BluetoothGattService gattService : gattServices) {
			// -----Characteristics的字段信�?----//
			List<BluetoothGattCharacteristic> gattCharacteristics = gattService
					.getCharacteristics();
			for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

				if (gattCharacteristic.getUuid().toString().equals(UUID_CHAR1)) {
					// 把char1 保存起来�?以方便后面读写数据时使用
					gattCharacteristic_char1 = gattCharacteristic;

				}
			}
			// -----Service的字段信�?----//
			int type = gattService.getType();
			// find oad service uuid and oad chara uuid
			if (gattService.getUuid().equals(UUID_OAD_SERVICE)) {
				List<BluetoothGattCharacteristic> mGattCharacteristics = gattService
						.getCharacteristics();
				for (BluetoothGattCharacteristic mGattCharacteristic : mGattCharacteristics) {
					if (mGattCharacteristic.getUuid().equals(
							UUID_OAD_IMG_IDENTIFY)) {
						gattCharacteristic_oadimgidentify = mGattCharacteristic;
						mBLE.setCharacteristicNotification(mGattCharacteristic,
								true);
					} else if (mGattCharacteristic.getUuid().equals(
							UUID_OAD_IMG_BLOCK)) {
						gattCharacteristic_oadimablock = mGattCharacteristic;
						mBLE.setCharacteristicNotification(mGattCharacteristic,
								true);
					}
				}
			}

		}//
		if (AppSetting.mSoftWare >= 4) {
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					Boolean ssBoolean = mBLE.setCharacteristicNotification(
							gattCharacteristic_char1, true);
				}
			}).start();
		}
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// File fs=new File(BluetoothLeClass.mFilePath);
				if (AppSetting.mSoftWare > 0
						&& AppSetting.mSoftWare < AppSetting.serviceSoftVersion
						&& updateshow) {
					updateshow = false;
					hardNewVersionUpdate();
				} else {
					// handler.postDelayed(this, 5000);
				}
			}
		}, 3000);
	}

	private static Timer listerTimer;
	private int numberout = 0;

	public class TimerTasklister extends TimerTask {

		@Override
		public void run() {
			if (isclike) {
				numberout++;
				if (numberout > 3) {
					Log.i("hzxlelp1", " 尝试失败");
					numberout = 0;
					isclike = false;
					Message msgMessage = new Message();
					msgMessage.what = 70001;
					handler.sendMessage(msgMessage);
					if (listerTimer != null)
						listerTimer.cancel();
					listerTimer = null;
				} else {
					if (!IsOpen) {
						// popDialog = PubileUI.MsgDialog(lockmain_contextmian,
						// "正在开启车锁...");
						Log.i("hzxlelp1", "F0 尝试第" + numberout + "次");
						isclike = true;
						Message msg = new Message();
						writeCharAction("02", "F0", _lockkey);
						msg.what = 3003;
						handler.sendMessage(msg);
					} else {
						Log.i("hzxlelp1", "0F 尝试第" + numberout + "次");
						isclike = true;
						// popDialog = PubileUI.MsgDialog(lockmain_contextmian,
						// "正在关闭车锁...");
						Message msg = new Message();
						writeCharAction("02", "0F", _lockkey);
						msg.what = 3004;
						handler.sendMessage(msg);
					}
				}
			}
		}
	}

	public class TimerTaskTest extends TimerTask {

		@Override
		public void run() {
			if (n_isNew) {
				n_isNew = false;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (n_CTLString.equals("0f")) {
					// n_isToken="b0";
					if (n_isToken.equals("b0")) {
						writeCharTaken("FF", _LockTaken, _locksn,
								_lockBlueAddress);
					} else {
						writeCharKey("01", _LockTaken, _lockkey);
					}

				} else if (n_CTLString.equals("ff")) {
					if (n_isAction.equals("ff")) {
						writeCharKey("01", _LockTaken, _lockkey);
					} else {

						int dqz = Integer.parseInt(n_isAction);
						int sumz = dataSumStringArr.length;
						if (dqz < sumz) {
							Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
							boolean bRet = gattCharacteristic_char1
									.setValue(StringToByteArray(dataSumStringArr[dqz]));
							Boolean iswrite = mBLE
									.writeCharacteristic(gattCharacteristic_char1);
						}
					}
				} else if (n_CTLString.equals("01")) {
					Log.i("hzxlelp3", n_isAction);
					if (n_isAction.equals("0f") || n_isAction.equals("f0")
							|| n_isAction.equals("aa")) {
						isWriteOkBoolean = true;
						Message msg = new Message();
						IsAction = n_isAction;
						if (IsAction.equals("0f")) {
							msg.what = 30041;
						} else if (IsAction.equals("f0")) {
							msg.what = 30031;
						}
						if (!lockBoolea) {
							handler.sendMessage(msg);
						}
						// if (dqz >= sumz)
						// {
						// Read_Char3();
						// }
					} else {

						int dqz = Integer.parseInt(n_isAction);
						int sumz = dataSumStringArr.length;
						if (dqz < sumz) {
							Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
							boolean bRet = gattCharacteristic_char1
									.setValue(StringToByteArray(dataSumStringArr[dqz]));
							Boolean iswrite = mBLE
									.writeCharacteristic(gattCharacteristic_char1);
						}
						Log.i("hzxlelp3", n_isAction + "||" + sumz);
					}
				} else if (n_CTLString.equals("02")) {
					if (n_isAction.equals("f0") || n_isAction.equals("0f")
							|| n_isAction.equals("aa")) {
						numberout = 0;
						isclike = false;
						if (listerTimer != null)
							listerTimer.cancel();
						listerTimer = null;

						// actionHandler.removeCallbacks(actionRunnable);
						// actionRunnable=null;

					} else {

						int dqz = Integer.parseInt(n_isAction);
						int sumz = dataSumStringArr.length;
						if (dqz < sumz) {
							Log.i("bbbbbbb", "写 " + n_CTLString + dqz);
							boolean bRet = gattCharacteristic_char1
									.setValue(StringToByteArray(dataSumStringArr[dqz]));
							Boolean iswrite = mBLE
									.writeCharacteristic(gattCharacteristic_char1);
						}
					}

				} else if (n_CTLString.equals("03")) {
					// lockmain_lockofforob.setEnabled(true);
					if (n_isAction.equals("f0") || n_isAction.equals("0f")) {
						// isclike = true;
						if (!lockBoolea) {
							Message msg = new Message();
							if (n_isAction.equals("0f")) {
								msg.what = 30041;
							} else if (n_isAction.equals("f0")) {
								msg.what = 30031;
							}
							handler.sendMessage(msg);
						}
					}
					actionHandler.removeCallbacks(actionRunnable);
					actionRunnable = null;
					numberout = 0;
					isclike = false;
					if (listerTimer != null)
						listerTimer.cancel();
					listerTimer = null;
					if (n_isAction.equals("aa")) {
						Message msgMessage = new Message();
						msgMessage.what = 70001;
						handler.sendMessage(msgMessage);
					}

				}

			}

		}
	}

	private String n_CTLString = "";
	private String n_isToken = "";
	private String n_isAction = "";
	private Boolean n_isNew = false;

	private void char_display(String CTL, String isToken, String isAction) {
		n_CTLString = CTL;
		n_isToken = isToken;
		n_isAction = isAction;

		if (n_CTLString.equals("ff")) {
			if (isToken.equals("22")) {
				if (isAction.equals("ff")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：",
							"Token 解析正确，写入flash正确");
				} else if (isAction.equals("aa")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "Token 解密失败");
				}
			} else if (isToken.equals("0a")) {
				mdb.writeLogTxt("操作指令 " + CTL + " ：", "Toke解析正确，写入flash错误");
			}
		} else if (n_CTLString.equals("01")) {
			Log.i("bbbbbbb", "写入回调3" + n_CTLString);
			if (isToken.equals("22")) {
				Log.i("bbbbbbb", "写入回调4" + isToken);
				if (isAction.equals("aa")) {
					Log.i("bbbbbbb", "写入回调5" + isAction);
					Message msg = new Message();
					msg.what = 3005;
					handler.sendMessage(msg);

					mdb.writeLogTxt("操作指令 " + CTL + " ：", "Key解密失败");
					if (!isWriteOkBoolean) {
						lockBoolea = true;
						if (progressDialog != null) {
							progressDialog.hide();
							progressDialog.dismiss();
							// ShowToast("初始化失败，请重新连接车锁");
						}

						if (mBLE != null) {
							mBLE.disconnect();
							mBLE.close();
							gattCharacteristic_oadimgidentify = null;
							gattCharacteristic_oadimablock = null;
						}
						ISLIANJIE = false;

					}

				} else if (isAction.equals("0f")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "Key解析正确，车锁90°");
				} else if (isAction.equals("f0")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "Key解析正确，车锁0°");
				}
			}
		} else if (n_CTLString.equals("02")) {

			if (isToken.equals("00")) {
				if (isAction.equals("aa")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "控制命令解密失败");
					if (!isWriteOkBoolean) {
						lockBoolea = true;
						progressDialog.hide();
						progressDialog.dismiss();
						ShowToast("指令非法");
					}
				} else if (isAction.equals("0f")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "控制命令下降解析正确");
				} else if (isAction.equals("f0")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "控制命令上升车锁正确");
				}
			}
		} else if (n_CTLString.equals("0f")) {
			if (isToken.equals("b0")) {
				if (isAction.equals("aa")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁未激活，状态不定");
				} else if (isAction.equals("0f")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁未激活，状态90°");
				} else if (isAction.equals("f0")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁未激活，状态0°");
				}
			} else if (isToken.equals("b1")) {
				if (isAction.equals("aa")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁已经激活，状态不定");
				} else if (isAction.equals("0f")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁已经激活，状态90°");
				} else if (isAction.equals("f0")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁已经激活，状态0°");
				}
			}
		} else if (n_CTLString.equals("aa")) {
			if (isToken.equals("ff")) {
				if (isAction.equals("ab")) {
					mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁被阻挡");
				}

			} else if (isToken.equals("00")) {
				Message msg = new Message();
				msg.what = 80002;
				handler.sendMessage(msg);

				mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁被认为扳动");

			} else {
				Message msg = new Message();
				msg.what = 80001;
				handler.sendMessage(msg);
				mdb.writeLogTxt("操作指令 " + CTL + " ：", "车锁被阻挡");
			}
		}
		n_isNew = true;
	}

	public void writeCharTaken(String Type, String TaKen, String SN,
			String BuleAddress) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd-HH-mm");
		String date = sDateFormat.format(new java.util.Date());
		date = to16(date);

		String fields = SimpleCrypto.str2HexStr("INIT:");
		String strmd5Taken = " 10 "
				+ SimpleCrypto.str2HexStr(SimpleCrypto.MD5(TaKen));
		String strmd5SN = " 0B " + SimpleCrypto.str2HexStr(SN);

		String DataWT = fields + strmd5Taken + strmd5SN + " " + date;

		byte[] Seed = "ACWLSHYOEFZGJRMX".getBytes();
		byte[] byteAction = StringToByteArray(DataWT);
		String ddddddString = null;
		try {
			ddddddString = SimpleCrypto.encrypt(Seed, byteAction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String dataSumString = "00 00 34 BB BB BB BB " + ddddddString;
		writeblue(Type, dataSumString);
	}

	private static String to16(String data) {
		String newdataString = "";
		String[] daStrings = data.split("-");
		for (int i = 0; i < daStrings.length; i++) {
			String dt = Integer.toHexString(Integer.parseInt(daStrings[i]));
			if (dt.length() == 1) {
				dt = "0" + dt;
			}
			if (i < daStrings.length - 1) {
				newdataString += dt + " ";
			} else {
				newdataString += dt;
			}
		}

		return newdataString;
	}

	public void writeCharKey(String Type, String TaKen, String Key) {
		byte[] Seed = StringToByteArray(SimpleCrypto.str2HexStr(SimpleCrypto
				.MD5(TaKen)));
		String dataDW = SimpleCrypto.str2HexStr("KEY2:") + " 10 "
				+ SimpleCrypto.str2HexStr(SimpleCrypto.MD5(Key));
		byte[] byteAction = StringToByteArray(dataDW);
		String ddddddString = null;
		try {
			ddddddString = SimpleCrypto.encrypt(Seed, byteAction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dataSumString = "00 00 24 BB BB BB BB " + ddddddString;

		writeblue(Type, dataSumString);

	}

	public void writeCharAction(String Type, String Action, String Key) {

		SimpleDateFormat sDateFormat = new SimpleDateFormat("yy-MM-dd-HH-mm");
		String date = sDateFormat.format(new java.util.Date());
		date = to16(date);
		String dataDW = SimpleCrypto.str2HexStr("CTL:") + " " + Action
				+ " 0F 05 13 0a 20 " + date + " 0F 05 13 0a 25";
		byte[] Seed = StringToByteArray(SimpleCrypto.str2HexStr(SimpleCrypto
				.MD5(Key)));
		byte[] byteAction = StringToByteArray(dataDW);
		String ddddddString = null;
		try {
			ddddddString = SimpleCrypto.encrypt(Seed, byteAction);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String DataWT = " BB BB BB BB " + ddddddString;
		String dataSumString = "00 00 24" + DataWT;

		writeblue(Type, dataSumString);
	}

	private void writeblue(final String Type, final String dataSumString) {

		int counts = dataSumString.length() / 48;
		counts++;
		dataSumStringArr = new String[counts];
		for (int i = 0; i < counts; i++) {
			String minData = "";
			if (i < dataSumString.length() / 48) {
				minData = Type + " 00 0" + Integer.toHexString(i + 1) + " "
						+ dataSumString.substring(i * 48, (i + 1) * 48);

			} else {
				minData = Type
						+ " 00 0"
						+ Integer.toHexString(i + 1)
						+ " "
						+ dataSumString.substring(i * 48,
								dataSumString.length());
			}
			dataSumStringArr[i] = minData;
		}


		Log.d("write",dataSumStringArr[0]);
		try {
			Log.i("bbbbbbb", "写 " + Type + "第一针");
			boolean bRet = gattCharacteristic_char1
					.setValue(StringToByteArray(dataSumStringArr[0]));
			Boolean iswrite = mBLE
					.writeCharacteristic(gattCharacteristic_char1);
		} catch (Exception exception) {
			String exString = exception.getMessage();
		}

	}

	public static void SuperWriteZooe() {
		boolean bRet = gattCharacteristic_char1
				.setValue(StringToByteArray("EF AB 37"));
		Boolean iswrite = mBLE.writeCharacteristic(gattCharacteristic_char1);
	}

	static byte[] StringToByteArray(String str) {
		String[] str_ary = str.split(" ");
		int n = str_ary.length;
		byte[] bt_ary = new byte[n];
		for (int i = 0; i < n; i++)
			bt_ary[i] = (byte) Integer.parseInt(str_ary[i], 16);
		return bt_ary;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
		// mManager.registerListener(this,
		// mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),// 距离感应器
		// SensorManager.SENSOR_DELAY_NORMAL);//
		// 注册传感器，第一个参数为距离监听器，第二个是传感器类型，第三个是延迟类型

		lockmain_IsLogoWX = true;
		if (IsLoadNewData) {
			new WorkTheard().start();
			IsLoadNewData = false;
		}

		getTimer = new Timer();
		getTimer.schedule(new TimerTaskTest(), 10, 10);
		listerTimer = new Timer();
		// lockmain_lockofforob.setEnabled(true);
	}

	private void StatrTimer() {
		if (listerTimer != null) {
			listerTimer.cancel();
			listerTimer = null;
		}

		if (listerTimer == null) {
			listerTimer = new Timer();
			listerTimer.schedule(new TimerTasklister(), 4000, 4000);
		}
		actionHandler = new Handler();
		actionRunnable = new Runnable() {

			public void run() {

				if (popDialog != null) {
					popDialog.cancel();
					popDialog.dismiss();
					popDialog = null;
				}
			}

		};
		actionHandler.postDelayed(actionRunnable, 10000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 退出时销毁定位
		// if (mManager != null)
		// {
		// localWakeLock.release();//
		// 释放电源锁，如果不释放finish这个acitivity后仍然会有自动锁屏的效果，不信可以试一试
		// mManager.unregisterListener(this);// 注销传感器监听
		// }
		//mBluetoothAdapter.disable();
		Login.LoadUserData = true;
		lockmain_LoadUserData = true;
		lockBoolea = true;
		ISLIANJIE = false;
		super.onDestroy();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}

	@SuppressLint("NewApi")
	private void initDragLayout() {
		lockmain_dl = (DragLayout) findViewById(R.id.lockmain_dl);

		lockmain_dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				lockmain_lv.smoothScrollToPosition(0);
			}

			@Override
			public void onClose() {

			}

			@Override
			public void onDrag(float percent) {
				// ViewHelper.setAlpha(lockmain_mainshow, 1 - percent);
			}
		});
	}

	@SuppressLint("ShowToast")
	private static void ShowToast(String msg) {
		Toast.makeText(lockmain_contextmian, msg, 100).show();
	}

	private static void initView(List<Map<String, Object>> data) {

		if (AppSetting.AppVCode > mdb.getVersion() && AppSetting.AppVCode > 0) {
			lockmain_rl_about_red.setVisibility(View.VISIBLE);
		} else {
			lockmain_rl_about_red.setVisibility(View.GONE);
		}

		if ((AppSetting.AppVCode > mdb.getVersion() && AppSetting.AppVCode > 0)
				|| (AppSetting.mSoftWare > 0 && AppSetting.mSoftWare < AppSetting.serviceSoftVersion)) {
			lockmain_iv_icon_red.setVisibility(View.VISIBLE);
		} else {
			lockmain_iv_icon_red.setVisibility(View.GONE);
		}

		adapter = new SimpleAdapter(lockmain_contextmian, data,
				R.layout.listitem,
				new String[] { "nametext", "iconid", "red" }, new int[] {
						R.id.m_item_tv, R.id.m_item_iv, R.id.m_item_iv_red });
		lockmain_lv.setAdapter(null);
		lockmain_lv.setAdapter(adapter);

	}

	private static void connectionBlue() {
		boolean bRet = mBLE.connect(_lockBlueAddress);
		Log.i("hzxlelp6", bRet + "");
		Message msg = new Message();
		msg.what = 3001;
		handler.sendMessage(msg);
	}

	private static BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {

			final iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi,
					scanRecord);
			// _lockBlueAddressLocad = "54:4A:16:6F:6D:20";

			Log.i("hzxlelp1", ibeacon.bluetoothAddress);
			String dataBlue = ibeacon.proximityUuid;
			if (ibeacon.bluetoothAddress.equals(_lockBlueAddressLocad)) {
				mdb.writeLogTxt("搜到设备,广播数据包为", dataBlue + "--------"
						+ ISLIANJIE);
				Log.i("hzxlelp5", dataBlue + "--------" + ISLIANJIE);
			}
			if (ibeacon.bluetoothAddress.equals(_lockBlueAddressLocad)
					&& !ISLIANJIE && dataBlue.length() > 60)// _lockBlueAddressLocad
			{
				mdb.writeLogTxt("搜到设备", "通过数据判断，开始解析");
				ISLIANJIE = true;
				if (mBluetoothAdapter != null)
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				_lockBlueAddress = _lockBlueAddressLocad;
				if (lockBoolea) {
					lockBoolea = false;

					if (dataBlue.length() >= 56
							&& dataBlue.substring(8, 24).equals(
									"4a4f594f54494d45")) {
						String battery = dataBlue.substring(42, 44);
						int batteryint = Integer.parseInt(battery, 16);
						String sSoftWare = dataBlue.substring(60, 62);
						if (sSoftWare.equals("23")) {
							AppSetting.mSoftWare = 1;
							AppSetting.mHardWare = 10;
						} else {
							AppSetting.mSoftWare = Integer.parseInt(sSoftWare,
									16);
							String sHardWare = dataBlue.substring(58, 60);
							AppSetting.mHardWare = Integer.parseInt(sHardWare,
									16);
							connectionBlue();
						}
						Message msgs = new Message();
						msgs.what = 70000;

						handler.sendMessage(msgs);
						Message msg = new Message();
						msg.what = batteryint;
						handler.sendMessage(msg);

					} else if (dataBlue.length() >= 60
							&& (dataBlue.substring(10, 18)).equals("4a4f594f")) {
						String battery = dataBlue.substring(36, 38);
						int batteryint = Integer.parseInt(battery, 16);
						String sSoftWare = dataBlue.substring(54, 56);
						AppSetting.mSoftWare = Integer.parseInt(sSoftWare, 16);
						String sHardWare = dataBlue.substring(52, 54);
						AppSetting.mHardWare = Integer.parseInt(sHardWare, 16);
						Message msgs = new Message();
						msgs.what = 70000;

						handler.sendMessage(msgs);
						Message msg = new Message();
						msg.what = batteryint;
						handler.sendMessage(msg);
						connectionBlue();
					}

				}
			}

		}
	};

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// System.exit(0);
				_locksn = "";
				_lockBlueAddressLocad = "";
				_lockkey = "";
				_LockTaken = "";
				_lockName = "";
				_parkingname = "";
				if (mBLE != null) {
					mBLE.disconnect();
					mBLE.close();
				}
				if (mBluetoothAdapter != null)
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				ISLIANJIE = false;
				finish();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private static Runnable task = new Runnable() {

		public void run() {
			AppSetting.serviceSoftVersion = mdb
					.GetOneVersion(AppSetting.mHardWare);
			initView(getData());
		}
	};

	private static List<Map<String, Object>> getData() {

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("nametext", "添加车锁");
		map1.put("iconid", R.drawable.menu_icon_add);
		map1.put("red", R.drawable.gen_null_red);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("nametext", "车锁管理");
		map2.put("iconid", R.drawable.menu_icon_lock);
		map2.put("red", R.drawable.gen_null_red);

		// Map<String, Object> map3 = new HashMap<String, Object>();
		// map3.put("nametext", "已共享车锁");
		// map3.put("iconid", R.drawable.menu_icon_share);
		// map3.put("red", R.drawable.gen_null_red);

		/*Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("nametext", "更新固件");
		map4.put("iconid", R.drawable.menu_icon_update);
		if (AppSetting.mSoftWare > 0
				&& AppSetting.mSoftWare < AppSetting.serviceSoftVersion) {
			map4.put("red", R.drawable.gen_point);
			// Message msg = new Message();
			// msg.what = 5000;
			// handler.sendMessage(msg);

		} else {
			map4.put("red", R.drawable.gen_null_red);
		}

		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("nametext", "购买车锁");
		map5.put("iconid", R.drawable.menu_lockshopicon);
		map5.put("red", R.drawable.gen_null_red);*/

		data.add(map1);
		data.add(map2);
		// data.add(map3);
		//data.add(map4);
		//data.add(map5);
		return data;
	}

	private Handler showhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Intent intent = new Intent();
				intent.setClass(LockMain.this, SearchCarLock.class);
				intent.putExtra("automatic", "false");
				startActivityForResult(intent, 1);
			}
		}
	};

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what <= 100 && msg.what > 0) {
				battrry_count.setText(msg.what + "%");
				lockmain_battery.setVisibility(View.VISIBLE);
				battrry_count.setVisibility(View.VISIBLE);

				int cccsz = msg.what;
				float csz = cccsz / 10f;
				int aa = (int) Math.floor(csz);
				setBatteryCount(aa);

			} else if (msg.what == 520) {
				if (!_OpenID.equals("")) {
					if (lockmain_username != null)
						lockmain_username.setText(_UserName);
					if (_headimage != null && lockmain_headimage != null) {
						lockmain_headimage.setImageBitmap(_headimage);
					}
				}
			} else if (msg.what == 521) {
				lockmain_username.setText("登录中...");
			} else if (msg.what == 3001) {
				progressDialog.show();
				tv_tomattext.setText("正在连接车锁");
				lockBoolea = true;

				isWriteOkBoolean = false;
				new Handler().postDelayed(new Runnable() {

					public void run() {
						if (!isWriteOkBoolean) {
							lockBoolea = true;
							progressDialog.hide();
							progressDialog.dismiss();
							// ShowToast("初始化失败，请重新连接车锁");
							ISLIANJIE = false;

							if (mBLE != null) {
								mBLE.disconnect();
								mBLE.close();
								gattCharacteristic_oadimgidentify = null;
								gattCharacteristic_oadimablock = null;
							}

							if (mBluetoothAdapter != null)
								mBluetoothAdapter.startLeScan(mLeScanCallback);
						}
					}

				}, 10000);
			} else if (msg.what == 3002) {
				if (progressDialog != null && tv_tomattext != null
						&& lockmain_link != null) {
					progressDialog.show();

					tv_tomattext.setText("车锁已连接");
					lockmain_link.setBackground(Resourcesimg
							.getDrawable(R.drawable.gen_icon_link));
					lockBoolea = false;

					isWriteOkBoolean = false;
					new Handler().postDelayed(new Runnable() {

						public void run() {
							if (!isWriteOkBoolean) {
								lockBoolea = true;
								progressDialog.hide();
								progressDialog.dismiss();
								// ShowToast("初始化失败，请重新连接车锁");
								ISLIANJIE = false;

								if (mBLE != null) {
									mBLE.disconnect();
									mBLE.close();
									gattCharacteristic_oadimgidentify = null;
									gattCharacteristic_oadimablock = null;
								}

								if (mBluetoothAdapter != null)
									mBluetoothAdapter
											.startLeScan(mLeScanCallback);
							}
						}

					}, 10000);
				}
			} else if (msg.what == 3003) {
				// tv_tomattext.setText("车锁正在开启...");
			} else if (msg.what == 30031) {
				iv_lock.setBackgroundResource(R.anim.lockopen);
				_lockanimat = (AnimationDrawable) iv_lock.getBackground();
				_lockanimat.setOneShot(true);
				// TODO Auto-generated method stub
				if (!_lockanimat.isRunning())// 是否正在运行？
				{
					_lockanimat.start();// 启动
				}
				tv_tomattext.setText("车位锁已下降");
				lockmain_lockofforob.setBackground(Resourcesimg
						.getDrawable(R.drawable.btn_lock_onx));
				IsOpen = true;

				isWriteOkBoolean = true;
				progressDialog.hide();
				progressDialog.dismiss();
				if (popDialog != null) {
					popDialog.hide();
					popDialog.dismiss();
				}
			} else if (msg.what == 300311) {
				iv_lock.setBackgroundResource(R.anim.lockopen);
				_lockanimat = (AnimationDrawable) iv_lock.getBackground();
				_lockanimat.setOneShot(true);
				// TODO Auto-generated method stub
				if (!_lockanimat.isRunning())// 是否正在运行？
				{
					_lockanimat.start();// 启动
				}
				lockmain_lockofforob.setBackground(Resourcesimg
						.getDrawable(R.drawable.btn_lock_onx));
				FirstTime = false;
				IsOpen = true;
				isWriteOkBoolean = true;
				progressDialog.hide();
				progressDialog.dismiss();
			} else if (msg.what == 300411) {
				iv_lock.setBackgroundResource(R.anim.lockclose);
				_lockanimat = (AnimationDrawable) iv_lock.getBackground();
				_lockanimat.setOneShot(true);
				// TODO Auto-generated method stub
				if (!_lockanimat.isRunning())// 是否正在运行？
				{
					_lockanimat.start();// 启动

				}
				lockmain_lockofforob.setBackground(Resourcesimg
						.getDrawable(R.drawable.btn_lock_offx));
				FirstTime = false;
				IsOpen = false;
				isWriteOkBoolean = true;
				progressDialog.hide();
				progressDialog.dismiss();
			} else if (msg.what == 3004) {
				// tv_tomattext.setText("车锁正在关闭...");
			} else if (msg.what == 30041) {

				iv_lock.setBackgroundResource(R.anim.lockclose);
				_lockanimat = (AnimationDrawable) iv_lock.getBackground();
				_lockanimat.setOneShot(true);
				// TODO Auto-generated method stub
				if (!_lockanimat.isRunning())// 是否正在运行？
				{
					_lockanimat.start();// 停止

				}

				tv_tomattext.setText("车位锁已升起");
				lockmain_lockofforob.setBackground(Resourcesimg
						.getDrawable(R.drawable.btn_lock_offx));
				IsOpen = false;
				isWriteOkBoolean = true;
				progressDialog.hide();
				progressDialog.dismiss();

				if (popDialog != null) {
					popDialog.hide();
					popDialog.dismiss();
				}
			} else if (msg.what == 3005) {
				tv_tomattext.setText("车锁连接失败");
				lockmain_link.setBackground(Resourcesimg
						.getDrawable(R.drawable.gen_icon_nolink));
			} else if (msg.what == 3006) {
				// getTimer.cancel();
				if (tv_tomattext != null && lockmain_link != null
						&& lockmain_battery != null) {
					tv_tomattext.setText("连接已断开");
					lockmain_link.setBackground(Resourcesimg
							.getDrawable(R.drawable.gen_icon_nolink));
					lockmain_battery.setVisibility(View.GONE);
					battrry_count.setVisibility(View.GONE);
					progressDialog.hide();
					progressDialog.dismiss();
					if (popDialog != null) {
						popDialog.hide();
						popDialog.dismiss();
					}
					ISLIANJIE = false;
				}
				lockBoolea = true;

				if (mBluetoothAdapter != null && !IsLoadNewData)
					mBluetoothAdapter.startLeScan(mLeScanCallback);

			} else if (msg.what == 3007) {
				tv_tomattext.setText("车锁不在附近");
				if (tv_tomattext != null && lockmain_link != null) {
					lockmain_link.setBackground(Resourcesimg
							.getDrawable(R.drawable.gen_icon_nolink));
					lockmain_battery.setVisibility(View.GONE);
					battrry_count.setVisibility(View.GONE);
				}
			} else if (msg.what == 3008) {
				tv_tomattext.setText("还未绑定车锁");
				if (tv_tomattext != null && lockmain_link != null) {
					lockmain_link.setBackground(Resourcesimg
							.getDrawable(R.drawable.gen_icon_nolink));
					lockmain_battery.setVisibility(View.GONE);
					battrry_count.setVisibility(View.GONE);
					lockmain_bluename.setText("车锁名称");
					lockmain_connect.setText("未连接");
					lockmain_connect.setBackgroundColor(Color.TRANSPARENT);
					lockmain_parkingaddress.setText("停车场地址");
				}
				lockBoolea = true;

			} else if (msg.what == 3009) {
				tv_tomattext.setText("正在搜索车锁...");
				lockBoolea = true;
				if (tv_tomattext != null && lockmain_link != null) {
					lockmain_link.setBackground(Resourcesimg
							.getDrawable(R.drawable.gen_icon_nolink));
					lockmain_battery.setVisibility(View.GONE);
					battrry_count.setVisibility(View.GONE);
					lockmain_bluename.setText(_lockName);
					lockmain_connect.setText("");
					lockmain_connect.setBackgroundResource(R.drawable.already_connect);
					if(null == _parkingname || _parkingname.equals("")){
						lockmain_parkingaddress.setText("无效的停车场名");
					} else {
						lockmain_parkingaddress.setText(_parkingname);
					}
				}
			} else if (msg.what == 3500) {
				tv_tomattext.setText("操作过于频繁");
			} else if (msg.what == 5000) {

				// ShowToast("请及时更新硬件固件");
			} else if (msg.what == 5001) {
				// ShowToast("下载失败");
			} else if (msg.what == 70000) {
				handler.postDelayed(task, 3000);

			} else if (msg.what == 70001) {
				if (popDialog != null) {
					popDialog.hide();
					popDialog.dismiss();
				}
				ShowToast("请重试");
			} else if (msg.what == 70002) {
				// update();
			} else if (msg.what == 80001) {
				ShowToast("车锁被阻挡");
			} else if (msg.what == 80002) {
				ShowToast("车锁被人为扳动");
			}

		}
	};

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		// float[] its = event.values;
		// //
		// Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
		// if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY)
		// {
		// // 经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
		// if (its[0] == 0.0)
		// {
		// // 贴近手机
		// if (localWakeLock.isHeld())
		// {
		// return;
		// }
		// else
		// {
		// Log.i("hzxlelp4", "acquire");
		// localWakeLock.acquire();// 申请设备电源锁
		// }
		//
		// }
		// else
		// {// 远离手机
		// if (localWakeLock.isHeld())
		// {
		// return;
		// }
		// else
		// {
		// Log.i("hzxlelp4", "release");
		// localWakeLock.setReferenceCounted(false);
		// localWakeLock.release(); // 释放设备电源锁
		// }
		// }
		//
		// }

	}

}
