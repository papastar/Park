package com.joyotime.qparking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.view.CheckSwitchButton;
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

import java.io.IOException;

public class AddShareLock extends Activity {
	private String lockid = null;
	private String guest = null;
	private String day = "1";
	private TextView addsharelock_et_phonenumber;
	private TextView addsharelock_submit;
	private CheckSwitchButton mCheckSwithcButton;
	private ImageView addsharelock_goback;
	private Boolean GetShareData = false;
	MethodDate DB = new MethodDate(this);
	private ImageView addsharelock_iv_book;
	private static Dialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addsharelock);
		progressDialog = PubileUI.MsgDialog(this, " 正在提交...");
		Intent intent = getIntent();
		Bundle buldt = intent.getExtras();
		lockid = buldt.getString("lockid");
		mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mCheckSwithcButton);
		addsharelock_et_phonenumber = (TextView) findViewById(R.id.addsharelock_et_phonenumber);
		addsharelock_goback = (ImageView) findViewById(R.id.addsharelock_goback);
		addsharelock_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		addsharelock_iv_book = (ImageView) findViewById(R.id.addsharelock_iv_book);
		addsharelock_iv_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				AddShareLock.this.startActivityForResult(intent, 1);
			}
		});
		addsharelock_submit = (TextView) findViewById(R.id.addsharelock_submit);
		addsharelock_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ConnectionChangeReceiver.IsNetWork) {
					ShowToast("网络已断开，请检查网络");
					return;
				}
				// TODO Auto-generated method stub
				String telRegex = "[1][3578]\\d{9}";
				// TODO Auto-generated method stub
				String phonenumString = addsharelock_et_phonenumber.getText().toString();
				if (phonenumString.equals("")) {
					ShowToast("被分享者手机号不能为空");
					return;
				}
				if (!phonenumString.matches(telRegex)) {
					ShowToast("手机号码格式不正确");
					return;
				}

				if (mCheckSwithcButton.isChecked()) {
					day = "200000";
				}
				guest = addsharelock_et_phonenumber.getText().toString();

				new RequestTask().execute();
				progressDialog.show();
			}
		});

	}

	private class RequestTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			try {
				if (!GetShareData) {
					return RequestData(lockid, guest, day);
				} else {
					return RequestGetData();
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
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
				if (!GetShareData) {
					if (result.equals("No")) {
						Toast.makeText(AddShareLock.this, "不能共享给自己", Toast.LENGTH_LONG).show();
						progressDialog.hide();
						progressDialog.dismiss();
						return;
					}
					if (result.equals("NoID")) {
						Toast.makeText(AddShareLock.this, "车锁不存在，请重新绑定再分享", Toast.LENGTH_LONG).show();
						progressDialog.hide();
						progressDialog.dismiss();
						return;
					}
					JSONObject da = null;
					try {
						da = new JSONObject(result);
						String msg = da.getString("message");

						if (msg.equals("succ")) {
							// Toast.makeText(AddShareLock.this, "车锁已共享",
							// Toast.LENGTH_LONG).show();
							// finish();
							GetShareData = true;
							new RequestTask().execute();
						} else {
							Toast.makeText(AddShareLock.this, "共享失败", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(AddShareLock.this, "共享失败", Toast.LENGTH_LONG).show();
					}
				} else {
					try {
						JSONAnalysis(result);
						Toast.makeText(AddShareLock.this, "车锁已共享", Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(AddShareLock.this, "网络已断开，请检查网络", Toast.LENGTH_LONG).show();
					}
					finish();
				}
			} else if (result == null) {
				Toast.makeText(AddShareLock.this, "操作失败", Toast.LENGTH_LONG).show();
			}
			progressDialog.hide();
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri contactData = data.getData();
				Cursor cursor = managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				String num = this.getContactPhone(cursor);
				addsharelock_et_phonenumber.setText(num);
			}
			break;

		default:
			break;
		}
	}

	private String getContactPhone(Cursor cursor) {
		// TODO Auto-generated method stub
		int dacount = cursor.getCount();
		if (dacount == 0) {
			ShowToast("未能读取通讯录，请授权");
			return null;
		}
		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String result = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {
					int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phone.getInt(typeindex);
					String phoneNumber = phone.getString(index);
					result = phoneNumber.replace(" ", "");
					// switch (phone_type) {//此处请看下方注释
					// case 2:
					// result = phoneNumber;
					// break;
					//
					// default:
					// break;
					// }
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}

	public void JSONAnalysis(String result) throws JSONException {
		JSONArray object = null;
		try {
			object = new JSONArray(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < object.length(); i++) {
			JSONObject jsObject = object.getJSONObject(i);
			String _id = jsObject.optString("_id");
			String guest = jsObject.optString("guest");
			String usableDate = jsObject.optString("usableDate");
			String sn = jsObject.getJSONObject("lock").optString("sn");
			String bluetooth = jsObject.getJSONObject("lock").optString("bluetooth");

			if (!DB.ValidationShareBlue(bluetooth, guest)) {
				if (DB.InstarBlueDBShare(LockMain._OpenID, _id, guest, usableDate, sn, bluetooth)) {
					// ShowToast("添加成功");
				}
			}
			GetShareData = false;
		}

	}

	public String RequestData(String lockid, String guest, String day)
			throws ClientProtocolException, IOException, JSONException {

		JSONObject jsondt = new JSONObject();
		jsondt.put("lock", lockid);
		jsondt.put("guest", guest);
		jsondt.put("day", day);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(AppSetting.URL_STRING + "lockshare/share");
		StringEntity postingString = new StringEntity(jsondt.toString());// json传递
		post.setEntity(postingString);
		post.addHeader("Authorization", LockMain._Token);
		post.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() == 201) {
			try {
				HttpEntity qq = response.getEntity();
				String content = EntityUtils.toString(qq);

				return content;
			} catch (Exception e) {
				// TODO: handle exception
				String wwwww = e.toString();
				return null;
			}

		} else if (response.getStatusLine().getStatusCode() == 401) {
			return "No";
		} else if (response.getStatusLine().getStatusCode() == 402) {
			return "NoID";
		} else {
			return null;
		}
	}

	public String RequestGetData() throws ClientProtocolException, IOException, JSONException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(AppSetting.URL_STRING + "lockshare/myLockShareList");
		get.addHeader("Authorization", LockMain._Token);
		get.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
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

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

}
