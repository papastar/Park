package com.papa.park.mvp.model;

import com.papa.park.data.BleManager;
import com.papa.park.mvp.SearchLockContract;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/13.
 */

public class SearchLockModel implements SearchLockContract.Model {
    @Override
    public Observable<RxBleScanResult> scanDevices() {
        return BleManager.getInstance().getRxBleClient().scanBleDevices();
    }
}
