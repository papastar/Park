/************************************************************************************************** Filename: FwUpdateActivity.java
 * Revised: $Date: 2013-09-05 05:55:20 +0200 (to, 05 sep 2013) $
 * Revision: $Revision: 27614 $
 * 
 * Copyright 2013 Texas Instruments Incorporated. All rights reserved.
 * 
 * IMPORTANT: Your use of this Software is limited to those specific rights
 * granted under the terms of a software license agreement between the user
 * who downloaded the software, his/her employer (which must be your employer)
 * and Texas Instruments Incorporated (the "License"). You may not use this
 * Software unless you agree to abide by the terms of the License.
 * The License limits your use, and you acknowledge, that the Software may not
 * be
 * modified, copied or distributed unless used solely and exclusively in
 * conjunction
 * with a Texas Instruments Bluetooth device. Other than for the foregoing
 * purpose,
 * you may not use, reproduce, copy, prepare derivative works of, modify,
 * distribute,
 * perform, display or sell this Software and/or its documentation for any
 * purpose.
 * 
 * YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE
 * PROVIDED 锟紸S IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,
 * INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL
 * TEXAS INSTRUMENTS OR ITS LICENSORS BE LIABLE OR OBLIGATED UNDER CONTRACT,
 * NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER
 * LEGAL EQUITABLE THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES
 * INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT, PUNITIVE
 * OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT
 * OF SUBSTITUTE GOODS, TECHNOLOGY, SERVICES, OR ANY CLAIMS BY THIRD PARTIES
 * (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.
 * 
 * Should you have any questions regarding your right to use this Software,
 * contact Texas Instruments Incorporated at www.TI.com **************************************************************************************************/
