package com.joyotime.qparking;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application
{

	@Override
	public void onCreate()
	{
		super.onCreate();
		SDKInitializer.initialize(this);
	}

}