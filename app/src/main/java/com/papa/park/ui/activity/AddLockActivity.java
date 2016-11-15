package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.mvp.AddLockContract;
import com.papa.park.mvp.model.AddLockModel;
import com.papa.park.mvp.presenter.AddLockPresenter;
import com.papa.park.utils.KeyConstant;

import butterknife.Bind;

public class AddLockActivity extends BaseFrameActivity<AddLockPresenter, AddLockModel> implements
        AddLockContract.View, Toolbar.OnMenuItemClickListener {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.lock_name_tv)
    TextView mLockNameTv;
    @Bind(R.id.address_name_tv)
    TextView mAddressNameTv;
    @Bind(R.id.parking_name_edit)
    EditText mParkingNameEdit;
    @Bind(R.id.parking_no_edit)
    EditText mParkingNoEdit;
    @Bind(R.id.note_edit)
    EditText mNoteEdit;

    private String mBleAddress;
    private String mBleName;

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
        initIntent();
        mPresenter.checkLockState(mBleAddress, mBleName);
    }

    private void initToolBar() {
        setToolbar(mToolBar, "添加车锁信息");
        mToolBar.inflateMenu(R.menu.menu_add_lock);
        mToolBar.setOnMenuItemClickListener(this);
    }


    private void initIntent() {
        mBleAddress = getIntent().getStringExtra(KeyConstant.KEY_MAC_ADDRESS);
        mBleName = getIntent().getStringExtra(KeyConstant.KEY_NAME);
        mLockNameTv.setText(mBleName);
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
        }
        return false;
    }

    private void save() {

    }


    @Override
    public void onGetLockState(String state) {

    }
}
