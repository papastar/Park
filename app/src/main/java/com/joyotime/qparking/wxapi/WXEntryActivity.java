package com.joyotime.qparking.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.LockMain;
import com.joyotime.qparking.Login;
import com.joyotime.qparking.Login.Login_WorkTheard;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private static final String wx_AppId = "wxc89e170a4b4b49c0";
	private IWXAPI wxapi;
	MethodDate DB = new MethodDate(this);

	private Boolean iscode = true;
	RequestTask requstData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_null);

		wxapi = WXAPIFactory.createWXAPI(this, wx_AppId, false);
		wxapi.registerApp(wx_AppId);
		wxapi.handleIntent(getIntent(), this);

		if (LockMain.lockmain_IsLogoWX && iscode) {
			iscode = false;
			LockMain.lockmain_IsLogoWX = false;
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test_qparking";
			wxapi.sendReq(req);

		}

		finish();
	}

	private class RequestTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			try {
				return RequestData(LockMain.lockmain_wx_codeString);
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

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				// 如果获取的result数据不为空，那么对其进行JSON解析。并显示在手机屏幕上。
				try {
					if (JSONAnalysis(result)) {
						new Login_WorkTheard().start();

						// Message Lmsg = new Message();
						// Lmsg.what = 520;
						// LockMain.handler.sendMessage(msg);
						Toast.makeText(WXEntryActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(WXEntryActivity.this, "绑定失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (result == null) {
				Toast.makeText(WXEntryActivity.this, "信息拉取失败", Toast.LENGTH_LONG).show();
			}
		}
	}

	public Boolean JSONAnalysis(String result) throws JSONException {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String name = object.optString("name");
		String avatar = object.optString("avatar");
		String gender = object.optString("gender");
		String province = object.optString("province");
		String city = object.optString("city");

		String cellphone = "";
		try {
			cellphone = object.optString("cellphone");
		} catch (Exception e) {
			// TODO: handle exception
			cellphone = "";
		}

		return DB.UpdateUserInfo(LockMain._OpenID, name, avatar, gender.equals("1") ? "男" : "女", province, city);

	}

	public String RequestData(String Code) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;

		JSONObject param = new JSONObject();
		try {
			param.put("code", Code);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpPost post = new HttpPost(AppSetting.URL_STRING + "user/wechat");

		StringEntity postingString = new StringEntity(param.toString());// json传递
		post.setEntity(postingString);
		post.addHeader("Authorization", LockMain._Token);
		post.setHeader("Content-type", "application/json");
		response = httpClient.execute(post);

		if (300 > response.getStatusLine().getStatusCode() && response.getStatusLine().getStatusCode() >= 200) {
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

	@Override
	protected void onNewIntent(Intent intent) {
		iscode = false;
		super.onNewIntent(intent);

		setIntent(intent);
		wxapi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			// goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			// goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		Login.LoadUserData = true;
		LockMain.lockmain_IsLogoWX = true;
		String result = "";
		iscode = false;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// startActivity(new Intent(WXEntryActivity.this,
			// LockMain.class));
			String code = ((SendAuth.Resp) resp).code;
			result = code;
			LockMain.lockmain_wx_codeString = code;
			requstData = new RequestTask();
			requstData.execute();
			finish();

			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "绑定取消";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "errcode_deny";
			break;
		default:
			result = "errcode_unknown";
			break;
		}

	}

}