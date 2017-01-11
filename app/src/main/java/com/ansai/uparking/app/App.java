package com.ansai.uparking.app;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.ansai.libcommon.util.AppUtils;
import com.ansai.libcommon.util.Logger;
import com.ansai.libcommon.util.SharedPreferencesUtil;
import com.ansai.libcommon.util.ToastUtils;
import com.ansai.uparking.BuildConfig;

/**
 * User: PAPA
 * Date: 2016-11-09
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        AppUtils.init(this);
        ToastUtils.init(this);
        Logger.init("Park", BuildConfig.DEV_MODE);
        SharedPreferencesUtil.init(this, getPackageName(), Context.MODE_PRIVATE);
        SDKInitializer.initialize(getApplicationContext());
    }


}
