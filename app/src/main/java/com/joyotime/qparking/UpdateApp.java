package com.joyotime.qparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ansai.uparking.R;
import com.joyotime.qparking.db.AppSetting;
import com.joyotime.qparking.db.MethodDate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpdateApp extends Activity {

	// 更新变量
	Button m_btnCheckNewestVersion;
	String m_appNameStr; // 下载到本地要给这个APP命的名字
	Handler m_mainHandler;
	ProgressDialog m_progressDlg;

	MethodDate mdb = new MethodDate(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upateapp);

		// 初始化相关变量
		initVariable();

		m_btnCheckNewestVersion.setOnClickListener(btnClickListener);
	}

	private void initVariable() {
		m_btnCheckNewestVersion = (Button) findViewById(R.id.chek_newest_version);
		m_mainHandler = new Handler();
		m_progressDlg = new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
		m_progressDlg.setIndeterminate(false);
		m_appNameStr = "Qparking.apk";
	}

	OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new checkNewestVersionAsyncTask().execute();
		}
	};

	class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (AppSetting.AppVCode > mdb.getVersion() && AppSetting.AppVCode > 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {// 如果有最新版本
				doNewVersionUpdate(); // 更新新版本
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}

	/** 提示更新新版本 */
	private void doNewVersionUpdate() {

		String str = "当前版本：" + mdb.getVersionName() + " ,发现新版本：" + AppSetting.AppVName + " ,是否更新？";
		Dialog dialog = new AlertDialog.Builder(this).setTitle("软件更新").setCancelable(false).setMessage(str)
				.setPositiveButton("更新", // 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								m_progressDlg.setTitle("正在下载");
								m_progressDlg.setMessage("请稍候...");
								downFile(AppSetting.Url_Android); // 开始下载
							}
						})
				.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 点击"取消"按钮之后退出程序
						finish();
					}
				}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	private void downFile(final String url) {
		m_progressDlg.setCancelable(false);
		m_progressDlg.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();

					m_progressDlg.setMax((int) length);// 设置进度条的最大值

					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(), m_appNameStr);
						if (file.exists()) {
							file.delete();
						}
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								m_progressDlg.setProgress(count);
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down(); // 告诉HANDER已经下载完成了，可以安装了
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/** 告诉HANDER已经下载完成了，可以安装了 */
	private void down() {
		m_mainHandler.post(new Runnable() {
			public void run() {
				m_progressDlg.cancel();
				update();
			}
		});
	}

	/** 安装程序 */
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), m_appNameStr)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
}
