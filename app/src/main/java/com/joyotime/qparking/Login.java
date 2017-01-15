package com.joyotime.qparking;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ansai.uparking.R;
import com.ansai.uparking.data.UserInfoManager;
import com.ansai.uparking.entity.bean.UserInfo;
import com.ansai.uparking.utils.StringUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.CrashHandler;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.view.PubileUI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity {
    private Button login_btn_signin;
    private Button login_btn_getyanzhengma;
    private ViewFlipper allFlipper;
    static MethodDate mdb;
    Context contextmian;
    static public Boolean LoadUserData = false;
    static public Boolean isLogin = false;
    static public Boolean isNetLogin = false;

    MethodDate DB = new MethodDate(this);

    private Boolean isauthcode = false;
    private static String _token_newString = null;
    // RequestTask requstData = null;
    private static String _openId = null;
    private Boolean IsloadShare = false;

    private String _cellphone = null;
    private String _code = null;

    private static int nowtime = 60;
    private static Timer getTimer;
    private Boolean isSuccCode = false;

    private EditText login_et_phonenumber;
    private TextView login_deal;
    private EditText login_et_yanzhengma;

    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private boolean mIsEngineInitSuccess = false;

    public static String m_Latitude = "";
    public static String m_Longitude = "";
    public static String m_cityname = "";
    public static String m_citycode = "";
    public static String m_addressde = "";
    public static int AppKey = 0;
    private Boolean iswhileLoad = false;
    private Context thisContext;
    private static Dialog progressDialog;

    private int loadcount = 0;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                String qqString = s;
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                String wifiString = "error";
            }
        }
    }

    private SDKReceiver mReceiver;
    private ConnectionChangeReceiver myReceiver;

//	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
//		public void engineInitSuccess() {
//			mIsEngineInitSuccess = true;
//		}
//
//		public void engineInitStart() {
//		}
//
//		public void engineInitFail() {
//		}
//	};

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public class TimerTaskTest extends java.util.TimerTask {

        @Override
        public void run() {
            if (nowtime > 1) {
                Message msg = new Message();
                msg.what = 600;
                statichandler.sendMessage(msg);
                nowtime--;
            } else {
                nowtime = 60;
                Message msg = new Message();
                msg.what = 601;
                statichandler.sendMessage(msg);

            }
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 1) {

                if (LoadUserData) {
                    startActivity(new Intent(Login.this, LockMain.class));
                    iswhileLoad = true;
                    handler.postDelayed(task, 5 * 1000);

                } else {
                    startActivityForResult(new Intent(Login.this, FlashApp.class), 1);
                    // allFlipper.setDisplayedChild(1);
                }
            }

        }
    };
    private Handler statichandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 600) {
                login_btn_getyanzhengma.setText(nowtime + "秒后重新获取");
            } else if (msg.what == 601) {
                getTimer.cancel();
                login_btn_getyanzhengma.setTextColor(Color.BLUE);

                login_btn_getyanzhengma.setText("重新获取验证码");
                login_btn_getyanzhengma.setClickable(true);

            }

        }
    };

    private void ShowToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        thisContext = this;
        progressDialog = PubileUI.MsgDialog(this, "正在登录...");

