package com.joyotime.qparking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BindingPhone extends Activity {

	private EditText binding_et_phonenumber;
	private EditText binding_et_yanzhengma;
	private static Button binding_btn_getyanzhengma;
	private static Timer getTimer;
	private static int nowtime = 60;
	private String messageYZ = "";
	TextView qparking_submit;
	MethodDate mdb = new MethodDate(this);
	ImageView bindsetting_goback;
	private String _cellphone = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bindingphone);
		binding_et_phonenumber = (EditText) findViewById(R.id.binding_et_phonenumber);
		binding_et_yanzhengma = (EditText) findViewById(R.id.binding_et_yanzhengma);
		binding_btn_getyanzhengma = (Button) findViewById(R.id.binding_btn_getyanzhengma);
		binding_btn_getyanzhengma.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				if (!ConnectionChangeReceiver.IsNetWork) {
					ShowToast("网络已断开，请检查网络");
					return;
				}
				String telRegex = "[1][358]\\d{9}";
				// TODO Auto-generated method stub
				String phonenumString = binding_et_phonenumber.getText().toString();
				if (phonenumString.equals("")) {
					ShowToast("手机号码不能为空");
					return;
				}
				if (!phonenumString.matches(telRegex)) {
					ShowToast("手机号码格式不正确");
					return;
				}
				RequestTask ccRequestTask = new RequestTask();
				ccRequestTask.execute();
				getTimer = new Timer();
				getTimer.schedule(new TimerTaskTest(), 1000, 1000);
				binding_btn_getyanzhengma.setClickable(false);
				// binding_btn_getyanzhengma.setBackgroundColor(com.joyotime.qparking.R.color.yanzma_banc);
			}
		});
		bindsetting_goback = (ImageView) findViewById(R.id.bindsetting_goback);
		bindsetting_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		qparking_submit = (TextView) findViewById(R.id.qparking_submit);
		qparking_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String yanzhengma = binding_et_yanzhengma.getText().toString();
				if (yanzhengma.equals("")) {
					ShowToast("验证码不能为空");
					return;
				}
				if (!messageYZ.equals("ok")) {
					ShowToast("验证码无效，请重新获取");
					return;
				}
				new RequestTaskP().execute();
			}
		});
	}

	private class RequestTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			return RequestData(binding_et_phonenumber.getText().toString());
		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(BindingPhone.this, "获取失败", Toast.LENGTH_LONG).show();
			} else if (result != null && !result.equals("401")) {
				// 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
				try {
					JSONAnalysis(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private class RequestTaskP extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			return PatchData(binding_et_phonenumber.getText().toString(), binding_et_yanzhengma.getText().toString());
		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(BindingPhone.this, "绑定失败", Toast.LENGTH_LONG).show();
			} else if (result != null && !result.equals("401")) {
				// 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
				try {
					JSONObject object = null;
					try {
						object = new JSONObject(result);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String normal = object.getString("type");
					if (normal != null && normal.equals("normal")) {
						if (mdb.UpdatePhoneNum(object.getString("openId"), _cellphone)) {
							LockMain._phonenum = _cellphone;
							Toast.makeText(BindingPhone.this, "绑定成功", Toast.LENGTH_LONG).show();
							finish();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(BindingPhone.this, "绑定失败", Toast.LENGTH_LONG).show();
				}
			}

		}
	}

	public void JSONAnalysis(String result) throws JSONException {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		messageYZ = "ok";

	}

	public String PatchData(String cellphone, String code) {
		_cellphone = cellphone;
		String json = "{\"cellphone\":\"" + cellphone + "\",\"code\":\"" + code + "\"}";
		HttpClient httpClient = getNewHttpClient();

		String url = AppSetting.URL_STRING + "user";

		HttpPost put = new HttpPost(url);
		StringEntity postingString = null;
		try {
			postingString = new StringEntity(json);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // json传递
		put.setEntity(postingString);
		put.setHeader("Content-type", "application/json");
		put.addHeader("Authorization", LockMain._Token);
		StringBuilder builder = null;

		try {
			HttpResponse response = httpClient.execute(put);
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

	public String RequestData(String cellphone) {
		HttpClient httpClient = getNewHttpClient();

		String url = AppSetting.URL_STRING + "sms/code?cellphone=" + cellphone;

		HttpGet get = new HttpGet(url);
		get.setHeader("Content-type", "application/json");
		get.addHeader("Authorization", LockMain._Token);
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

	// 这是设置的请求时的超时时间常量
	private static final int SET_CONNECTION_TIMEOUT = 20 * 1000;
	private static final int SET_SOCKET_TIMEOUT = 20 * 1000;

	private static HttpClient getNewHttpClient() { // --->wj
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			HttpClient client = new DefaultHttpClient(ccm, params);

			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SET_SOCKET_TIMEOUT);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, SET_CONNECTION_TIMEOUT);
			return client;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public class TimerTaskTest extends java.util.TimerTask {

		@Override
		public void run() {
			if (nowtime > 1) {
				Message msg = new Message();
				msg.what = 600;
				handler.sendMessage(msg);
				nowtime--;
			} else {
				nowtime = 60;
				Message msg = new Message();
				msg.what = 601;
				handler.sendMessage(msg);

			}
		}

	}

	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 600) {
				binding_btn_getyanzhengma.setText(nowtime + "秒后重新获取");
			} else if (msg.what == 601) {
				getTimer.cancel();
				binding_btn_getyanzhengma.setText("获取验证码");
				binding_btn_getyanzhengma.setClickable(true);
				// binding_btn_getyanzhengma.setBackgroundColor(R.drawable.btn_lockopen);
			}
		}
	};

}
