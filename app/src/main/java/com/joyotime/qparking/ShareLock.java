package com.joyotime.qparking;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
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
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.model.BlueMsg;
import com.joyotime.qparking.widget.SwipeListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShareLock extends Activity
{
	private List<BlueMsg> data = new ArrayList<BlueMsg>();
	private SwipeListView mListView;
	private SwipeAdapter mAdapter = null;
	private int _position = -1;
	private String _KeyIdString = null;
	MethodDate MD_METHODDATE = new MethodDate(this);
	Cursor cursor = null;
	ImageView sharelock_goback;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharelock);
		sharelock_goback=(ImageView)findViewById(R.id.sharelock_goback);
		sharelock_goback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		cursor = MD_METHODDATE.GetShareBlue(LockMain._OpenID,"");
		new WorkThread().start();
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
			int guestColumn = cursor.getColumnIndex("guest");
			int snColumn = cursor.getColumnIndex("sn");
			int keyidColumn = cursor.getColumnIndex("shareid");
			BlueMsg msg = null;
			msg = new BlueMsg(cursor.getString(guestColumn), cursor.getString(snColumn), cursor.getString(keyidColumn));
			msg.setIcon_id(R.drawable.ic_launcher);
			data.add(msg);
		}

	}

	private void initView()
	{

		mListView = (SwipeListView) findViewById(R.id.sharelock_listview);
		mAdapter = new SwipeAdapter(this, data, mListView.getRightViewWidth());

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (!mListView.mIsShown)
				{
					_KeyIdString = data.get(position).getMsg();

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
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private class SwipeAdapter extends BaseAdapter
	{
		/** 上下文对�? */
		private Context mContext = null;
		private List<BlueMsg> data;

		private int mRightWidth = 0;


		public SwipeAdapter(Context ctx, List<BlueMsg> data, int rightWidth)
		{
			mContext = ctx;
			this.data = data;
			mRightWidth = rightWidth;
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
				holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
				holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
				holder.item_keyid_txt=(TextView)convertView.findViewById(R.id.item_keyid_txt);
				convertView.setTag(holder);
			}
			else
			{// 有直接获得ViewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			holder.item_left.setLayoutParams(lp1);
			LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
			holder.item_right.setLayoutParams(lp2);
			BlueMsg msg = data.get(position);

			holder.tv_title.setText(msg.getTitle());
			holder.tv_msg.setText(msg.getMsg());
			holder.iv_icon.setImageResource(msg.getIcon_id());

			holder.item_right.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mListView.hiddenRight(mListView.mPreItemView);
					_position = position;
					_KeyIdString = data.get(position).getKeyId();
					new RequestTaskDeleS().execute();

				}
			});
			return convertView;
		}

	}
	
	
	private class RequestTaskDeleS extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... params)
		{
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			return DeleteSData(_KeyIdString);
		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result) 
		{
			super.onPostExecute(result);
			if (result == null)
			{
				Toast.makeText(ShareLock.this, "解绑失败", Toast.LENGTH_LONG).show();
			}
			else if (result != null && result.equals("unbundling"))
			{
				JSONObject object = null;

				if (MD_METHODDATE.DetetelBlueShare(_KeyIdString))
				{
					data.remove(_position);
					ShowToast("解绑成功");
					mAdapter.notifyDataSetChanged();
				}
				else
				{
					ShowToast("解绑失败");
				}
			}

		}
	}
	public String DeleteSData(String keyid)
	{
		HttpClient httpClient = new DefaultHttpClient();

		String url = AppSetting.URL_STRING+"lockshare/delete/" + keyid;

		HttpDelete put = new HttpDelete(url);
		put.setHeader("Content-type", "application/json");
		put.addHeader("Authorization", LockMain._Token);
		StringBuilder builder = null;

		try
		{
			HttpResponse response = httpClient.execute(put);
			if (response.getStatusLine().getStatusCode() == 204)
			{
				return "unbundling";
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

	
	private void ShowToast(String msg)
	{
		Toast toast = Toast.makeText(getApplicationContext(), msg, 1500);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
		}
	};

	static class ViewHolder
	{
		RelativeLayout item_left;
		RelativeLayout item_right;

		TextView tv_title;
		TextView tv_msg;
		ImageView iv_icon;

		TextView item_right_txt;
		
		TextView item_keyid_txt;
	}

}
