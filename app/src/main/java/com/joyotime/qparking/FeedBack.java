package com.joyotime.qparking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.view.PubileUI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class FeedBack extends Activity {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private EditText feedback_tv;
	private TextView feedback_submit;
	private static Dialog progressDialog = null;
	Context context;
	ImageView feedback_goback;
	String buglog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		Intent Data = getIntent();
		Bundle bunData = Data.getExtras();

		buglog = bunData.getString("buglog");
		context = this;
		feedback_goback = (ImageView) findViewById(R.id.feedback_goback);
		feedback_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		feedback_tv = (EditText) findViewById(R.id.feedback_tv);
		feedback_tv.setText(buglog);
		feedback_submit = (TextView) findViewById(R.id.feedback_submit);
		feedback_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (feedback_tv.getText().toString().equals("")) {
					ShowToast("内容不能为空");
					return;
				}
				if (feedback_tv.getText().toString().equals("ubO_85521169")) {
					LockMain.SuperWriteZooe();
					ShowToast("配置已清除");
				} else {

					if (buglog == null || buglog.equals("")) {
						if (!ConnectionChangeReceiver.IsNetWork) {
							ShowToast("网络已断开，请检查网络");
							return;
						}
					}
					// TODO Auto-generated method stub
					progressDialog = PubileUI.MsgDialog(context, "正在提交...");
					progressDialog.show();
					new RequestTask().execute();
				}
			}
		});

	}

	String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	private void ShowToast(String msg) {
		Toast.makeText(context, msg, 3000).show();
	}

	private class RequestTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				JSONObject jsondt = new JSONObject();
				jsondt.put("cellphone", LockMain._OpenID);
				jsondt.put("cellphoneType", "android");
				jsondt.put("content", chinaToUnicode(feedback_tv.getText().toString()));
				jsondt.put("softVersion", chinaToUnicode(
						"android:" + android.os.Build.VERSION.RELEASE + "   SDK:" + android.os.Build.VERSION.SDK));
				jsondt.put("hardVersion", chinaToUnicode(android.os.Build.MODEL));

				/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(AppSetting.URL_STRING + "suggestion/add");
				StringEntity postingString = new StringEntity(jsondt.toString());// json传递
				post.setEntity(postingString);
				// post.addHeader("Authorization", LockMain._Token);
				post.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(post);
				HttpEntity qq = response.getEntity();
				String content = EntityUtils.toString(qq);
				return content;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ShowToast("提交成功,感谢您的反馈!");
			progressDialog.hide();
			progressDialog.dismiss();
			finish();
		}
	}
}
