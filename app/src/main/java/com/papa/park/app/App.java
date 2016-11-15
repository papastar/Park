package com.papa.park.app;

import android.app.Application;
import android.content.Context;

import com.papa.libcommon.util.AppUtils;
import com.papa.libcommon.util.SharedPreferencesUtil;
import com.papa.libcommon.util.ToastUtils;

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
        SharedPreferencesUtil.init(this, getPackageName(), Context.MODE_PRIVATE);

    }


}
