package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.mvp.AddLockContract;
import com.papa.park.mvp.model.AddLockModel;
import com.papa.park.mvp.presenter.AddLockPresenter;

import butterknife.Bind;

public class AddLockActivity extends BaseFrameActivity<AddLockPresenter, AddLockModel> implements
        AddLockContract.View, Toolbar.OnMenuItemClickListener {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_lock;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        setToolbar(mToolBar, "添加车锁信息");
        mToolBar.inflateMenu(R.menu.menu_add_lock);
        mToolBar.setOnMenuItemClickListener(this);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                return true;
        }
        return false;
    }

    private void save() {

    }

}
