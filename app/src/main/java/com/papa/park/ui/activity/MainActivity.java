package com.papa.park.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.mvp.MainContract;
import com.papa.park.mvp.model.MainModel;
import com.papa.park.mvp.presenter.MainPresenter;
import com.papa.park.ui.fragment.MainLockFragment;

import butterknife.Bind;

import static com.papa.park.R.id.container;
import static com.papa.park.R.id.toolBar;

public class MainActivity extends BaseFrameActivity<MainPresenter, MainModel> implements
        MainContract.View, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private static final int REQUEST_CODE_SEARCH_LOCK = 1;

    @Bind(toolBar)
    Toolbar mToolBar;
    @Bind(container)
    FrameLayout mContainer;
    @Bind(R.id.navigation_view)
    LinearLayout mNavigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    protected Fragment mCurrFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolbar();
        initFragment();

        readyGoForResult(SearchLockActivity.class, REQUEST_CODE_SEARCH_LOCK);
    }

    private void initFragment() {
        Fragment fragment = MainLockFragment.newInstance();
        setCurrFragment(fragment);
        toFragment(fragment);
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    private void initToolbar() {
        mToolBar.inflateMenu(R.menu.menu_main);
        mToolBar.setNavigationIcon(R.mipmap.ic_actionbar_menu);
        mToolBar.setOnMenuItemClickListener(this);
        mToolBar.setNavigationOnClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SEARCH_LOCK:
                    break;
            }
        }
    }

    protected void toFragment(Fragment toFragment) {
        if (mCurrFragment == null) {
            //showShortToast("mCurrFragment is null!");
            return;
        }

        if (toFragment == null) {
            //showShortToast("toFragment is null!");
            return;
        }

        if (toFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(mCurrFragment)
                    .show(toFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(mCurrFragment)
                    .add(R.id.container, toFragment).show(toFragment)
                    .commit();
        }
        mCurrFragment = toFragment;

    }

    private void setCurrFragment(Fragment mCurrFragment) {
        this.mCurrFragment = mCurrFragment;
    }

}
