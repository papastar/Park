package com.joyotime.qparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends Activity
{
	final int getAddSuccess = 0;
	final int getAddFail = 1;

	private ImageView iv_icon;
	private double mQJLat1, mQJLon1, mQJLat2, mQJLon2;

	private EditText mEtSearch = null;// 输入搜索内容
	private Button mBtnClearSearchText = null;// 清空搜索信息的按钮
	private LinearLayout mLayoutClearSearchText = null;
	// 当前城市
	private String LocationCity = "";
	// 地图
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	static public String wx_codeString;
	static public Boolean IsLogoWX = true;

	Context contextmian;
	// private Marker mMarkerMove;
	// UI相关
	ImageButton requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位
	private UiSettings mUiSettings;

	private InfoWindow mInfoWindow;

	LinearLayout showdeatil_p;

	RelativeLayout main_bottom;
	Button main_NavBaiDu;
	Button main_nowbooking;
	private boolean mIsEngineInitSuccess = false;
	TextView main_parkname;
	TextView main_fristprice;
	TextView main_available;
	static ImageView loadingmove_m;
	static ImageView loadingmove_cilck_m;
	static ImageView loading_ok_m;
	static TextView loading_text_ok;

	// 中心坐标
	double centerLng, centerLat;
	// 点击点
	private Marker oldMarker = null;
	private String NavTitle = "";
	Animation hyperspaceJumpAnimation;

	static MethodDate mdb;
	// 用户信息
	static public String _OpenID = "";
	static public String _UserName = "";
	static public String _Token = "";
	static public Bitmap _headimage = null;
	private static Boolean LoadUserData = true;

	/** 构造广播监听类，监听 SDK key 验证以及网络异常广播 */
	public class SDKReceiver extends BroadcastReceiver
	{
		public void onReceive(Context context, Intent intent)
		{
			String s = intent.getAction();

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR))
			{
				String qqString = s;
			}
			else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR))
			{
				String wifiString = "error";
			}
		}
	}
	private SDKReceiver mReceiver;

