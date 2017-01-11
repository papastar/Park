package com.ansai.uparking.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.litesuits.bluetooth.LiteBluetooth;
import com.ansai.libcommon.base.BaseFrameActivity;
import com.ansai.libcommon.util.ToastUtils;
import com.ansai.uparking.R;
import com.ansai.uparking.ble.BleUtil;
import com.ansai.uparking.ble.iBeaconClass;
import com.ansai.uparking.data.BleManager;
import com.ansai.uparking.entity.bean.BleBean;
import com.ansai.uparking.mvp.SearchLockContract;
import com.ansai.uparking.mvp.model.SearchLockModel;
import com.ansai.uparking.mvp.presenter.SearchLockPresenter;
import com.ansai.uparking.ui.view.SearchDeviceView;
import com.ansai.uparking.utils.KeyConstant;
import com.polidea.rxandroidble.RxBleScanResult;

import butterknife.Bind;

public class SearchLockActivity extends BaseFrameActivity<SearchLockPresenter, SearchLockModel>
        implements SearchLockContract.View, AdapterView.OnItemClickListener {

    @Bind(R.id.toolBar)
    Toolbar toolbar;
    @Bind(R.id.scan_view)
    SearchDeviceView scanView;
    @Bind(R.id.list_view)
    ListView listView;


    QuickAdapter<BleBean> mAdapter;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_lock;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(toolbar, "搜索车锁");
        mAdapter = new QuickAdapter<BleBean>(this, R.layout.scan_result_list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, BleBean item) {
                helper.setText(R.id.locker_name_tv, TextUtils.isEmpty(item.getDevice().getName()) ?
                        "未知设备" : item.getDevice().getName());
                helper.setText(R.id.rssi_tv, String.valueOf(item.getRssi()));
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        scanView.setWillNotDraw(false);
        scanView.setSearching(true);


        startScan();
    }

    private void startScan() {
        LiteBluetooth liteBluetooth = BleManager.getInstance().getLiteBluetooth();
        if (!liteBluetooth.getBluetoothAdapter().isEnabled())
            liteBluetooth.enableBluetooth(this, 1);
        else
            mPresenter.startLiteScan();

    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public void onGetScanResult(RxBleScanResult data) {
//        if (data == null)
//            return;
//        iBeaconClass.iBeacon iBeacon = BleUtil.covert(data);
//        if (!BleUtil.filter(iBeacon))
//            return;
//
//        boolean contain = false;
//        for (int index = 0; index < mAdapter.getCount(); index++) {
//            if (TextUtils.equals(data.getBleDevice().getMacAddress(), mAdapter.getItem(index)
//                    .getBleDevice().getMacAddress())) {
//                mAdapter.set(index, data);
//                contain = true;
//            }
//        }
//        if (!contain)
//            mAdapter.addSubscription(data);
    }

    @Override
    public void onGetScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device == null)
            return;
        iBeaconClass.iBeacon iBeacon = BleUtil.covert(device, rssi, scanRecord);
        if (!BleUtil.filter(iBeacon))
            return;
        BleBean bleBean = new BleBean(device, rssi, scanRecord);
        boolean contain = false;
        for (int index = 0; index < mAdapter.getCount(); index++) {
            if (TextUtils.equals(device.getAddress(), mAdapter.getItem(index).getDevice()
                    .getAddress())) {
                mAdapter.set(index, bleBean);
                contain = true;
            }
        }
        if (!contain)
            mAdapter.add(bleBean);
    }

    @Override
    public void onScanTimeOut() {
        ToastUtils.getInstance().showToast("扫描超时");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        iBeaconClass.iBeacon device = BleUtil.covert(mAdapter.getItem(position));
        String dataBlue = device.proximityUuid;
        if (dataBlue.length() >= 60)
        //&& (dataBlue.substring(10, 18)).equals("4a4f594f"))
        {
            String softWare = dataBlue.substring(54, 56);
            String hardWare = dataBlue.substring(52, 54);
            Intent data = new Intent();
            data.putExtra(KeyConstant.KEY_MAC_ADDRESS, device.bluetoothAddress);
            data.putExtra(KeyConstant.KEY_NAME, device.name);
            data.putExtra(KeyConstant.KEY_SOFTWARE, softWare);
            data.putExtra(KeyConstant.KEY_HARDWARE, hardWare);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            mPresenter.startLiteScan();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stopLiteScan();
    }
}