//		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener,
// new LBSAuthManagerListener() {
//			@Override
//			public void onAuthResult(int status, String msg) {
//				String str = null;
//				if (0 != status) {
//					str = "网络已断开，请检查网络";// "key校验失败, " + msg;
//					Toast.makeText(Login.this, str, Toast.LENGTH_LONG).show();
//				}
//
//			}
//		});
        try {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            myReceiver = new ConnectionChangeReceiver();
            this.registerReceiver(myReceiver, filter);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        contextmian = this;
        mdb = new MethodDate(contextmian);
        login_deal = (TextView) findViewById(R.id.login_deal);
        login_deal.setText(Html.fromHtml("<a href='" + AppSetting.clause_url + "'>《用户使用协议》 </a>"));
        login_deal.setMovementMethod(LinkMovementMethod.getInstance());
        login_btn_getyanzhengma = (Button) findViewById(R.id.login_btn_getyanzhengma);
        login_btn_getyanzhengma.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ConnectionChangeReceiver.IsNetWork) {
                    ShowToast("网络已断开，请检查网络");
                    return;
                }
                // TODO Auto-generated method stub
                String telRegex = "[1][34578]\\d{9}";
                // TODO Auto-generated method stub
                String phonenumString = login_et_phonenumber.getText().toString();
                if (phonenumString.equals("")) {
                    ShowToast("手机号码不能为空");
                    return;
                }
                if (!phonenumString.matches(telRegex)) {
                    ShowToast("手机号码格式不正确");
                    return;
                }
                isauthcode = true;
                isSuccCode = false;
                RequestTask ccRequestTask = new RequestTask();
                ccRequestTask.execute();
                getTimer = new Timer();
                getTimer.schedule(new TimerTaskTest(), 1000, 1000);
                login_btn_getyanzhengma.setClickable(false);
                login_btn_getyanzhengma.setTextColor(ContextCompat.getColor(Login.this, R.color
                        .forbidden_c));

            }
        });
        login_et_phonenumber = (EditText) findViewById(R.id.login_et_phonenumber);
        login_et_yanzhengma = (EditText) findViewById(R.id.login_et_yanzhengma);
        allFlipper = (ViewFlipper) findViewById(R.id.allFlipper);
        login_btn_signin = (Button) findViewById(R.id.login_btn_signin);
        login_btn_signin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ConnectionChangeReceiver.IsNetWork) {
                    ShowToast("网络已断开，请检查网络");
                    return;
                }
                if (!isSuccCode) {
                    ShowToast("请重新获取验证码");
                    return;
                }
                RequestTask ccRequestTask = new RequestTask();
                ccRequestTask.execute();
                progressDialog.show();
            }
        });

        login_et_phonenumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // s:变化后的所有字符

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                // s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                // S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                if (login_et_yanzhengma.getText().toString().length() == 6 &&
                        login_et_phonenumber.getText().toString().length() == 11) {
                    login_btn_signin.setBackground(getResources().getDrawable(R.drawable
                            .btn_signin));
                    login_btn_signin.setClickable(true);
                    InputMethodManager m = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    login_btn_signin.setBackground(getResources().getDrawable(R.drawable
                            .btn_forbidden));
                    login_btn_signin.setClickable(false);
                }
            }

        });
        login_et_yanzhengma.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // s:变化后的所有字符

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                // s:变化前的所有字符； start:字符开始的位置； count:变化前的总字节数；after:变化后的字节数

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                // S：变化后的所有字符；start：字符起始的位置；before: 变化之前的总字节数；count:变化后的字节数
                if (login_et_yanzhengma.getText().toString().length() == 6 &&
                        login_et_phonenumber.getText().toString().length() == 11) {
                    login_btn_signin.setBackground(getResources().getDrawable(R.drawable
                            .btn_signin));
                    login_btn_signin.setClickable(true);
                    InputMethodManager m = (InputMethodManager) getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    login_btn_signin.setBackground(getResources().getDrawable(R.drawable
                            .btn_forbidden));
                    login_btn_signin.setClickable(false);
                }
            }

        });

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context
                .BLUETOOTH_SERVICE);
        LockMain.mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (LockMain.mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LockMain.mBluetoothAdapter.enable();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                handler.sendEmptyMessage(1); // 给UI主线程发送消息

            }
        }, 2000); // 启动等待3秒钟
        new Login_WorkTheard().start();
        allFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
        allFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
        new RequestTaskAppData().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            allFlipper.setDisplayedChild(1);
        }
    }

    public class RequestTaskAppData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = null;
            switch (AppKey) {
                case 0:
                    result = RequestAppV();
                    break;

                default:
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            switch (AppKey) {
                case 0:
                    JosnAppv(result);
                    break;

                default:
                    break;
            }
        }

    }

    private static String RequestAppV() {
        HttpClient httpClient = new DefaultHttpClient();

        String strhardwarecode = mdb.GetAllVersion();
        String url = AppSetting.URL_STRING + "version?model=android&hardCode=" + strhardwarecode;
        // +
        // AppSetting.mHardWare;
        // String url = AppSetting.URL_STRING + "version/android/2/2";

        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", "application/json");
        StringBuilder builder = null;

        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String s = null;
                for (s = reader.readLine(); s != null; s = reader.readLine()) {
                    builder.append(s);
                }

                return builder.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void JosnAppv(String result) {
        if (result == null) {
            return;
        }
        JSONArray object = null;
        try {
            object = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < object.length(); i++) {
            JSONObject jsObject = null;
            try {
                jsObject = object.getJSONObject(i);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (i == object.length() - 1) {
                AppSetting.AppVCode = Integer.parseInt(jsObject.optString("androidAppCode"));
                AppSetting.AppVName = jsObject.optString("androidAppName");
                AppSetting.Url_Android = AppSetting.URL_STRING_Down + jsObject.optString("android");
            } else {

                AppSetting.nowServiceSoftVersion = Integer.parseInt(jsObject.optString
                        ("softCode").toString());
                int hardCode = Integer.parseInt(jsObject.optString("hardCode").toString());
                AppSetting.Url_hardware = AppSetting.URL_STRING_Down + jsObject.optString
                        ("hardLocation");

                if (AppSetting.nowServiceSoftVersion > mdb.GetOneVersion(hardCode)) {
                    DownLoadFiles("" + hardCode, "" + AppSetting.nowServiceSoftVersion,
                            AppSetting.Url_hardware);
                }
            }
            // DB.InstarVersion(hardware, "0", LockMain._OpenID, sn);
            // ShowToast("添加成功");

        }

    }

    private class RequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            /** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
            try {
                if (isauthcode) {
                    return RequestCodeData(login_et_phonenumber.getText().toString());
                } else {

                    if (_token_newString == null || _token_newString.equals("")) {
                        _cellphone = login_et_phonenumber.getText().toString();
                        return RequestData(login_et_yanzhengma.getText().toString());
                    } else if (!IsloadShare) {
                        return RequestData(_token_newString);
                    } else if (IsloadShare) {
                        return RequestData(_token_newString);
                    } else {
                        return null;
                    }
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        /**
         * onPostExecute方法主要是主线程中的数据更新。
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
                try {
                    JSONAnalysis(result);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (result == null) {
                progressDialog.hide();
                progressDialog.dismiss();
                Toast.makeText(Login.this, "操作失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String RequestCodeData(String cellphone) {
        HttpClient httpClient = new DefaultHttpClient();

        String url = AppSetting.URL_STRING + "sms/code?cellphone=" + cellphone;

        HttpGet get = new HttpGet(url);
        get.setHeader("Content-type", "application/json");
        StringBuilder builder = null;

        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream inputStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                builder = new StringBuilder();
                String s = null;
                for (s = reader.readLine(); s != null; s = reader.readLine()) {
                    builder.append(s);
                }

                return builder.toString();
            } else if (response.getStatusLine().getStatusCode() == 401) {
                return "401";
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void JSONAnalysis(String result) throws JSONException {
        if (isauthcode) {
            isauthcode = false;
            if (result == null || result.equals("") || result.equals("401")) {
                isSuccCode = false;
                Toast.makeText(Login.this, "验证码发送失败", Toast.LENGTH_LONG).show();
            } else {
                isSuccCode = true;
                Toast.makeText(Login.this, "验证码已发送", Toast.LENGTH_LONG).show();
            }
        } else {

            if ((_token_newString == null || _token_newString.equals("")) && !iswhileLoad) {
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String _id = object.optString("_id");
                String type = object.optString("type");
                String name = object.optString("name");
                String avatar = object.optString("avatar");
                String token = object.optString("token");
                String balance = object.optString("balance");

                String gender = object.optString("gender");
                String province = object.optString("province");
                String city = object.optString("city");

                _token_newString = token;

                String cellphone = "";
                try {
                    cellphone = object.optString("cellphone");
                } catch (Exception e) {
                    // TODO: handle exception
                    cellphone = "";
                }
                _openId = cellphone;

                UserInfo userInfo = new UserInfo();
                userInfo._id =_id;
                userInfo.cellphone = cellphone;
                userInfo.balance =balance;
                userInfo.token = token;
                userInfo.avatar = avatar;
                userInfo.city = city;
                userInfo.gender = StringUtil.parseInt(gender);
                userInfo.name = name;
                userInfo.province = province;
                UserInfoManager.getInstance().saveUser(userInfo);


                if (!DB.ValidationUser(_openId)) {
                    DB.InstarUserDataLocal(_openId, name, token, avatar, cellphone, gender.equals
                            ("1") ? "男" : "女", province, city);
                } else {
                    DB.LogoUserDataLocal(_openId, name, token, avatar);

                }
                new RequestTask().execute();
            } else if (!IsloadShare) {
                JSONArray object = null;
                try {
                    object = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < object.length(); i++) {
                    JSONObject jsObject = object.getJSONObject(i);

                    String _id = jsObject.optString("_id");
                    String sn = jsObject.optString("sn");
                    String bluetooth = jsObject.optString("bluetooth");
                    String bluetoothName = jsObject.optString("bluetoothName");
                    String key = jsObject.optString("key");
                    String hardware = jsObject.optString("hardware");

                    String locktoken = jsObject.optString("token");

                    String Botocellphone = "";

                    String lockLng = jsObject.getJSONObject("location").getJSONArray
                            ("coordinates").optString(0);
                    String lockLat = jsObject.getJSONObject("location").getJSONArray
                            ("coordinates").optString(1);
                    String parkingAddress = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("address"));
                    String cityCode = jsObject.getJSONObject("parkingLot").optString("cityCode");
                    String cityName = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("cityName"));
                    String parkingName = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("name"));
                    String lockAddress = UnicodeToString(jsObject.optString("address"));

                    if (!DB.ValidationBlue(bluetooth, "1")) {
                        if (DB.InstarBlueDBLocal(_id, bluetoothName, bluetooth, "", key, sn,
                                locktoken, _openId, "1", i == 0 ? "1" : "0", lockLng, lockLat,
                                lockAddress, cityName,
                                parkingName, parkingAddress, cityCode, Botocellphone)) {
                            DB.InstarVersion(hardware, "0", LockMain._OpenID, sn);
                            // ShowToast("添加成功");
                        }
                    } else {
                        DB.UpdateBlueDBLocal(bluetoothName, "", key, sn, locktoken, lockLng,
                                lockLat, lockAddress, cityName, parkingName, parkingAddress,
                                cityCode, Botocellphone);
                    }
                }
                IsloadShare = true;
                new RequestTask().execute();

            } else if (IsloadShare) {
                JSONArray object = null;
                try {
                    object = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int count = DB.GetTableCode("BlueData", _openId, null);
                mdb.DetetelBlueShareToMe();
                for (int i = 0; i < object.length(); i++) {
                    JSONObject jsObject = object.getJSONObject(i);

                    String _id = jsObject.optString("_id");
                    String sn = jsObject.getJSONObject("lock").optString("sn");
                    String bluetooth = jsObject.getJSONObject("lock").optString("bluetooth");
                    String bluetoothName = jsObject.getJSONObject("lock").optString
                            ("bluetoothName");
                    String key = jsObject.getJSONObject("lock").optString("key");
                    String locktoken = jsObject.getJSONObject("lock").optString("token");
                    String Botocellphone = jsObject.getJSONObject("owner").optString("cellphone");
                    String lockLng = jsObject.getJSONObject("lock").getJSONObject("location")
                            .getJSONArray("coordinates").optString(0);
                    String lockLat = jsObject.getJSONObject("lock").getJSONObject("location")
                            .getJSONArray("coordinates").optString(1);
                    String parkingAddress = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("address"));
                    String cityCode = jsObject.getJSONObject("parkingLot").optString("cityCode");
                    String cityName = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("cityName"));
                    String parkingName = UnicodeToString(jsObject.getJSONObject("parkingLot")
                            .optString("name"));
                    String lockAddress = UnicodeToString(jsObject.optString("address"));

                    if (!DB.ValidationBlue(bluetooth, "0")) {
                        if (DB.InstarBlueDBLocal(_id, bluetoothName, bluetooth, "", key, sn,
                                locktoken, _openId, "0", count == 0 ? (i == 0 ? "1" : "0") : "0",
                                lockLng, lockLat,
                                lockAddress, cityName, parkingName, parkingAddress, cityCode,
                                Botocellphone)) {
                            // ShowToast("添加成功");
                        }
                    } else {
                        DB.UpdateBlueDBLocal(bluetoothName, "", key, sn, locktoken, lockLng,
                                lockLat, lockAddress, cityName, parkingName, parkingAddress,
                                cityCode, Botocellphone);

                    }
                }

                if (iswhileLoad) {
                    handler.postDelayed(task, 5 * 1000);
                } else {
                    iswhileLoad = true;
                    handler.postDelayed(task, 5 * 1000);

                    progressDialog.hide();
                    progressDialog.dismiss();
                    startActivity(new Intent(Login.this, LockMain.class));
                    new Login_WorkTheard().start();
                    Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_LONG).show();
                }

            }
        }
    }





    // private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            if (!ConnectionChangeReceiver.IsNetWork) {
                if (loadcount < 1) {
                    loadcount++;
                    ShowToast("网络已断开，请检查网络");
                }
                handler.postDelayed(task, 5 * 1000);
            } else {
                if (loadcount > 0) {

                    IsloadShare = true;
                } else {
                    loadcount++;
                    IsloadShare = false;
                }

                new RequestTask().execute();
            }

        }
    };

    public String RequestData(String code) throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        if (_token_newString == null || _token_newString.equals("")) {
            _code = code;
            JSONObject param = new JSONObject();
            try {
                param.put("cellphone", _cellphone);
                param.put("code", _code);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            HttpPost post = new HttpPost(AppSetting.URL_STRING + "signin/cellphone");

            StringEntity postingString = new StringEntity(param.toString());// json传递
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            response = httpClient.execute(post);
        } else if (!IsloadShare) {
            HttpGet get = new HttpGet(AppSetting.URL_STRING + "lockers");
            get.addHeader("Authorization", _token_newString);
            get.setHeader("Content-type", "application/json");
            response = httpClient.execute(get);
        } else if (IsloadShare) {
            HttpGet get = new HttpGet(AppSetting.URL_STRING + "/lockshare/sharedToMe/" + _openId);
            get.addHeader("Authorization", _token_newString);
            get.setHeader("Content-type", "application/json");
            response = httpClient.execute(get);
        }

        if (300 > response.getStatusLine().getStatusCode() && response.getStatusLine()
                .getStatusCode() >= 200) {
            try {
                HttpEntity qq = response.getEntity();
                String content = EntityUtils.toString(qq);

                return content;
            } catch (Exception e) {
                // TODO: handle exception
                String wwwww = e.toString();
                return null;
            }

        } else {
            return null;
        }
    }

    public static class Login_WorkTheard extends Thread {

        @Override
        public void run() {
            LockMain._headimage = null;
            LockMain._UserName = null;
            LoadUserData = false;
            final Cursor userinfo = mdb.GetLoginUser();
            int count = userinfo.getCount();
            if (count > 0) {
                LoadUserData = true;
            }
            LockMain.lockmain_LoadUserData = false;
            for (userinfo.moveToFirst(); !userinfo.isAfterLast(); userinfo.moveToNext()) {
                int openidColumn = userinfo.getColumnIndex("openid");
                int usernameColumn = userinfo.getColumnIndex("username");
                int tokenColumn = userinfo.getColumnIndex("token");
                final int headimageColumn = userinfo.getColumnIndex("headimage");
                int phonenumColumn = userinfo.getColumnIndex("phonenum");

                int provinceColumn = userinfo.getColumnIndex("province");
                int cityColumn = userinfo.getColumnIndex("city");

                LockMain._OpenID = userinfo.getString(openidColumn);
                LockMain._UserName = userinfo.getString(usernameColumn);
                LockMain._AddressString = userinfo.getString(provinceColumn) + " " + userinfo
                        .getString(cityColumn);
                LockMain._Token = userinfo.getString(tokenColumn);
                _token_newString = LockMain._Token;
                _openId = LockMain._OpenID;
                LockMain._phonenum = userinfo.getString(phonenumColumn);
                Looper.prepare();
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        try {
                            InputStream _headimageIS = getImageStream(userinfo.getString
                                    (headimageColumn));
                            LockMain._headimage = BitmapFactory.decodeStream(_headimageIS);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            LockMain._headimage = null;
                        }
                        isNetLogin = true;
                        Message msg = new Message();
                        msg.what = 520;
                        LockMain.handler.sendMessage(msg);
                        // new LockMain.WorkTheard().start();
                        // Message msg1 = new Message();
                        // msg1.what = 3008;
                        // UserSetting.handler.sendMessage(msg1);
                    }

                }, 500);
                Looper.loop();

            }
        }

    }

    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            m_Latitude = arg0.getLatitude() + "";
            m_Longitude = arg0.getLongitude() + "";
            m_citycode = arg0.getCityCode();
            m_cityname = arg0.getCity();
            m_addressde = arg0.getAddrStr();
            mLocClient.stop();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public void DownLoadFiles(final String Finder, final String SoftWare, final String
            UrlHardware) {
        if (ConnectionChangeReceiver.IsNetWork) {
            DownLoadFile(Finder, SoftWare, "PL_imageA.bin", UrlHardware);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DownLoadFile(Finder, SoftWare, "PL_imageB.bin", UrlHardware);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, 2000);
        } else {
            ShowToast("网络已断开，请检查网络");
        }
    }

    public void DownLoadFile(String Finder, String SoftWare, String FileName, String UrlHardware) {
        StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory() + "/")
                .append("QParking/" + Finder + "/");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
            // 创建文件夹
        }
        // 获取文件全名
        sb.append(FileName);
        file = new File(sb.toString());
        if (file.exists()) {
            Boolean sdsd = file.delete();
            if (!sdsd) {
                file.exists();
            }
        }

        DownloadManager downloadManager = (DownloadManager) contextmian.getSystemService
                (DOWNLOAD_SERVICE);
        String apkUrl = UrlHardware + FileName;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDestinationInExternalPublicDir("QParking/" + Finder, FileName);
        request.setVisibleInDownloadsUi(false);
        long downloadId = downloadManager.enqueue(request);
        if (FileName.equals("PL_imageB.bin")) {
            mdb.UpdateVersion(SoftWare, Finder);
        }

    }

    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub

        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (isLogin) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(mReceiver);
        unregisterReceiver(myReceiver);

        super.onDestroy();
    }

}
