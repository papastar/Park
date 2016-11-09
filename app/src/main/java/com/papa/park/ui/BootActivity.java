package com.papa.park.ui;

import android.os.Bundle;
import android.view.View;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.libcommon.util.AppUtils;
import com.papa.park.R;

public class BootActivity extends BaseAppCompatActivity {


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            readyGo(MainActivity.class);
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
