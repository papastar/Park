package com.papa.park.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.papa.libcommon.base.BaseFrameActivity;
import com.papa.park.R;
import com.papa.park.ble.BleUtil;
import com.papa.park.ble.iBeaconClass;
import com.papa.park.mvp.SearchLockContract;
import com.papa.park.mvp.model.SearchLockModel;
import com.papa.park.mvp.presenter.SearchLockPresenter;
import com.papa.park.ui.view.SearchDeviceView;
import com.papa.park.utils.KeyConstant;
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


    QuickAdapter<RxBleScanResult> mAdapter;

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
        mAdapter = new QuickAdapter<RxBleScanResult>(this, R.layout.scan_result_list_item) {
            @Override
            protected void convert(BaseAdapterHelper helper, RxBleScanResult item) {
                helper.setText(R.id.name_tv, TextUtils.isEmpty(item.getBleDevice().getName()) ?
                        "未知设备" : item.getBleDevice().getName());
                helper.setText(R.id.rssi_tv, String.valueOf(item.getRssi()));
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        scanView.setWillNotDraw(false);
        scanView.setSearching(true);
        mPresenter.startScan();


    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public void onGetScanResult(RxBleScanResult data) {
        if (data == null)
            return;
        Log.d("RxBleScanResult", data.getBleDevice().getName());
        iBeaconClass.iBeacon iBeacon = BleUtil.covert(data);
        if (!BleUtil.filter(iBeacon))
            return;

        boolean contain = false;
        for (int index = 0; index < mAdapter.getCount(); index++) {
            if (TextUtils.equals(data.getBleDevice().getMacAddress(), mAdapter.getItem(index)
                    .getBleDevice().getMacAddress())) {
                mAdapter.set(index, data);
                contain = true;
            }
        }
        if (!contain)
            mAdapter.add(data);
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
}
