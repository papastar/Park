package com.ansai.uparking.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.uparking.R;

import butterknife.Bind;

public class LockersActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_lockers;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "我的车锁");
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }


}