//	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener()
//	{
//		public void engineInitSuccess()
//		{
//			mIsEngineInitSuccess = true;
//		}
//
//		public void engineInitStart()
//		{
//		}
//
//		public void engineInitFail()
//		{
//		}
//	};
	private String getSdcardDir()
	{
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private class RequestTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... params)
		{
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			return RequestData(centerLng, centerLat);
		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (result == null)
			{
				Toast.makeText(Main.this, "请求数据失败", Toast.LENGTH_LONG).show();
			}
			else if (result != null && !result.equals("401"))
			{
				// 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
				try
				{
					JSONAnalysis(result);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (result.equals("401"))
			{
				Toast.makeText(Main.this, "请重新登陆", Toast.LENGTH_LONG).show();

			}

		}
	}

	
	
	/** 网络请求，这里用的是HttpClient，区别于上一篇文章的HttpURLConnection。
	 * 
	 * @return */
	public String RequestData(double lng, double lat)
	{
		HttpClient httpClient = new DefaultHttpClient();

		String url = AppSetting.URL_STRING+"parkinglots/near/" + lng + "," + lat;
		HttpGet get = new HttpGet(url);
		get.addHeader("Authorization", _Token);
		StringBuilder builder = null;

		try
		{
			HttpResponse response = httpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				builder = new StringBuilder();
				String s = null;
				for (s = reader.readLine(); s != null; s = reader.readLine())
				{
					builder.append(s);
				}

				return builder.toString();
			}
			else if (response.getStatusLine().getStatusCode() == 401)
			{
				return "401";
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/** 对请求回来的数据进行JSON解析。
	 * 
	 * @param result
	 * @throws JSONException */
	public void JSONAnalysis(String result) throws JSONException
	{
		JSONArray object = null;
		try
		{
			object = new JSONArray(result);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		JSONObject ObjectInfo = null;

		for (int i = 0; i < object.length(); i++)
		{
			ObjectInfo = object.getJSONObject(i);

			String dis = ObjectInfo.optString("dis");
			String obj_type = ObjectInfo.getJSONObject("obj").optString("type");
			String obj_name = ObjectInfo.getJSONObject("obj").optString("name");
			String obj_available = ObjectInfo.getJSONObject("obj").optString("available");
			String obj_ID = ObjectInfo.getJSONObject("obj").optString("_id");
			String obj_address = ObjectInfo.getJSONObject("obj").optString("address");
			String obj_pricing_starting = ObjectInfo.getJSONObject("obj").getJSONObject("pricing").optString("starting");
			String obj_pricing_startingHours = ObjectInfo.getJSONObject("obj").getJSONObject("pricing").optString("startingHours");
			String obj_pricing_perHour = ObjectInfo.getJSONObject("obj").getJSONObject("pricing").optString("perHour");
			String obj_location_type = ObjectInfo.getJSONObject("obj").getJSONObject("location").optString("type");
			double obj_location_coordinates_lng = ObjectInfo.getJSONObject("obj").getJSONObject("location").getJSONArray("coordinates").getDouble(0);
			double obj_location_coordinates_lat = ObjectInfo.getJSONObject("obj").getJSONObject("location").getJSONArray("coordinates").getDouble(1);
			// 隐藏比例尺控件
			LatLng point = new LatLng(obj_location_coordinates_lat, obj_location_coordinates_lng);// 104.077894,30.556172
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.map_p_blue);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option1 = new MarkerOptions().position(point).icon(bitmap)
					.title(obj_ID + "/t" + obj_name + "/t" + obj_pricing_starting + "/t" + obj_pricing_startingHours + "/t" + obj_pricing_perHour + "/t" + obj_available);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option1);
		}

		Message msg = new Message();
		msg.what = 1001;
		handler.sendMessage(msg);

	}

	public static class WorkTheard extends Thread
	{

		@Override
		public void run()
		{
			LoadUserData = false;
			Cursor userinfo = mdb.GetLoginUser();
			for (userinfo.moveToFirst(); !userinfo.isAfterLast(); userinfo.moveToNext())
			{
				int openidColumn = userinfo.getColumnIndex("openid");
				int usernameColumn = userinfo.getColumnIndex("username");
				int tokenColumn = userinfo.getColumnIndex("token");
				int headimageColumn = userinfo.getColumnIndex("headimage");

				_OpenID = userinfo.getString(openidColumn);
				_UserName = userinfo.getString(usernameColumn);
				_Token = userinfo.getString(tokenColumn);
				try
				{
					InputStream _headimageIS = getImageStream(userinfo.getString(headimageColumn));
					_headimage = BitmapFactory.decodeStream(_headimageIS);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					_headimage = null;
				}

			}

			Message msg = new Message();
			msg.what = 520;
			handler.sendMessage(msg);
		}

	}

	public static InputStream getImageStream(String path) throws Exception
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			return conn.getInputStream();
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contextmian = this;
		mdb = new MethodDate(contextmian);

		Login.isLogin=true;

		loadingmove_m = (ImageView) findViewById(R.id.main_loadiconmove);
		loadingmove_cilck_m = (ImageView) findViewById(R.id.main_loadiconcliock);
		loading_ok_m = (ImageView) findViewById(R.id.main_load_ok);
		loading_text_ok = (TextView) findViewById(R.id.main_load_text_ok);
		hyperspaceJumpAnimation = AnimationUtils.loadAnimation(contextmian, R.anim.loading_animation);
		mEtSearch = (EditText) findViewById(R.id.et_search);
		mBtnClearSearchText = (Button) findViewById(R.id.btn_clear_search_text);
		main_parkname = (TextView) findViewById(R.id.main_prakname);
		main_fristprice = (TextView) findViewById(R.id.main_parkfirst);
		main_available = (TextView) findViewById(R.id.main_available);
		mLayoutClearSearchText = (LinearLayout) findViewById(R.id.layout_clear_search_text);

		mEtSearch.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				int textLength = mEtSearch.getText().length();
				if (textLength > 0)
				{
					mLayoutClearSearchText.setVisibility(View.VISIBLE);
				}
				else
				{
					mLayoutClearSearchText.setVisibility(View.GONE);
				}
			}
		});
		if (LoadUserData)
		{
			new WorkTheard().start();
		}
		mBtnClearSearchText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mEtSearch.setText("");
				mLayoutClearSearchText.setVisibility(View.GONE);
			}
		});
		mEtSearch.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					Toast.makeText(Main.this, mEtSearch.getText().toString().trim(), Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});

