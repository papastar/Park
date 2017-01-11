package com.ansai.uparking.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.libcommon.util.AppUtils;
import com.ansai.uparking.R;
import com.ansai.uparking.data.BleManager;
import com.ansai.uparking.data.LocationManager;
import com.ansai.uparking.data.UserInfoManager;

public class BootActivity extends BaseAppCompatActivity {


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (UserInfoManager.getInstance().isValid()) {
                readyGo(MainActivity.class);
            } else {
                readyGo(LoginActivity.class);
            }
            finish();
        }
    };

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_boot;
    }

    @Override
    protected void initViewsAndEvents() {
        BleManager.getInstance().getLiteBluetooth().enableBluetooth();
        LocationManager.getInstance().startLocation();
        AppUtils.runOnUIDelayed(mRunnable, 2000);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeRunnable(mRunnable);
    }
}
