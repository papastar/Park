package com.joyotime.qparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;

import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;
import com.joyotime.qparking.http.ConnectionChangeReceiver;
import com.joyotime.qparking.model.BlueMsg;
import com.joyotime.qparking.model.ShareItemData;
import com.joyotime.qparking.view.RoundImageView;
import com.joyotime.qparking.widget.SwipeListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LockManage extends Activity
{

	private List<BlueMsg> data = new ArrayList<BlueMsg>();
	private List<ShareItemData> listsharedata = new ArrayList<ShareItemData>();
	private SwipeListView mListView;
	private SwipeAdapter mAdapter = null;
	Cursor cursor = null;
	Cursor phones = null;

	private SwipeListView mListViewtwo;
	private SwipeAdapterTwo mAdaptertwo = null;

	MethodDate MD_METHODDATE = new MethodDate(this);

	LinearLayout lockmanage_ll_share;
	TextView lockmanage_tv_share;
	ImageView lockmanage_iv_share;

	LinearLayout lockmanage_ll_mylock;
	TextView lockmanage_tv_mylock;
	ImageView lockmanage_iv_mylock;
	ImageView lockmanage_goback;

	SwipeListView lockmanage_listviewone;
	SwipeListView lockmanage_listviewtwo;

	public String notename;
	private String _SNString = null;
	private int _position = -1;

	private String _KeyIdString = null;

	private int _OutListPosition = -2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockmanage);
		lockmanage_ll_share = (LinearLayout) findViewById(R.id.lockmanage_ll_share);
		lockmanage_ll_share.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				lockmanage_iv_share.setVisibility(View.VISIBLE);

				lockmanage_iv_mylock.setVisibility(View.GONE);

				lockmanage_listviewone.setVisibility(View.GONE);
				lockmanage_listviewtwo.setVisibility(View.VISIBLE);
			}
		});
		lockmanage_goback = (ImageView) findViewById(R.id.lockmanage_goback);
		lockmanage_goback.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		lockmanage_ll_mylock = (LinearLayout) findViewById(R.id.lockmanage_ll_mylock);
		lockmanage_ll_mylock.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				lockmanage_iv_share.setVisibility(View.GONE);

				lockmanage_iv_mylock.setVisibility(View.VISIBLE);

				lockmanage_listviewone.setVisibility(View.VISIBLE);
				lockmanage_listviewtwo.setVisibility(View.GONE);
			}
		});

		lockmanage_tv_share = (TextView) findViewById(R.id.lockmanage_tv_share);
		lockmanage_tv_mylock = (TextView) findViewById(R.id.lockmanage_tv_mylock);

		lockmanage_iv_share = (ImageView) findViewById(R.id.lockmanage_iv_share);
		lockmanage_iv_mylock = (ImageView) findViewById(R.id.lockmanage_iv_mylock);

		lockmanage_listviewone = (SwipeListView) findViewById(R.id.lockmanage_listviewone);
		lockmanage_listviewtwo = (SwipeListView) findViewById(R.id.lockmanage_listviewtwo);

	}
	private void initView()
	{

		mListView = (SwipeListView) findViewById(R.id.lockmanage_listviewone);
		mAdapter = new SwipeAdapter(this, data, mListView.getRightViewWidth());
		mListView.setRightViewWidth(0);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{

			}
		});

		mListViewtwo = (SwipeListView) findViewById(R.id.lockmanage_listviewtwo);
		mAdaptertwo = new SwipeAdapterTwo(this, listsharedata, mListViewtwo.getRightViewWidth());
		mListViewtwo.setRightViewWidth(0);
		mListViewtwo.setAdapter(mAdaptertwo);

		mListViewtwo.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (!mListViewtwo.mIsShown)
				{

				}
			}
		});
	}
	SwipeAdapterthree mAdapterz = null;

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

			final ViewHolder holder;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_lockmanageone, parent, false);
				holder = new ViewHolder();
				holder.item_left = (RelativeLayout) convertView.findViewById(R.id.lmo_item_left);
				holder.item_right = (RelativeLayout) convertView.findViewById(R.id.lmo_item_right);
				holder.iv_icon = (RoundImageView) convertView.findViewById(R.id.lmo_iv_icon);
				holder.tv_title = (TextView) convertView.findViewById(R.id.lmo_tv_lockname);
				holder.tv_msg = (TextView) convertView.findViewById(R.id.lmo_tv_addname);
				holder.item_right_txt = (TextView) convertView.findViewById(R.id.lmo_item_right_txt);
				holder.item_keyid_txt = (TextView) convertView.findViewById(R.id.lmo_item_keyid_txt);
				holder.item_sharetohe = (RelativeLayout) convertView.findViewById(R.id.lmo_item_one_sharetohe);
				holder.item_delete = (ImageView) convertView.findViewById(R.id.lmo_iv_two_delete);
				holder.item_line = (View) convertView.findViewById(R.id.lmo_item_one_line);
				holder.item_sharetohelist = (RelativeLayout) convertView.findViewById(R.id.lmo_item_one_sharetohelist);
				holder.item_triangle = (ImageView) convertView.findViewById(R.id.lmo_item_one_triangle);
				holder.item_isopen = (TextView) convertView.findViewById(R.id.lmo_item_one_isopen);

				holder.itme_listthree = (SwipeListView) convertView.findViewById(R.id.lockmanage_listviewthree);

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
			holder.item_keyid_txt.setText(msg.getKeyId());
			holder.item_isopen.setText("false");
			holder.item_sharetohe.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					if (_OutListPosition == -2 || _OutListPosition != position)
					{
						for (int i = 0; i < mListView.getCount(); i++)
						{
							View mView = mListView.getChildAt(i - mListView.getFirstVisiblePosition());// 获取指定itemIndex在屏幕中的view
							ViewHolder mViewHolder = (ViewHolder) mView.getTag();
							mViewHolder.item_isopen.setText("");// 重新设值
							mViewHolder.item_triangle.setBackgroundResource(R.drawable.lockadm_icon_triangle);

							mViewHolder.item_sharetohelist.setVisibility(View.GONE);
							mViewHolder.item_line.setVisibility(View.GONE);
							mViewHolder.item_isopen.setText("false");

						}
					}
					_OutListPosition = position;
					if (holder.item_isopen.getText().toString().equals("false"))
					{
						holder.item_triangle.setBackgroundResource(R.drawable.lockadm_icon_triangle_down);
						// TODO Auto-generated method stub
						holder.item_sharetohelist.setVisibility(View.VISIBLE);
						holder.item_line.setVisibility(View.VISIBLE);
						holder.item_isopen.setText("true");

						Cursor cursorz = MD_METHODDATE.GetShareBlue(LockMain._OpenID, data.get(position).getKeyId());

						int i = 0;
						ArrayList<BlueMsg> dataz = new ArrayList<BlueMsg>();
						for (cursorz.moveToFirst(); !cursorz.isAfterLast(); cursorz.moveToNext())
						{
							int guestColumn = cursorz.getColumnIndex("guest");
							int snColumn = cursorz.getColumnIndex("sn");
							int keyidColumn = cursorz.getColumnIndex("shareid");
							BlueMsg msg = null;
							msg = new BlueMsg(cursorz.getString(guestColumn), cursorz.getString(snColumn), cursorz.getString(keyidColumn));
							msg.setIcon_id(R.drawable.ic_launcher);
							dataz.add(msg);
							i++;
						}
						mAdapterz = new SwipeAdapterthree(mContext, dataz, holder.itme_listthree.getRightViewWidth());
						holder.itme_listthree.setRightViewWidth(0);
						holder.itme_listthree.setVisibility(View.VISIBLE);
						holder.itme_listthree.setAdapter(mAdapterz);

						RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.itme_listthree.getLayoutParams();
						linearParams.height = 135 * i;

						holder.itme_listthree.setLayoutParams(linearParams);
					}
					else
					{
						holder.item_triangle.setBackgroundResource(R.drawable.lockadm_icon_triangle);

						holder.item_sharetohelist.setVisibility(View.GONE);
						holder.item_line.setVisibility(View.GONE);
						holder.item_isopen.setText("false");
					}
				}
			});

			holder.item_left.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					if (!mListView.mIsShown)
					{
						_SNString = data.get(position).getKeyId();

						Intent intent = new Intent();
						intent.setClass(LockManage.this, LockSetting.class);
						intent.putExtra("sncode", _SNString);
						intent.putExtra("show", _SNString);
						startActivityForResult(intent, 1);
					}
				}
			});
			holder.item_delete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (!ConnectionChangeReceiver.IsNetWork)
					{
						ShowToast("网络已断开，请检查网络");
						return;
					}
					mListView.hiddenRight(mListView.mPreItemView);
					_position = position;
					_SNString = data.get(position).getKeyId();
					new RequestTaskD().execute();

				}
			});
			return convertView;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		cursor = MD_METHODDATE.GetAllBlue(LockMain._OpenID);
		new WorkThread().start();
	}
	private class WorkThread extends Thread
	{
		@Override
		public void run()
		{
			DesPersonData();
			DesPersonDataTwo();
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
	}

	public class SwipeAdapterthree extends BaseAdapter
	{
		/** 上下文对�? */
		private Context mContext = null;
		private List<BlueMsg> threedata;

		private int mRightWidth = 0;


		private SwipeAdapterthree(Context ctx, List<BlueMsg> data, int rightWidth)
		{
			mContext = ctx;
			this.threedata = data;
			mRightWidth = rightWidth;
		}

		@Override
		public int getCount()
		{
			return threedata.size();
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

			ViewHolderthree holder;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_lockmanagethree, parent, false);
				holder = new ViewHolderthree();
				holder.item_left = (RelativeLayout) convertView.findViewById(R.id.lmo_item_leftthree);
				holder.item_right = (RelativeLayout) convertView.findViewById(R.id.lmo_item_rightthree);
				holder.iv_icon = (RoundImageView) convertView.findViewById(R.id.lmo_iv_iconthree);
				holder.tv_phonenumber = (TextView) convertView.findViewById(R.id.lmo_tv_locktitlethree);
				holder.iv_btn_callphone = (RoundImageView) convertView.findViewById(R.id.lmo_iv_iconcellthree);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.lmo_iv_icondeletethree);

				holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
				holder.item_keyid_txt = (TextView) convertView.findViewById(R.id.item_keyid_txt);
				convertView.setTag(holder);
			}
			else
			{// 有直接获得ViewHolder
				holder = (ViewHolderthree) convertView.getTag();
			}

			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			holder.item_left.setLayoutParams(lp1);
			LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
			holder.item_right.setLayoutParams(lp2);
			BlueMsg msg = threedata.get(position);

			// holder.iv_icon.setImageResource(msg.getIcon_id());

			holder.tv_phonenumber.setText(msg.getTitle() + "(手机)");
			holder.iv_btn_callphone.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + threedata.get(position).getTitle()));
					startActivity(intent);
				}
			});
			holder.iv_delete.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub

					if (!ConnectionChangeReceiver.IsNetWork)
					{
						ShowToast("网络已断开");
						return;
					}
					_position = position;

					_KeyIdString = threedata.get(position).getKeyId();
					threedata.remove(_position);
					mAdapterz.notifyDataSetChanged();

					View mView = mListView.getChildAt(_OutListPosition - mListView.getFirstVisiblePosition());// 获取指定itemIndex在屏幕中的view
					ViewHolder mViewHolder = (ViewHolder) mView.getTag();
					RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) mViewHolder.itme_listthree.getLayoutParams();
					linearParams.height = 135 * mViewHolder.itme_listthree.getCount();

					mViewHolder.itme_listthree.setLayoutParams(linearParams);

					new RequestTaskDeleS().execute();

				}
			});

			return convertView;
		}

	}

	private void DesPersonDataTwo()
	{
		listsharedata = new ArrayList<ShareItemData>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{

			if (cursor.getString(cursor.getColumnIndex("isowner")).equals("0"))
			{
				int notenameColumn = cursor.getColumnIndex("notename");
				int bluenameColumn = cursor.getColumnIndex("bluename");
				int snColumn = cursor.getColumnIndex("sn");
				int parkingnameColumn = cursor.getColumnIndex("parkingname");
				int parkingaddressColumn = cursor.getColumnIndex("parkingaddress");
				int locklongColumn = cursor.getColumnIndex("locklong");
				int locklatColumn = cursor.getColumnIndex("locklat");
				int lockaddressColumn = cursor.getColumnIndex("lockaddress");
				ShareItemData data = null;
				String noteString = cursor.getString(notenameColumn).equals("") ? cursor.getString(bluenameColumn) : cursor.getString(notenameColumn);
				data = new ShareItemData(noteString, cursor.getString(parkingnameColumn), cursor.getString(parkingaddressColumn), cursor.getString(lockaddressColumn), cursor.getString(snColumn),
						cursor.getString(locklongColumn), cursor.getString(locklatColumn));
				data.setIcon_id(R.drawable.ic_launcher);
				listsharedata.add(data);
			}
		}

	}
	private void DesPersonData()
	{
		data = new ArrayList<BlueMsg>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			if (cursor.getString(cursor.getColumnIndex("isowner")).equals("1"))
			{
				int notenameColumn = cursor.getColumnIndex("notename");
				int bluenameColumn = cursor.getColumnIndex("bluename");
				int snColumn = cursor.getColumnIndex("sn");
				int parkingnameColumn = cursor.getColumnIndex("parkingname");
				BlueMsg msg = null;
				String noteString = cursor.getString(notenameColumn).equals("") ? cursor.getString(bluenameColumn) : cursor.getString(notenameColumn);
				msg = new BlueMsg(noteString, "停车场：" + cursor.getString(parkingnameColumn), cursor.getString(snColumn));
				msg.setIcon_id(R.drawable.ic_launcher);
				data.add(msg);
			}
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

	private class SwipeAdapterTwo extends BaseAdapter
	{
		/** 上下文对�? */
		private Context mContext = null;
		private List<ShareItemData> sharedata;

		private int mRightWidth = 0;


		public SwipeAdapterTwo(Context ctx, List<ShareItemData> sharedata, int rightWidth)
		{
			mContext = ctx;
			this.sharedata = sharedata;
			mRightWidth = rightWidth;
		}

		@Override
		public int getCount()
		{
			return sharedata.size();
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

			ViewHoldertwo holder;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_lockmanagetwo, parent, false);
				holder = new ViewHoldertwo();
				holder.item_left = (RelativeLayout) convertView.findViewById(R.id.lmo_item_lefttwo);
				holder.item_right = (RelativeLayout) convertView.findViewById(R.id.lmo_item_righttwo);
				holder.iv_icon = (RoundImageView) convertView.findViewById(R.id.lmo_iv_icontwo);
				holder.tv_lockname = (TextView) convertView.findViewById(R.id.lmo_tv_locknametwo);
				holder.tv_lockparking = (TextView) convertView.findViewById(R.id.lmo_tv_lockparkingtwo);
				holder.tv_lockaddress = (TextView) convertView.findViewById(R.id.lmo_tv_lockparkingaddresstwo);
				holder.tv_lockusetime = (TextView) convertView.findViewById(R.id.lmo_tv_lockusetimetwo);
				holder.tv_mlong_txt = (TextView) convertView.findViewById(R.id.lmo_item_mlong_txttwo);
				holder.tv_mlat_txt = (TextView) convertView.findViewById(R.id.lmo_item_mlat_txttwo);
				holder.iv_iconcelltwo = (RoundImageView) convertView.findViewById(R.id.lmo_iv_iconcelltwo);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.lmo_iv_icondeletetwo);
				holder.item_right_txt = (TextView) convertView.findViewById(R.id.lmo_item_right_txttwo);
				convertView.setTag(holder);
			}
			else
			{// 有直接获得ViewHolder
				holder = (ViewHoldertwo) convertView.getTag();
			}

			LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			holder.item_left.setLayoutParams(lp1);
			LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
			holder.item_right.setLayoutParams(lp2);
			ShareItemData data = sharedata.get(position);

			holder.tv_lockname.setText(data.getLockname());
			holder.tv_lockparking.setText(data.getLockparking());
			holder.tv_lockaddress.setText(data.getLockaddress());
			holder.tv_lockusetime.setText(data.getLockusetime());
			holder.iv_icon.setImageResource(data.getIcon_id());

			holder.tv_mlong_txt.setText(data.getmLong());
			holder.tv_mlat_txt.setText(data.getmLat());

			holder.iv_iconcelltwo.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					String shareSN = listsharedata.get(position).getKeyId();
					phones = MD_METHODDATE.GetSharePhoneNum(shareSN);
					String phonenum = "";
					for (phones.moveToFirst(); !phones.isAfterLast(); phones.moveToNext())
					{
						int phonenumColumn = phones.getColumnIndex("phonenum");
						phonenum = phones.getString(phonenumColumn);
					}
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum));
					startActivity(intent);
				}
			});

			holder.iv_delete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mListView.hiddenRight(mListView.mPreItemView);
					_position = position;
					String mLong = sharedata.get(position).getmLong();
					String mLat = sharedata.get(position).getmLat();
					launchNavigator2(Double.valueOf(Login.m_Longitude).doubleValue(), Double.valueOf(Login.m_Latitude).doubleValue(), Double.valueOf(mLong).doubleValue(), Double.valueOf(mLat)
							.doubleValue(), "false", "终点");

				}
			});
			return convertView;
		}

	}
	private void launchNavigator2(double startJD, double startWD, double endJD, double endWD, final String Parkingnavigation, String Title)
	{
		// 104.07054,30.581552
		// double mLat2 = 30.581552;
		// double mLon2 = 104.07054;
		// endJD=mLon2;
		// endWD=mLat2;
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标//104.066515,30.58346
//		BNaviPoint startPoint = new BNaviPoint(startJD, startWD, "当前位置", BNaviPoint.CoordinateType.BD09_MC);
//		BNaviPoint endPoint = new BNaviPoint(endJD, endWD, Title, BNaviPoint.CoordinateType.BD09_MC);
//		BaiduNaviManager.getInstance().launchNavigator(this, startPoint, // 起点（可指定坐标系）
//				endPoint, // 终点（可指定坐标系）
//				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
//				true, // 真实导航
//				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
//				new OnStartNavigationListener()
//				{ // 跳转监听
//
//					@Override
//					public void onJumpToNavigator(Bundle configParams)
//					{
//						Intent intent = new Intent(LockManage.this, BNavigatorActivity.class);
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
	private class RequestTaskD extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... params)
		{
			/** 在doInBackground方法中，做一些诸如网络请求等耗时操作。 */
			return DeleteData(_SNString);
		}

		/** onPostExecute方法主要是主线程中的数据更新。 */
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			if (result == null)
			{
				Toast.makeText(LockManage.this, "解绑失败", Toast.LENGTH_LONG).show();
			}
			else if (result != null && result.equals("unbundling"))
			{
				JSONObject object = null;

				if (MD_METHODDATE.DetetelBlueDBLocal(_SNString, LockMain._locksn.equals(_SNString), LockMain._OpenID))
				{
					data.remove(_position);
					ShowToast("解绑成功");
					mAdapter.notifyDataSetChanged();
					if (LockMain._locksn.equals(_SNString))
					{
						Intent data = new Intent();
						data.putExtra("istrue", "true");
						setResult(100, data);
					}

					// LockMain._locksn = "";
					// LockMain._lockkey = "";
					// LockMain._lockBlueAddress = "";
					// LockMain._lockName = "";
					// LockMain._lockBlueAddressLocad = "";
					// LockMain._parkingname = "";
				}
				else
				{
					ShowToast("解绑失败");
				}
			}

		}
	}

	public String DeleteData(String SN)
	{
		HttpClient httpClient = new DefaultHttpClient();

		String url = AppSetting.URL_STRING + "lockers/sn/" + SN;

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
				ShowToast("删除已分享锁失败");
			}
			else if (result != null && result.equals("unbundling"))
			{
				JSONObject object = null;

				if (MD_METHODDATE.DetetelBlueShare(_KeyIdString))
				{
					ShowToast("删除已分享锁");

				}
				else
				{
					ShowToast("删除已分享锁失败");
				}
			}

		}
	}
	public String DeleteSData(String keyid)
	{
		HttpClient httpClient = new DefaultHttpClient();

		String url = AppSetting.URL_STRING + "lockshare/delete/" + keyid;

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
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	static class ViewHolder
	{
		RelativeLayout item_left;
		RelativeLayout item_right;
		RelativeLayout item_sharetohe;
		TextView tv_title;
		TextView tv_msg;
		RoundImageView iv_icon;
		TextView item_right_txt;
		TextView item_keyid_txt;
		ImageView item_delete;
		View item_line;
		RelativeLayout item_sharetohelist;
		SwipeListView itme_listthree;
		ImageView item_triangle;
		TextView item_isopen;
	}
	static class ViewHoldertwo
	{
		RelativeLayout item_left;
		RelativeLayout item_right;
		TextView tv_lockname;
		TextView tv_lockparking;
		TextView tv_lockaddress;
		TextView tv_lockusetime;
		RoundImageView iv_icon;
		TextView item_right_txt;
		TextView tv_mlat_txt;
		TextView tv_mlong_txt;
		TextView item_keyid_txt;
		RoundImageView iv_iconcelltwo;
		ImageView iv_delete;
	}
	static class ViewHolderthree
	{
		RelativeLayout item_left;
		RelativeLayout item_right;
		RoundImageView iv_icon;
		TextView tv_phonenumber;
		RoundImageView iv_btn_callphone;
		ImageView iv_delete;

		TextView item_right_txt;
		TextView item_keyid_txt;

	}
}