//		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener, new LBSAuthManagerListener()
//		{
//			@Override
//			public void onAuthResult(int status, String msg)
//			{
//				String str = null;
//				if (0 != status)
//				{
//					str = "key校验失败, " + msg;
//					Toast.makeText(Main.this, str, Toast.LENGTH_LONG).show();
//				}
//
//			}
//		});

		showdeatil_p = (LinearLayout) findViewById(R.id.showdeatil_p);
		main_bottom = (RelativeLayout) findViewById(R.id.main_bottom);

		main_NavBaiDu = (Button) findViewById(R.id.main_NavBaiDu);
		main_NavBaiDu.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub 104.070414,30.588753
				StartNavBD("false");
			}
		});
		main_nowbooking = (Button) findViewById(R.id.main_newbooking);
		main_nowbooking.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				new AlertDialog.Builder(contextmian).setTitle("预约提示").setMessage("确认预约该停车场车位？").setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						ShowToast("预约成功");
//						BNTTSPlayer.initPlayer();
//						BNTTSPlayer.playTTSText("预约成功", -1);
						StartNavBD("true");
					}
				}).setNegativeButton("取消", null).show();
			}
		});
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);

		requestLocButton = (ImageButton) findViewById(R.id.button1);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setBackgroundResource(R.drawable.map_icon_navigation_1);

		OnClickListener btnClickListener = new OnClickListener()
		{
			public void onClick(View v)
			{
				switch (mCurrentMode)
				{
					case NORMAL :
						// requestLocButton.setText("跟随");
						requestLocButton.setBackgroundResource(R.drawable.map_icon_navigation_2);
						mCurrentMode = LocationMode.FOLLOWING;
						mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
						break;
					case COMPASS :
						// requestLocButton.setText("普通");
						requestLocButton.setBackgroundResource(R.drawable.map_icon_navigation_1);
						mCurrentMode = LocationMode.NORMAL;
						mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
						mUiSettings.setOverlookingGesturesEnabled(false);
						break;
					case FOLLOWING :
						// requestLocButton.setText("罗盘");
						requestLocButton.setBackgroundResource(R.drawable.map_icon_navigation_3);
						mCurrentMode = LocationMode.COMPASS;
						mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
						break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mUiSettings = mBaiduMap.getUiSettings();
		mUiSettings.setOverlookingGesturesEnabled(false);

		mMapView.removeViewAt(1);
		mMapView.showZoomControls(false);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
		mBaiduMap.setOnMapStatusChangeListener(listener);

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener()
		{

			@Override
			public boolean onMarkerClick(Marker arg0)
			{
				if (oldMarker != null)
				{
					oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_p_blue));
				}
				String dataString = arg0.getTitle();
				String[] arrdata = dataString.split("/t");
				String _idString = arrdata[0];
				String _nameString = arrdata[1];
				String _firsteprice = arrdata[2];
				String _firstHoure = arrdata[3];
				String _PriceHoure = arrdata[4];
				String _available = arrdata[5];
				main_parkname.setText(_nameString);
				NavTitle = _nameString;
				main_fristprice.setText("首停  " + _firsteprice + "元/" + _firstHoure + "小时");
				main_available.setText(_available);
				showdeatil_p.setVisibility(View.VISIBLE);
				main_bottom.setVisibility(View.GONE);
				arg0.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_p_orange_press));
				oldMarker = arg0;
				mQJLat2 = arg0.getPosition().latitude;
				mQJLon2 = arg0.getPosition().longitude;
				return true;
			}
		});
		//Util.initImageLoader(this);

	}

	@Override
	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
		IsLogoWX = true;
		new WorkTheard().start();
	}

	private void StartNavBD(String Parkingnavigation)
	{
		// mQJLat2 = 30.588753;
		// mQJLon2 = 104.070414;

		launchNavigator2(mQJLon1, mQJLat1, mQJLon2, mQJLat2, Parkingnavigation, NavTitle);
	}
	private OnMapStatusChangeListener listener = new OnMapStatusChangeListener()
	{
		/** 手势操作地图，设置地图状态等操作导致地图状态开始改变。
		 * 
		 * @param status
		 *            地图状态改变开始时的地图状态 */
		public void onMapStatusChangeStart(MapStatus status)
		{
			mBaiduMap.clear();
			showdeatil_p.setVisibility(View.GONE);
			main_bottom.setVisibility(View.VISIBLE);
		}
		/** 地图状态变化中
		 * 
		 * @param status
		 *            当前地图状态 */
		public void onMapStatusChange(MapStatus status)
		{
		}
		/** 地图状态改变结束
		 * 
		 * @param status
		 *            地图状态改变结束后的地图状态 */
		public void onMapStatusChangeFinish(MapStatus status)
		{
			centerLat = status.target.latitude;
			centerLng = status.target.longitude;

			loading_ok_m.setVisibility(View.GONE);
			loading_text_ok.setVisibility(View.GONE);
			loadingmove_m.setVisibility(View.VISIBLE);
			loadingmove_cilck_m.setVisibility(View.VISIBLE);

			loadingmove_m.startAnimation(hyperspaceJumpAnimation);

			//new RequestTask().execute();
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{

		requestLocButton.setBackgroundResource(R.drawable.map_icon_navigation_1);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
		mUiSettings.setOverlookingGesturesEnabled(false);

		showdeatil_p.setVisibility(View.GONE);
		main_bottom.setVisibility(View.VISIBLE);
		return super.onTouchEvent(event);
	}
	/** 定位SDK监听函数 */
	public class MyLocationListenner implements BDLocationListener
	{

		@Override
		public void onReceiveLocation(BDLocation location)
		{
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc)
			{
				isFirstLoc = false;
				LocationCity = location.getCity();
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				mQJLat1 = location.getLatitude();
				mQJLon1 = location.getLongitude();
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}

		}

		public void onReceivePoi(BDLocation poiLocation)
		{
		}
	}

	@Override
	protected void onPause()
	{
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		LoadUserData=true;
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	
	private void ShowToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	/** 指定导航起终点启动GPS导航.起终点可为多种类型坐标系的地理坐标。
	 * 前置条件：导航引擎初始化成功 */
	private void launchNavigator2(double startJD, double startWD, double endJD, double endWD, final String Parkingnavigation, String Title)
	{
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标//104.066515,30.58346
//		BNaviPoint startPoint = new BNaviPoint(104.066515, 30.58346, "当前位置", BNaviPoint.CoordinateType.BD09_MC);
//		BNaviPoint endPoint = new BNaviPoint(endJD, endWD, Title, BNaviPoint.CoordinateType.BD09_MC);
//		BaiduNaviManager.getInstance().launchNavigator(this, startPoint, // 起点（可指定坐标系）
//				endPoint, // 终点（可指定坐标系）
//				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
//				false, // 真实导航
//				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
//				new OnStartNavigationListener()
//				{ // 跳转监听
//
//					@Override
//					public void onJumpToNavigator(Bundle configParams)
//					{
//						Intent intent = new Intent(Main.this, BNavigatorActivity.class);
//						intent.putExtras(configParams);
//						intent.putExtra("Parkingnavigation", Parkingnavigation);
//						startActivity(intent);
//					}
//
//					@Override
//					public void onJumpToDownloader()
//					{
//					}
//				});
	}
	
	private static Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 1001)
			{
				loading_text_ok.setVisibility(View.VISIBLE);
				loading_ok_m.setVisibility(View.VISIBLE);
				loadingmove_m.setVisibility(View.GONE);
				loadingmove_cilck_m.setVisibility(View.GONE);
				loadingmove_m.clearAnimation();
			}
			else if (msg.what == 520)
			{
				if (!_OpenID.equals(""))
				{

				}
			}

		}
	};
}
