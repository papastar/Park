package com.papa.park.ui;

import android.os.Bundle;
import android.view.View;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.mvp.MainContract;

public class MainActivity extends BaseFrameActivity<MainContract.MainPresenter,MainContract.Model> implements MainContract.View {


    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}
