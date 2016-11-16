package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.data.LocationInfo;
import com.papa.park.data.LocationManager;
import com.papa.park.mvp.AddLockContract;
import com.papa.park.mvp.model.AddLockModel;
import com.papa.park.mvp.presenter.AddLockPresenter;
import com.papa.park.utils.JSONUtils;
import com.papa.park.utils.KeyConstant;

import butterknife.Bind;

/**
 *
 */
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
        LocationInfo locationInfo = LocationManager.getInstance().getLocationInfo();
        if (locationInfo != null) {
            mAddressNameTv.setText(locationInfo.getAddressInfo());
        }
        mPresenter.checkLockState(mBleAddress, mBleName);
    }

    private void initToolBar() {
        mToolBar.setTitle("添加车锁信息");
        mToolBar.inflateMenu(R.menu.menu_add_lock);
        mToolBar.setOnMenuItemClickListener(this);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

    /**
     * {"owner":"57ee0c9c7ad35e4b7673d6da","sn":"a107167f3e","bluetooth":"54:4A:16:35:74:89",
     * "bluetoothName":"UBO_544A16357489","token":"12302d5c1a12908f3d0f211657d0113e",
     * "key":"EbozdIyMFOApokVp","_id":"582b1c7556271a4e22ee8764"}
     */
    @Override
    public void onGetLockState(String state) {
        String token = JSONUtils.getString(state, "token", "");

        if (!TextUtils.isEmpty(token)) {

        } else {
            //车锁已经被绑定
            setResult(RESULT_OK);
        }
    }
}
