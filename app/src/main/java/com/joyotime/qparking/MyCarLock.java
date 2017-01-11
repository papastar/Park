package com.joyotime.qparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.model.BlueMsg;
import com.joyotime.qparking.widget.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class MyCarLock extends Activity
{
	private List<BlueMsg> data = new ArrayList<BlueMsg>();
	private SwipeListView mListView;
	private SwipeAdapter mAdapter = null;
	private ImageView search_gobackImageView;
	MethodDate MD_METHODDATE = new MethodDate(this);
	WorkThread loaddata;

	private Context thisContext = null;

	Cursor cursor = null;
	public String notename;
	private String _SNString = null;
	private int _position = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		thisContext = this;
		setContentView(R.layout.activity_mycarlock);
		search_gobackImageView = (ImageView) findViewById(R.id.search_goback);
		search_gobackImageView.setOnClickListener(mylockcliek);

	}
	private class WorkThread extends Thread
	{
		@Override
		public void run()
		{
			DesPersonData();

			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
	}

	private void DesPersonData()
	{
		data = new ArrayList<BlueMsg>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			int notenameColumn = cursor.getColumnIndex("notename");
			int bluenameColumn = cursor.getColumnIndex("bluename");
			int parkingnameColumn = cursor.getColumnIndex("parkingname");
			int isownerColumn = cursor.getColumnIndex("isowner");

			int snColumn = cursor.getColumnIndex("sn");
			BlueMsg msg = null;
			String noteString = cursor.getString(notenameColumn).equals("") ? cursor.getString(bluenameColumn) : cursor.getString(notenameColumn);
			msg = new BlueMsg(noteString, "停车场:" + cursor.getString(parkingnameColumn), cursor.getString(snColumn));
			if (cursor.getString(isownerColumn).equals("0"))
			{
				msg.setIcon_id(R.drawable.ic_launcher);
			}
			else
			{
				msg.setIcon_id(R.drawable.ic_launcher);
			}

			data.add(msg);

		}

	}

	private OnClickListener mylockcliek = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			ImageView btn = (ImageView) v;
			// TODO Auto-generated method stub
			switch (btn.getId())
			{
				case R.id.search_goback :
					Intent data = new Intent();
					data.putExtra("istrue", "false");
					setResult(500, data);
					finish();
					break;
				default :
					break;
			}
		}
	};

	/** 初始化界�? */
	private void initView()
	{

		mListView = (SwipeListView) findViewById(R.id.mylock_listview);

		mAdapter = new SwipeAdapter(this, data);
		mListView.setRightViewWidth(0);

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (!mListView.mIsShown)
				{
					_SNString = data.get(position).getKeyId();

					if (MD_METHODDATE.UpdateIsUseLock(LockMain._OpenID, _SNString))
					{
						Intent data = new Intent();
						if (!LockMain._locksn.equals(_SNString))
						{
							data.putExtra("istrue", "true");
						}
						else
						{
							data.putExtra("istrue", "false");
						}
						setResult(500, data);
						finish();
					}
					else
					{
						ShowToast("选择失败");
					}

					// Intent intent = new Intent();
					// intent.setClass(MyCarLock.this, LockSetting.class);
					// intent.putExtra("sncode", _SNString);
					// intent.putExtra("show", _SNString);
					// startActivityForResult(intent, 1);
				}
			}
		});
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		cursor = MD_METHODDATE.GetAllBlue(LockMain._OpenID);
		loaddata = new WorkThread();
		loaddata.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Intent data = new Intent();
			data.putExtra("istrue", "false");
			setResult(500, data);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// SearchCarLock.mBLE.disconnect();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		// SearchCarLock.mBLE.close();
	}

	private void ShowToast(String msg)
	{
		Toast toast = Toast.makeText(getApplicationContext(), msg, 1500);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	protected void onDestroy()
	{

		super.onDestroy();

	}
	private class SwipeAdapter extends BaseAdapter
	{
		/** 上下文对�? */
		private Context mContext = null;
		private List<BlueMsg> data;

		/** @param mainActivity */
		public SwipeAdapter(Context ctx, List<BlueMsg> data)
		{
			mContext = ctx;
			this.data = data;
		}

		@Override
		public int getCount()
		{
			return data.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{

			ViewHolder holder;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_mylock, parent, false);
				holder = new ViewHolder();
				holder.item_left = (RelativeLayout) convertView.findViewById(R.id.item_left);
				// holder.item_right = (RelativeLayout)
				// convertView.findViewById(R.id.item_right);
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
				// holder.item_right_txt = (TextView)
				// convertView.findViewById(R.id.item_right_txt);
				holder.item_keyid_txt = (TextView) convertView.findViewById(R.id.item_keyid_txt);
				convertView.setTag(holder);
			}
			else
			{// 有直接获得ViewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			holder.item_left.setLayoutParams(lp1);
			// LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
			// LayoutParams.MATCH_PARENT);
			// holder.item_right.setLayoutParams(lp2);
			BlueMsg msg = data.get(position);

			holder.tv_title.setText(msg.getTitle());
			holder.tv_msg.setText(msg.getMsg());
			holder.iv_icon.setImageResource(msg.getIcon_id());

			return convertView;
		}

	}

	Handler myHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{

			if (msg.what == 1)
			{
				// loaddata.stop();
				initView();
			}
			else
			{

			}

		}
	};

	static class ViewHolder
	{
		RelativeLayout item_left;
		// RelativeLayout item_right;

		TextView tv_title;
		TextView tv_msg;
		ImageView iv_icon;

		// TextView item_right_txt;
		TextView item_keyid_txt;
	}

}
