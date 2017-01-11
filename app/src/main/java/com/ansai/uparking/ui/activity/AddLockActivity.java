package com.ansai.uparking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ansai.libcommon.base.BaseFrameActivity;
import com.ansai.uparking.R;
import com.ansai.uparking.data.LocationInfo;
import com.ansai.uparking.data.LocationManager;
import com.ansai.uparking.entity.body.SaveBody;
import com.ansai.uparking.mvp.AddLockContract;
import com.ansai.uparking.mvp.model.AddLockModel;
import com.ansai.uparking.mvp.presenter.AddLockPresenter;
import com.ansai.uparking.utils.JSONUtils;
import com.ansai.uparking.utils.KeyConstant;

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
    private String mHardware = "";

    private String mInfo;

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
//        mHardware = Integer.parseInt(getIntent().getStringExtra("hardware"), 16) + "";
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
        mPresenter.save(createSaveBody(mInfo));
    }

    /**
     * {"owner":"57ee0c9c7ad35e4b7673d6da","sn":"a107167f3e","bluetooth":"54:4A:16:35:74:89",
     * "bluetoothName":"UBO_544A16357489","token":"12302d5c1a12908f3d0f211657d0113e",
     * "key":"EbozdIyMFOApokVp","_id":"582b1c7556271a4e22ee8764"}
     */
    @Override
    public void onGetLockState(String state) {
        mInfo = state;
        String token = JSONUtils.getString(state, "token", "");
        if (TextUtils.isEmpty(token)) {
            if (TextUtils.equals(state, "There")) {
                showToast("车锁已绑定");
            } else {
                showToast("操作失败");
            }
            Intent intent = new Intent();
            intent.putExtra(KeyConstant.KEY_DATA, Integer.valueOf(0));
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onSaveLockResult(Integer result) {
        Intent intent = new Intent();
        if (result == 0) {
            showToast("添加成功");
            setResult(RESULT_OK, intent);
        } else {
            showToast("添加失败");
            setResult(RESULT_OK, intent);
        }
    }

    /**
     * {"owner":"57ee0c9c7ad35e4b7673d6da","sn":"a107167f3e","bluetooth":"54:4A:16:35:74:89",
     * "bluetoothName":"UBO_544A16357489","token":"12302d5c1a12908f3d0f211657d0113e",
     * "key":"EbozdIyMFOApokVp","_id":"582b1c7556271a4e22ee8764"}
     */
    private SaveBody createSaveBody(String info) {
        LocationInfo locationInfo = LocationManager.getInstance().getLocationInfo();
        SaveBody body = new SaveBody();
        body.id = JSONUtils.getString(info,"_id","");
        body.lockerToken = JSONUtils.getString(info,"token","");
        body.bluetooth = JSONUtils.getString(info,"bluetooth","");
        body.bluetoothName =  JSONUtils.getString(info,"bluetoothName","");
        body.note = mNoteEdit.getEditableText().toString();
        body.sn = JSONUtils.getString(info,"sn","");
        body.lockLat = String.valueOf(locationInfo.getLatitude());
        body.lockLng = String.valueOf(locationInfo.getLongitude());
        body.lockAddress = mParkingNoEdit.getEditableText().toString();
        body.cityCode = locationInfo.getCityCode();
        body.cityName = locationInfo.getCityName();
        body.parkingName = mParkingNameEdit.getEditableText().toString();
        body.parkingAddress = locationInfo.getAddressInfo();
        body.key =  JSONUtils.getString(info,"key","");
        body.hardware = mHardware;
        return body;
    }

}
