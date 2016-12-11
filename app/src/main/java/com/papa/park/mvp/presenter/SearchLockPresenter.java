package com.papa.park.mvp.presenter;

import android.bluetooth.BluetoothDevice;

import com.litesuits.bluetooth.scan.PeriodScanCallback;
import com.papa.park.api.ApiCallback;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.app.Config;
import com.papa.park.mvp.SearchLockContract;
import com.polidea.rxandroidble.RxBleScanResult;

/**
 * Created by Administrator on 2016/11/13.
 */

public class SearchLockPresenter extends SearchLockContract.Presenter {

    private PeriodScanCallback mCallBack = new PeriodScanCallback(Config.SCAN_TIME_OUT) {
        @Override
        public void onScanTimeout() {
            mView.onScanTimeOut();
        }

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            mView.onGetScanResult(device, rssi, scanRecord);
        }
    };

    @Override
    public void startScan() {
        addSubscription(mModel.scanDevices(), new
                SubscriberCallBack<>(new ApiCallback<RxBleScanResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message, Exception e) {

            }

            @Override
            public void onSuccess(RxBleScanResult data) {
                mView.onGetScanResult(data);
            }
        }));
    }

    @Override
    public void startLiteScan() {
        mModel.scanDevices(mCallBack);
    }

    @Override
    public void stopLiteScan() {
        mModel.stopScan(mCallBack);
    }


}
