package com.joyotime.qparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.wxapi.WXEntryActivity;

public class UserSetting extends Activity {

	LinearLayout usersetting_bind_phone;
	LinearLayout usersetting_bind_wechat;
	LinearLayout usersetting_outlogin;
	static ImageView usersetting_headimg;
	static TextView usersetting_username;
	static TextView usersetting_phonenum;
	static TextView usersetting_wechat;
	static TextView usersetting_address;
	ImageView usersetting_goback;

	private Boolean IsExit = false;

	MethodDate mdb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usersetting);
		mdb = new MethodDate(this);

		usersetting_headimg = (ImageView) findViewById(R.id.usersetting_headimg);
		if (LockMain._headimage != null)
			usersetting_headimg.setImageBitmap(LockMain._headimage);

		usersetting_headimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowToast("暂未开放");
			}
		});

		usersetting_goback = (ImageView) findViewById(R.id.usersetting_goback);
		usersetting_goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		usersetting_username = (TextView) findViewById(R.id.usersetting_username);
		usersetting_username.setText(LockMain._UserName);

		usersetting_address = (TextView) findViewById(R.id.usersetting_address);
		usersetting_address.setText(LockMain._AddressString);

		usersetting_phonenum = (TextView) findViewById(R.id.usersetting_phonenum);
		usersetting_wechat = (TextView) findViewById(R.id.usersetting_wechat);

		usersetting_bind_phone = (LinearLayout) findViewById(R.id.usersetting_bind_phone);
		usersetting_bind_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// startActivity(new Intent(UserSetting.this,
				// BindingPhone.class));
				if (usersetting_phonenum.getText().equals("未绑定")) {
					// TODO Auto-generated method stub
					startActivity(new Intent(UserSetting.this, BindingPhone.class));
				} else {
					ShowToast("已绑定");
				}
			}
		});
		usersetting_bind_wechat = (LinearLayout) findViewById(R.id.usersetting_bind_wechat);
		usersetting_bind_wechat.setVisibility(View.GONE);
		usersetting_bind_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserSetting.this, WXEntryActivity.class));
			}
		});
		usersetting_outlogin = (LinearLayout) findViewById(R.id.usersetting_outlogin);
		usersetting_outlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mdb.DetetelUserData(LockMain._OpenID)) {
					ShowToast("退出成功");
					IsExit = true;
					finish();

				} else {
					ShowToast("退出失败");
				}
			}
		});

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (LockMain._phonenum != null) {
			if (!LockMain._phonenum.equals(""))
				usersetting_phonenum.setText(LockMain._phonenum);
			if (LockMain._headimage != null) {
				usersetting_wechat.setText("已绑定");

			}
		}
		handler.postDelayed(task, 100);
		super.onResume();
	}

	private void ShowToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stu
		super.onDestroy();
		if (IsExit) {
			System.runFinalizersOnExit(true);
			System.exit(0);
		}
	}

	public static Handler handler = new Handler();

	private final Runnable task = new Runnable() {

		public void run() {
			if (LockMain._headimage != null) {

				/* 这里写你的功能模块代码 */
				if (usersetting_username != null && usersetting_wechat != null) {
					if (!LockMain._UserName.equals(""))
						usersetting_username.setText(LockMain._UserName);
					if (LockMain._AddressString.equals(""))
						usersetting_address.setText(LockMain._AddressString);
					if (LockMain._headimage != null) {
						usersetting_wechat.setText("已绑定");
						usersetting_headimg.setImageBitmap(LockMain._headimage);
					}
				}
			} else {
				handler.postDelayed(this, 100);
			}
		}
	};
}