package com.joyotime.qparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ansai.uparking.R;
import com.joyotime.qparking.ble.BluetoothLeClass;
import com.joyotime.qparking.ble.Conversion;
import com.joyotime.qparking.ble.UpUtils;
import com.joyotime.qparking.db.AppSetting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class FwUpdateActivity extends Activity
{
	public final static String EXTRA_MESSAGE = "ti.android.ble.sensortag.MESSAGE";
	// Log
	private static String TAG = "FwUpdateActivity";

	private BluetoothLeClass mBluetoothLeClass;

	// Activity

	private static byte flag = 0;
	// Programming parameters
	private static final int PKT_INTERVAL = 1; // Milliseconds

	private static final int FILE_BUFFER_SIZE = 0x40000;

	private static final int OAD_BLOCK_SIZE = 16;
	private static final int HAL_FLASH_WORD_SIZE = 4;
	private static final int OAD_BUFFER_SIZE = 2 + OAD_BLOCK_SIZE;
	private static final int OAD_IMG_HDR_SIZE = 8;

	// GUI

	private TextView mLog;
	private ProgressBar mProgressBar;
	private Button mBtnStart;
	// BLE

	private BluetoothGattCharacteristic mCharIdentify = null;
	private BluetoothGattCharacteristic mCharBlock = null;

	// Programming
	private final byte[] mFileBuffer = new byte[FILE_BUFFER_SIZE];
	private final byte[] mOadBuffer = new byte[OAD_BUFFER_SIZE];
	private final byte[] mmOadBuffer = new byte[OAD_BUFFER_SIZE];
	private ImgHdr mFileImgHdr = new ImgHdr();
	private ImgHdr mTargImgHdr = new ImgHdr();
	private Timer getTimer = null;
	private ProgInfo mProgInfo = new ProgInfo();

	// Housekeeping
	private boolean mProgramming = false;

	TextView fwupdate_V;
	TextView fwupdate_filesize;
	TextView fwupdate_downloadtime;
	TextView fwupdate_downspeed;
	int nowtime = 0;
	private TimerTask mTimerTask = null;
	private Timer mTimer = null;

	private ImageView fwupdate_goback;
	private static Context contextmian;

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			if (msg.what == 0x1234)
			{
				loadFile(mBluetoothLeClass.mFilePath, false);
			}
		}
	};
	public FwUpdateActivity()
	{
		Log.d(TAG, "construct");
		UpUtils.set_BluetoothLeClass(LockMain.mBLE);
		UpUtils.set_Oadimgidentif(LockMain.gattCharacteristic_oadimgidentify);
		UpUtils.set_Oadimablock(LockMain.gattCharacteristic_oadimablock);
		mBluetoothLeClass = UpUtils.get_BluetoothLeClass();

		mCharIdentify = UpUtils.get_Oadimgidentif();
		mCharBlock = UpUtils.get_Oadimablock();
		// mCharConnReq = ;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d(TAG, "onCreate");

		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_fwupdate);
		fwupdate_V = (TextView) findViewById(R.id.fwupdate_V);
		fwupdate_filesize = (TextView) findViewById(R.id.fwupdate_filesize);
		fwupdate_downloadtime = (TextView) findViewById(R.id.fwupdate_downloadtime);
		fwupdate_downspeed = (TextView) findViewById(R.id.fwupdate_downspeed);
		contextmian = this;
		fwupdate_goback = (ImageView) findViewById(R.id.fwupdate_goback);
		fwupdate_goback.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if (mProgramming)
				{
					Toast.makeText(contextmian, R.string.prog_ogoing, Toast.LENGTH_LONG).show();
				}
				else
				{
					finish();
				}

			}
		});
		// 设置notify 功能使能，适配android 5.0以上系统
		LockMain.mBLE.setCharacteristicNotification(LockMain.gattCharacteristic_oadimablock, true);

		// Context title
		setTitle(R.string.title_oad);

		// Initialize widgets
		mLog = (TextView) findViewById(R.id.tw_log);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		// mBtnStart.setEnabled(false);
		mBtnStart.setBackground(getResources().getDrawable(R.drawable.btn_forbidden));
		mBtnStart.setClickable(false);

		if (AppSetting.mSoftWare < AppSetting.serviceSoftVersion)
		{
			try
			{
				get_FileTye(); 
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread()
			{
				public void run()
				{
					while (true)
					{
						if (BluetoothLeClass.mFilePath != null)
						{
							mHandler.sendEmptyMessage(0x1234);
							break;
						}
						try
						{
							Thread.sleep(10);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
		else
		{
			mLog.setText("已是最新版");
		}
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		if (mTimerTask != null)
			mTimerTask.cancel();
		mTimer = null;
		getTimer = null;
	}

	@Override
	public void onBackPressed()
	{
		Log.d(TAG, "onBackPressed");
		if (mProgramming)
		{
			Toast.makeText(this, R.string.prog_ogoing, Toast.LENGTH_LONG).show();
		}
		else
			super.onBackPressed();
	}

	@Override
	protected void onResume()
	{
		Log.d(TAG, "onResume");
		super.onResume();

	}

	@Override
	protected void onPause()
	{
		Log.d(TAG, "onPause");
		super.onPause();
		// unregisterReceiver(mGattUpdateReceiver);
	}

	public void onStart(View v)
	{
		if (mProgramming)
		{
			stopProgramming();
		}
		else
		{
			startProgramming();
			mLog.append("开始更新，请勿离开车锁\n");
		}
	}

	private void startProgramming()
	{
		nowtime = 0;
		mLog.append("正在更新...\n");
		mProgramming = true;
		updateGui();
		// Prepare image notification
		byte[] buf = new byte[OAD_IMG_HDR_SIZE + 2 + 2 + 4];
		buf[0] = Conversion.loUint16(mFileImgHdr.ver);
		buf[1] = Conversion.hiUint16(mFileImgHdr.ver);
		buf[2] = Conversion.loUint16(mFileImgHdr.len);
		buf[3] = Conversion.hiUint16(mFileImgHdr.len);
		// 定义私有数据校验，防止其他工具刷机
		buf[4] = (byte) 0xAB;
		buf[5] = (byte) 0xB3;
		buf[6] = (byte) 0x5C;
		buf[7] = (byte) 0xFD;
		System.arraycopy(mFileImgHdr.uid, 0, buf, 8, 4);

		// Send image notification
		mCharIdentify.setValue(buf);

		mBluetoothLeClass.writeCharacteristic(mCharIdentify);

		// Initialize stats
		mProgInfo.reset();

		// Start the packet timer
		getTimer = null;

		getTimer = new Timer();
		mTimer = new Timer();
		getTimer.schedule(new TimerTaskTest(), 0, 1000);

		mTimerTask = new ProgTimerTask();
		mTimer.scheduleAtFixedRate(mTimerTask, 0, PKT_INTERVAL);

	}

	public class TimerTaskTest extends TimerTask
	{

		@Override
		public void run()
		{
			nowtime++;
			Message msg = new Message();
			msg.what = nowtime;
			statichandler.sendMessage(msg);
		}

	}
	private Handler statichandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			fwupdate_downloadtime.setText(msg.what + " s");
			fwupdate_downspeed.setText((mnum + 1) * 100 / mProgInfo.nBlocks + "%");

		}
	};
	private Handler jdhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String mlog = mLog.getText().toString();
			String[] arrlog = mlog.split("\n");
			mlog = "";
			for (int i = 0; i < arrlog.length-1; i++)
			{
				mlog += arrlog[i]+"\n";
			}
			mlog += "总共" + mProgInfo.nBlocks + "个数据包，已发送" + mnum + "个。\n";
			mLog.setText(mlog);
			if(!mProgramming)
			{
				String str = "升级失败，请重新升级!!!";
				Dialog dialog = new AlertDialog.Builder(contextmian).setTitle("提示").setCancelable(false).setMessage(str).setPositiveButton("重新升级",// 设置确定按钮
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								// ShowToast("升级成功，正在重启车锁...");
								startProgramming();
								mLog.append("开始更新，请勿离开车锁\n");
							}
						}).create();// 创建
				// 显示对话框
				dialog.show();
			}
		}
	};
	
	

	private void stopProgramming()
	{

		getTimer.cancel();
		getTimer.purge();
		mTimer.cancel();
		mTimer.purge();
		mTimerTask.cancel();
		mTimerTask = null;
		mProgramming = false;
		fwupdate_downspeed.setText("0%");
		// mProgressBar.setProgress(0);

		updateGui();

		// if (mProgInfo.iBlocks == mProgInfo.nBlocks) {
		if (mnum == mProgInfo.nBlocks - 1)
		{
			mLog.append("升级成功\n");
			BluetoothLeClass.mFilePath = null;
			// Toast.makeText(FwUpdateActivity.this, "升级成功。",
			// Toast.LENGTH_LONG).show();
			mBtnStart.setText("更新完成");
			mBtnStart.setClickable(false);
			mBtnStart.setBackground(getResources().getDrawable(R.drawable.btn_forbidden));
			StringBuilder sb = new StringBuilder(Environment.getExternalStorageDirectory() + "/").append("QParking/" + AppSetting.mHardWare + "/");
			File file = new File(sb.toString());
			if (file.exists())
			{
				// file.delete();
			}

			String str = "升级成功，正在重启车锁...";
			Dialog dialog = new AlertDialog.Builder(this).setTitle("提示").setCancelable(false).setMessage(str).setPositiveButton("确定",// 设置确定按钮
					new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							// ShowToast("升级成功，正在重启车锁...");
							Intent data = new Intent();
							setResult(1000, data);
							finish();
						}
					}).create();// 创建
			// 显示对话框
			dialog.show();

		}
		else
		{
			mLog.append("更新被取消\n");
			mBtnStart.setText("开始更新");
		}

	}
	private static void ShowToast(String msg)
	{
		Toast.makeText(contextmian, msg, 2000).show();
	}

	private void updateGui()
	{
		if (mProgramming)
		{
			// Busy: stop label, progress bar, disabled file selector
			mBtnStart.setText(R.string.cancel);

		}
		else
		{
			// Idle: program label, enable file selector
			// mProgressBar.setProgress(0);
			// mBtnStart.setText(R.string.start_prog);
			if (mFileImgHdr.imgType == 'A')
			{
				// mBtnLoadA.setEnabled(false);
				// mBtnLoadB.setEnabled(true);
			}
			else if (mFileImgHdr.imgType == 'B')
			{
				// mBtnLoadA.setEnabled(true);
				// mBtnLoadB.setEnabled(false);
			}
			// mBtnStart.setEnabled(false);
			// mBtnStart.setBackground(getResources().getDrawable(R.drawable.btn_forbidden));
			// mBtnStart.setClickable(false);
		}
	}

	private boolean loadFile(String filepath, boolean isAsset)
	{
		boolean fSuccess = false;

		// Load binary file
		try
		{
			// Read the file raw into a buffer
			InputStream stream;
			if (isAsset)
			{
				stream = getAssets().open(filepath);
			}
			else
			{
				File f = new File(filepath);
				stream = new FileInputStream(f);
			}
			stream.read(mFileBuffer, 0, mFileBuffer.length);
			stream.close();
		}
		catch (IOException e)
		{
			// Handle exceptions here
			mLog.setText("文件打开失败：" + filepath + "\n");
			return false;
		}

		// Show image info
		short verString = Conversion.buildUint16(mFileBuffer[5], mFileBuffer[4]);
		mFileImgHdr.ver = verString;
		mFileImgHdr.len = Conversion.buildUint16(mFileBuffer[7], mFileBuffer[6]);
		mFileImgHdr.imgType = ((mFileImgHdr.ver & 1) == 1) ? 'B' : 'A';
		System.arraycopy(mFileBuffer, 8, mFileImgHdr.uid, 0, 4);
		displayImageInfo(mFileImgHdr);

		// Verify image types
		boolean ready = mFileImgHdr.imgType != mTargImgHdr.imgType;
		// Enable programming button only if image types differ
		// mBtnStart.setEnabled(ready);
		if (!ready)
		{
			mBtnStart.setBackground(getResources().getDrawable(R.drawable.btn_forbidden));
		}
		else
		{
			mBtnStart.setBackground(getResources().getDrawable(R.drawable.btn_signin));
		}
		mBtnStart.setClickable(ready);

		displayStats();

		// Log
		mLog.setText("即将更新 Fac_V" + AppSetting.serviceSoftVersion + " 版本" + mFileImgHdr.imgType + " 包。\n");

		mLog.append(ready ? "准备就绪!\n" : "更新包不兼容!\n");

		updateGui();
		// boolean success =
		// DeviceScanActivity.writeChar_oadimgidentify("ad".getBytes());
		return fSuccess;
	}

	private void displayImageInfo(ImgHdr h)
	{
		int imgVer = (h.ver) >> 1;
		int imgSize = h.len * 4 / 1024;
		// String s = String.format("文件: %c \n版本号.: %d\n 大小: %d", h.imgType,
		// imgVer, imgSize);
		// v.setText(Html.fromHtml(s));

		fwupdate_V.setText("Fac_V" + AppSetting.mSoftWare);
		fwupdate_filesize.setText(imgSize + " KB");
	}

	private void displayStats()
	{
		int byteRate = 0;
		int sec = mProgInfo.iTimeElapsed / 1000;
		if (sec > 0)
		{
			byteRate = mnum / sec;
		}
		else
		{
			int n = 0;
			for (int i = 0; i < 30; i++)
			{
				n = 1000 + (int) (Math.random() * 30);
				if (n > byteRate + 10 || n < byteRate - 10)
					byteRate = n;
			}
		}

	}

	/* Called when a notification with the current image info has been received */
	Handler mmhanHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (msg.what == 0x1234)
			{
				Toast mToast = Toast.makeText(FwUpdateActivity.this, "now is imageB please update imageA first", Toast.LENGTH_LONG);

				mToast.setGravity(Gravity.TOP, 0, 0);
				mToast.show();
			}
		};
	};
	static boolean mtoast = false;
	static short mnum = 0;
	public static byte[] mbyte = new byte[2];

	static void showToast()
	{
		mtoast = true;
	}
	public static void setValue(byte[] b)
	{
		mbyte = b;
		mnum = (short) ((short) (mbyte[0] & 0xff) + (short) ((mbyte[1] & 0x00ff) << 8));
		Log.i(TAG, "mnum==" + mnum);
		flag = 1;
	}
	private void sendbuf()
	{
		Message msg = new Message();
		msg.what = 0;
		jdhandler.sendMessage(msg);
		if (mnum < mProgInfo.nBlocks)
		{
			Log.i(TAG, "nBlocks== " + mProgInfo.nBlocks);
			mProgramming = true;
			Log.i(TAG, "mnum ==" + mnum);
			// Prepare block
			mOadBuffer[0] = Conversion.loUint16(mnum);
			mOadBuffer[1] = Conversion.hiUint16(mnum);
			System.arraycopy(mFileBuffer, mnum * 16, mOadBuffer, 2, OAD_BLOCK_SIZE);
			Log.i(TAG, "length" + mOadBuffer.length);
			// System.arraycopy(mOadBuffer, 0, mmOadBuffer, 0,18);
			// Send block
			mCharBlock.setValue(mOadBuffer);
			boolean success = mBluetoothLeClass.writeCharacteristic(mCharBlock);
			if (success)
			{
				mProgressBar.setProgress((mnum + 1) * 100 / mProgInfo.nBlocks);

			}
			else
			{
				Log.i("hzxlelp3", "update err");
				// Check if the device has been prematurely disconnected
				if (mBluetoothLeClass.mBluetoothGatt == null)
					mProgramming = false;
			}
			if (mnum == mProgInfo.nBlocks - 1)
			{
				mProgramming = false;
			}
		}
		else
		{
			mProgramming = false;
		}
		mProgInfo.iTimeElapsed += PKT_INTERVAL;

		if (!mProgramming)
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					displayStats();
					stopProgramming();
				}
			});
		}
		flag = 0;
	}

	private class ProgTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			mProgInfo.mTick++;
			if (mProgramming)
			{
				if (flag == 1)
				{
					sendbuf();
				}
				if ((mProgInfo.mTick % PKT_INTERVAL) == 0)
				{
					runOnUiThread(new Runnable()
					{
						public void run()
						{
							displayStats();
						}
					});
				}
			}
		}
	}

	private class ImgHdr
	{
		short ver;
		short len;
		Character imgType;
		byte[] uid = new byte[4];
	}

	private class ProgInfo
	{
		int iBytes = 0; // Number of bytes programmed
		short iBlocks = 0; // Number of blocks programmed
		short nBlocks = 0; // Total number of blocks
		int iTimeElapsed = 0; // Time elapsed in milliseconds
		int mTick = 0;

		void reset()
		{
			iBytes = 0;
			iBlocks = 0;
			iTimeElapsed = 0;
			mTick = 0;
			nBlocks = (short) (mFileImgHdr.len / (OAD_BLOCK_SIZE / HAL_FLASH_WORD_SIZE));
		}
	}
	/* 获取SD卡的路径，并且在SD卡中找到升级文件A和B */

	public void get_FileTye() throws IOException
	{

		byte[] buf = new byte[OAD_IMG_HDR_SIZE + 2 + 2];
		buf[0] = (byte) 0xff;
		buf[1] = (byte) 0xff;
		buf[2] = (byte) 0xff;
		buf[3] = (byte) 0xff;
		for (int i = 0; i < 4; i++)
		{
			buf[i + 4] = (byte) 0xff;
		}
		// Send image notification
		mCharIdentify.setValue(buf);
		mBluetoothLeClass.writeCharacteristic(mCharIdentify);
	}
}