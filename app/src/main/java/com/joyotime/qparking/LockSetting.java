package com.joyotime.qparking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.view.PubileUI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LockSetting extends Activity {

	private static String LockTaKen = "";
	private static String _Token = "";
	MethodDate db = new MethodDate(this);

	private TextView locksetting_lockname;
	private TextView locksetting_sncode;
	private TextView locksetting_blueaddress;

	private LinearLayout lock_btn_save;
	private ImageView locksetting_goback;

	private TextView locksetting_btn_share;

	String sn = null, bluetooth, key, _id, lockid, bluename, software, hardware, notename, locklong, locklat, cityname,
			lockaddress, parkingname, parkingaddress;

	private Button confirmButton;
	private Button cancleButton;
	private PopupWindow popupWindow;
	private View popupWindowView;

	Cursor cursor = null;
	private Boolean IsUpDate = false;
	private Boolean loadData = true;

	MethodDate MD_METHODDATE = new MethodDate(this);
	private TextView locksetting_parkingAddress;
	private TextView locksetting_tv_parkingname;
	private EditText locksetting_et_parkingname;
	private ImageView locksetting_iv_parkingname;
	private LinearLayout locksetting_ll_parkingnameLayout;

	private TextView locksetting_tv_lcokaddress;
	private EditText locksetting_et_lcokaddress;
	private ImageView locksetting_iv_lcokaddress;
	private LinearLayout locksetting_ll_lcokaddress;

	private TextView locksetting_tv_note;
	private EditText locksetting_et_note;
	private ImageView locksetting_iv_note;
	private LinearLayout locksetting_ll_note;

	private TextView locksetting_tv_mlong;
	private TextView locksetting_tv_mlat;
	private TextView locksetting_tv_mcityname;
	private TextView locksetting_tv_mcitycode;
	private TextView locksetting_title;
	private static int _key = 0;
	private static String _retrueStr = null;
	private static Dialog progressDialog = null;
	Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locksetting);
		context = this;

		progressDialog = PubileUI.MsgDialog(this, "正在获取证书...");
		locksetting_tv_mlong = (TextView) findViewById(R.id.locksetting_tv_mlong);
		locksetting_tv_mlat = (TextView) findViewById(R.id.locksetting_tv_mlat);
		locksetting_tv_mcityname = (TextView) findViewById(R.id.locksetting_tv_mcityname);
		locksetting_tv_mcitycode = (TextView) findViewById(R.id.locksetting_tv_mcitycode);
		locksetting_title = (TextView) findViewById(R.id.locksetting_title);

		locksetting_lockname = (TextView) findViewById(R.id.locksetting_lockname);
		locksetting_sncode = (TextView) findViewById(R.id.locksetting_sncode);
		locksetting_blueaddress = (TextView) findViewById(R.id.locksetting_blueaddress);

		lock_btn_save = (LinearLayout) findViewById(R.id.locksetting_btn_save);

		locksetting_tv_parkingname = (TextView) findViewById(R.id.locksetting_tv_parkingname);
		locksetting_et_parkingname = (EditText) findViewById(R.id.locksetting_et_parkingname);
		locksetting_iv_parkingname = (ImageView) findViewById(R.id.locksetting_iv_parkingname);
		locksetting_ll_parkingnameLayout = (LinearLayout) findViewById(R.id.locksetting_ll_parkingname);

		locksetting_parkingAddress = (TextView) findViewById(R.id.locksetting_parkingAddress);
		locksetting_goback = (ImageView) findViewById(R.id.locksetting_goback);
		locksetting_tv_lcokaddress = (TextView) findViewById(R.id.locksetting_tv_lcokaddress);
		locksetting_et_lcokaddress = (EditText) findViewById(R.id.locksetting_et_lcokaddress);
		locksetting_iv_lcokaddress = (ImageView) findViewById(R.id.locksetting_iv_lcokaddress);
		locksetting_ll_lcokaddress = (LinearLayout) findViewById(R.id.locksetting_ll_lcokaddress);
		locksetting_tv_note = (TextView) findViewById(R.id.locksetting_tv_note);
		locksetting_et_note = (EditText) findViewById(R.id.locksetting_et_note);
		locksetting_iv_note = (ImageView) findViewById(R.id.locksetting_iv_note);
		locksetting_ll_note = (LinearLayout) findViewById(R.id.locksetting_ll_note);

		locksetting_ll_parkingnameLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				locksetting_tv_parkingname.setVisibility(View.GONE);
				locksetting_et_parkingname.setVisibility(View.VISIBLE);
				locksetting_iv_parkingname.setVisibility(View.GONE);
				locksetting_et_parkingname.setText(locksetting_tv_parkingname.getText().toString());
				locksetting_et_parkingname.setFocusable(true);
				locksetting_et_parkingname.requestFocus();
				InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
				im.showSoftInput(locksetting_et_parkingname, 0);
				IsUpDate = true;
			}
		});

		locksetting_et_parkingname.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					locksetting_tv_parkingname.setVisibility(View.VISIBLE);
					locksetting_et_parkingname.setVisibility(View.GONE);
					locksetting_iv_parkingname.setVisibility(View.VISIBLE);
					locksetting_tv_parkingname.setText(locksetting_et_parkingname.getText().toString());
					locksetting_et_parkingname.setSelection(locksetting_tv_parkingname.getText().toString().length());
				}
			}
		});// setOnEditorActionListener
		locksetting_et_parkingname.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				locksetting_tv_parkingname.setVisibility(View.VISIBLE);
				locksetting_et_parkingname.setVisibility(View.GONE);
				locksetting_iv_parkingname.setVisibility(View.VISIBLE);
				locksetting_tv_parkingname.setText(locksetting_et_parkingname.getText().toString());
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		});

		locksetting_ll_lcokaddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				locksetting_tv_lcokaddress.setVisibility(View.GONE);
				locksetting_et_lcokaddress.setVisibility(View.VISIBLE);
				locksetting_iv_lcokaddress.setVisibility(View.GONE);
				locksetting_et_lcokaddress.setText(locksetting_tv_lcokaddress.getText().toString());
				locksetting_et_lcokaddress.setFocusable(true);
				locksetting_et_lcokaddress.requestFocus();
				InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
				im.showSoftInput(locksetting_et_lcokaddress, 0);
			}
		});

		locksetting_et_lcokaddress.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					locksetting_tv_lcokaddress.setVisibility(View.VISIBLE);
					locksetting_et_lcokaddress.setVisibility(View.GONE);
					locksetting_iv_lcokaddress.setVisibility(View.VISIBLE);
					locksetting_tv_lcokaddress.setText(locksetting_et_lcokaddress.getText().toString());
					locksetting_et_lcokaddress.setSelection(locksetting_tv_lcokaddress.getText().toString().length());
				}
			}
		});// setOnEditorActionListener
		locksetting_et_lcokaddress.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				locksetting_tv_lcokaddress.setVisibility(View.VISIBLE);
				locksetting_et_lcokaddress.setVisibility(View.GONE);
				locksetting_iv_lcokaddress.setVisibility(View.VISIBLE);
				locksetting_tv_lcokaddress.setText(locksetting_et_lcokaddress.getText().toString());
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		});

		locksetting_ll_note.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				locksetting_tv_note.setVisibility(View.GONE);
				locksetting_et_note.setVisibility(View.VISIBLE);
				locksetting_iv_note.setVisibility(View.GONE);
				locksetting_et_note.setText(locksetting_tv_note.getText().toString());
				locksetting_et_note.setFocusable(true);
				locksetting_et_note.requestFocus();
				InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
				im.showSoftInput(locksetting_et_note, 0);
			}
		});

		locksetting_et_note.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					locksetting_tv_note.setVisibility(View.VISIBLE);
					locksetting_et_note.setVisibility(View.GONE);
					locksetting_iv_note.setVisibility(View.VISIBLE);
					locksetting_tv_note.setText(locksetting_et_note.getText().toString());
					locksetting_et_note.setSelection(locksetting_tv_note.getText().toString().length());
				}
			}
		});// setOnEditorActionListener
		locksetting_et_note.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				locksetting_tv_note.setVisibility(View.VISIBLE);
				locksetting_et_note.setVisibility(View.GONE);
				locksetting_iv_note.setVisibility(View.VISIBLE);
				locksetting_tv_note.setText(locksetting_et_note.getText().toString());
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				return true;
			}
		});

		locksetting_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		locksetting_btn_share = (TextView) findViewById(R.id.locksetting_btn_share);
		locksetting_btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ConnectionChangeReceiver.IsNetWork) {
					ShowToast("网络已断开，请检查网络");
					return;
				}
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (!db.ValidationBlue(bluetooth, "1")) {
					_key = 1;
					new RequestTask().execute();
					progressDialog = PubileUI.MsgDialog(context, "正在提交...");
					progressDialog.show();
				} else {
					_key = 2;
					new RequestTask().execute();
					progressDialog = PubileUI.MsgDialog(context, "正在提交...");
					progressDialog.show();
				}
			}
		});

		lock_btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				intent.setClass(LockSetting.this, AddShareLock.class);
				intent.putExtra("lockid", lockid);
				startActivityForResult(intent, 1);
				finish();

			}
		});

		Intent Data = getIntent();
		Bundle bunData = Data.getExtras();

		bluetooth = bunData.getString("macaddr");
		_Token = bunData.getString("taken");
		sn = bunData.getString("sncode");

		bluename = bunData.getString("bluename");
		String showGX = bunData.getString("show");
		if (showGX == null || showGX.equals("")) {
			lock_btn_save.setVisibility(View.GONE);
		} else {
			locksetting_title.setText("更新车锁信息");
		}
		if (!ConnectionChangeReceiver.IsNetWork) {
			ShowToast("网络已断开，请检查网络");
			return;
		} else {
			new RequestTask().execute();
		}

		if (sn == null || sn.equals("")) {
			progressDialog.show();
			software = Integer.parseInt(bunData.getString("software"), 16) + "";
			hardware = Integer.parseInt(bunData.getString("hardware"), 16) + "";
		}

	}

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private class RequestTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			try {
				switch (_key) {
				case 0:
					if (sn == null || sn.equals("")) {

						_retrueStr = RequestData(bluetooth);
					} else {
						loadData = false;
						cursor = MD_METHODDATE.GetonlyBlue(sn);

						for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
							int notenameColumn = cursor.getColumnIndex("notename");
							int bluenameColumn = cursor.getColumnIndex("bluename");
							int blueaddressColumn = cursor.getColumnIndex("blueaddress");
							int lockidColumn = cursor.getColumnIndex("lockid");

							int locklongColumn = cursor.getColumnIndex("locklong");
							int locklatColumn = cursor.getColumnIndex("locklat");
							int lockaddressColumn = cursor.getColumnIndex("lockaddress");
							int citynameColumn = cursor.getColumnIndex("cityname");
							int parkingnameColumn = cursor.getColumnIndex("parkingname");
							int parkingaddressColumn = cursor.getColumnIndex("parkingaddress");

							lockid = cursor.getString(lockidColumn);
							bluetooth = cursor.getString(blueaddressColumn);
							bluename = cursor.getString(bluenameColumn);
							notename = cursor.getString(notenameColumn);
							locklong = cursor.getString(locklongColumn);
							locklat = cursor.getString(locklatColumn);
							cityname = cursor.getString(citynameColumn);
							lockaddress = cursor.getString(lockaddressColumn);
							parkingname = cursor.getString(parkingnameColumn);
							parkingaddress = cursor.getString(parkingaddressColumn);
						}

						_retrueStr = "local";
					}
					break;
				case 1:
					String lcokaddress = "";
					if (!locksetting_tv_lcokaddress.getText().toString().equals("")) {
						lcokaddress = locksetting_tv_lcokaddress.getText().toString();
					}
					if (!locksetting_et_lcokaddress.getText().toString().equals("")) {
						lcokaddress = locksetting_et_lcokaddress.getText().toString();
					}
					String parkingname = "";
					if (!locksetting_tv_parkingname.getText().toString().equals("")) {
						parkingname = locksetting_tv_parkingname.getText().toString();
					}
					if (!locksetting_et_parkingname.getText().toString().equals("")) {
						parkingname = locksetting_et_parkingname.getText().toString();
					}
					String note = "";// locksetting_tv_note.getText().toString();
					if (!locksetting_tv_note.getText().toString().equals("")) {
						note = locksetting_tv_note.getText().toString();
					}
					if (!locksetting_et_note.getText().toString().equals("")) {
						note = locksetting_et_note.getText().toString();
					}

					_retrueStr = InsterServiceData(LockTaKen, bluetooth, bluename, Login.m_Longitude, Login.m_Latitude,
							lcokaddress, Login.m_citycode, Login.m_cityname, parkingname, Login.m_addressde, note);
					break;

				case 2:
					String lcokaddress1 = "";
					if (!locksetting_tv_lcokaddress.getText().toString().equals("")) {
						lcokaddress1 = locksetting_tv_lcokaddress.getText().toString();
					}
					if (!locksetting_et_lcokaddress.getText().toString().equals("")) {
						lcokaddress1 = locksetting_et_lcokaddress.getText().toString();
					}
					String parkingname1 = "";
					if (!locksetting_tv_parkingname.getText().toString().equals("")) {
						parkingname1 = locksetting_tv_parkingname.getText().toString();
					}
					if (!locksetting_et_parkingname.getText().toString().equals("")) {
						parkingname1 = locksetting_et_parkingname.getText().toString();
					}
					String note1 = "";// locksetting_tv_note.getText().toString();
					if (!locksetting_tv_note.getText().toString().equals("")) {
						note1 = locksetting_tv_note.getText().toString();
					}
					if (!locksetting_et_note.getText().toString().equals("")) {
						note1 = locksetting_et_note.getText().toString();
					}

					_retrueStr = UpdateData(sn, note1, Login.m_Longitude, Login.m_Latitude, lcokaddress1,
							Login.m_citycode, Login.m_cityname, parkingname1, Login.m_addressde);
					break;
				default:
					break;
				}

				return _retrueStr;

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

			try {

				if (result != null && !result.equals("There")) {

					switch (_key) {
					case 0:
						if (sn == null || sn.equals("")) {
							JSONAnalysis(result);
						} else {
							locksetting_lockname.setText(bluename);
							locksetting_sncode.setText(sn);
							locksetting_blueaddress.setText(bluetooth);
							locksetting_tv_note.setText(notename);

							locksetting_tv_mlong.setText(locklong);
							locksetting_tv_mlat.setText(locklat);
							locksetting_tv_mcityname.setText(cityname);
							locksetting_tv_lcokaddress.setText(lockaddress);
							locksetting_tv_parkingname.setText(parkingname);
							locksetting_parkingAddress.setText(parkingaddress);
						}
						break;
					case 1:
						JSONObject jsondata = new JSONObject(result);
						lockid = jsondata.optString("_id");

						String lcokaddress = "";
						if (!locksetting_tv_lcokaddress.getText().toString().equals("")) {
							lcokaddress = locksetting_tv_lcokaddress.getText().toString();
						}
						if (!locksetting_et_lcokaddress.getText().toString().equals("")) {
							lcokaddress = locksetting_et_lcokaddress.getText().toString();
						}
						String parkingname = "";
						if (!locksetting_tv_parkingname.getText().toString().equals("")) {
							parkingname = locksetting_tv_parkingname.getText().toString();
						}
						if (!locksetting_et_parkingname.getText().toString().equals("")) {
							parkingname = locksetting_et_parkingname.getText().toString();
						}
						String note = "";// locksetting_tv_note.getText().toString();
						if (!locksetting_tv_note.getText().toString().equals("")) {
							note = locksetting_tv_note.getText().toString();
						}
						if (!locksetting_et_note.getText().toString().equals("")) {
							note = locksetting_et_note.getText().toString();
						}

						if (db.InstarBlueDBLocal(lockid, bluename, bluetooth, note, key, sn, LockTaKen,
								LockMain._OpenID, "1", "1", Login.m_Longitude, Login.m_Latitude, lcokaddress,
								Login.m_cityname, parkingname, Login.m_addressde, Login.m_citycode, "")) {
							db.InstarVersion(hardware, software, LockMain._OpenID, sn);
							ShowToast("添加成功");
							Intent data = new Intent();
							data.putExtra("istrue", "true");
							setResult(200, data);
							finish();
						} else {
							ShowToast("添加失败");
						}
						break;
					case 2:
						// 更新成功
						try {
							JSONObject jsObject = new JSONObject(result);
							String sn = jsObject.optString("sn");
							String bluetoothName = jsObject.optString("bluetoothName");
							String note2 = jsObject.optString("note");
							String skey = jsObject.optString("key");
							String slocktoken = jsObject.optString("token");

							String lockLng = jsObject.getJSONObject("location").getJSONArray("coordinates")
									.optString(0);
							String lockLat = jsObject.getJSONObject("location").getJSONArray("coordinates")
									.optString(1);
							String parkingAddress = UnicodeToString(
									jsObject.getJSONObject("parkingLot").optString("address"));
							String cityCode = jsObject.getJSONObject("parkingLot").optString("cityCode");
							String cityName = UnicodeToString(
									jsObject.getJSONObject("parkingLot").optString("cityName"));
							String parkingName = UnicodeToString(
									jsObject.getJSONObject("parkingLot").optString("name"));
							String lockAddress = UnicodeToString(jsObject.optString("address"));

							if (db.UpdateBlueDBLocal(bluetoothName, note2, skey, sn, slocktoken, lockLng, lockLat,
									lockAddress, cityName, parkingName, parkingAddress, cityCode, "")) {
								ShowToast("更新成功");
								finish();
								// new RequestTask().execute();
							} else {
								ShowToast("信息更新失败");
							}
						} catch (Exception e) {
							// TODO: handle exception
							ShowToast("信息更新失败");
						}

						break;

					default:
						break;
					}
				} else if (result != null && result.equals("There")) {
					Toast.makeText(LockSetting.this, "车锁已绑定", Toast.LENGTH_LONG).show();
					finish();
				} else {
					Toast.makeText(LockSetting.this, "操作失败", Toast.LENGTH_LONG).show();
					finish();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			progressDialog.hide();
			progressDialog.dismiss();
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

	public void JSONAnalysis(String result) throws JSONException {
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		_id = object.optString("_id");
		sn = object.optString("sn");
		LockTaKen = object.optString("token");
		key = object.optString("key");

		locksetting_lockname.setText(bluename);
		locksetting_sncode.setText(sn);
		locksetting_blueaddress.setText(bluetooth);
		locksetting_parkingAddress.setText(Login.m_addressde);

	}

	public String RequestData(String bluetooth) throws ClientProtocolException, IOException {

		String json = "{\"bluetooth\":\"" + bluetooth + "\",\"bluetoothName\":\"" + bluename + "\"}";

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(AppSetting.URL_STRING + "lockers/activate");
		StringEntity postingString = new StringEntity(json);// json传递
		post.setEntity(postingString);
		post.addHeader("Authorization", _Token);
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

		} else {
			return null;
		}
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

	public String UpdateData(String isSN, String Note, String lockLng, String lockLat, String lockAddress,
			String cityCode, String cityName, String parkingName, String parkingAddress)
					throws ClientProtocolException, IOException {

		String content = null;
		// String json = "{\"note\":\"" + chinaToUnicode(Note) + "\"}";
		JSONObject jsondt = new JSONObject();
		try {
			jsondt.put("sn", isSN);
			jsondt.put("note", Note);
			jsondt.put("lockLng", lockLng);
			jsondt.put("lockLat", lockLat);
			jsondt.put("lockAddress", chinaToUnicode(lockAddress));
			jsondt.put("cityCode", cityCode);
			jsondt.put("cityName", chinaToUnicode(cityName));
			jsondt.put("parkingName", chinaToUnicode(parkingName));
			jsondt.put("parkingAddress", chinaToUnicode(parkingAddress));

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpPatch post = new HttpPatch(AppSetting.URL_STRING + "lockers/sn/" + isSN);
		StringEntity postingString = new StringEntity(jsondt.toString());// json传递
		post.setEntity(postingString);
		post.addHeader("Authorization", LockMain._Token);
		post.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			try {
				HttpEntity qq = response.getEntity();
				content = EntityUtils.toString(qq);

				return content;
			} catch (Exception e) {
				// TODO: handle exception
				String wwwww = e.toString();
				return content;
			}

		} else {
			return content;
		}
	}

	public String InsterServiceData(String lockerToken, String bluetooth, String bluetoothName, String lockLng,
			String lockLat, String lockAddress, String cityCode, String cityName, String parkingName,
			String parkingAddress, String note) throws ClientProtocolException, IOException {
		JSONObject param = new JSONObject();
		try {
			param.put("lockerToken", lockerToken);
			param.put("bluetooth", bluetooth);
			param.put("bluetoothName", bluetoothName);
			param.put("note", note);
			param.put("sn", sn);
			param.put("lockLng", lockLng);
			param.put("lockLat", lockLat);
			param.put("lockAddress", chinaToUnicode(lockAddress));
			param.put("cityCode", cityCode);
			param.put("cityName", chinaToUnicode(cityName));
			param.put("parkingName", chinaToUnicode(parkingName));
			param.put("parkingAddress", chinaToUnicode(parkingAddress));
			param.put("key", key);
			param.put("hardware", hardware);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(AppSetting.URL_STRING + "lockers/save");
		StringEntity postingString = new StringEntity(param.toString());// json传递
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

		} else if (response.getStatusLine().getStatusCode() == 202) {
			return "There";
		} else {
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		_key = 0;
	}

}
