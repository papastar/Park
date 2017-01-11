package com.joyotime.qparking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansai.uparking.R;
import com.joyotime.qparking.view.PubileUI;

public class About_WebView extends Activity
{
	private WebView webview;
	private ImageView webview_goback;
	private TextView webview_title;
	private static Dialog progressDialog;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		Intent dtin = getIntent();
		Bundle dBundle = dtin.getExtras();
		progressDialog = PubileUI.MsgDialog(this, "加载中...");

		webview_title = (TextView) findViewById(R.id.webview_title);
		webview_goback = (ImageView) findViewById(R.id.webview_goback);
		webview_goback.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		
		
		webview_title.setText(dBundle.getString("title"));
		// 实例化WebView对象
		webview = (WebView) findViewById(R.id.webview);
		
		webview.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url)
			{
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				progressDialog.hide();
				progressDialog.dismiss();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				
				progressDialog.show();
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
			{
				// TODO Auto-generated method stub
				//super.onReceivedSslError(view, handler, error);
				handler.proceed();
			}
			
			
		});
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl(dBundle.getString("url"));
		// 设置Web视图

	}
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();

		webview.destroy();
		// WindowManager.removeView(webview);

	}

}
