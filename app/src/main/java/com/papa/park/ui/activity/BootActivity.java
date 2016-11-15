package com.papa.park.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.libcommon.util.AppUtils;
import com.papa.park.R;
import com.papa.park.data.LocationManager;
import com.papa.park.data.UserInfoManager;

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
