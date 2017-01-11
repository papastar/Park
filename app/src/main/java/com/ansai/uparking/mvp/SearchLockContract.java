package com.ansai.uparking.mvp;

import android.bluetooth.BluetoothDevice;

import com.litesuits.bluetooth.scan.PeriodScanCallback;
import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/13.
 */

public interface SearchLockContract {

    interface Model extends BaseModel {
        public Observable<RxBleScanResult> scanDevices();

        public void scanDevices(PeriodScanCallback callback);

        public void stopScan(PeriodScanCallback callback);

    }

    interface View extends BaseView {
        void onGetScanResult(RxBleScanResult iBeacon);
        void onGetScanResult(BluetoothDevice device, int rssi, byte[] scanRecord);
        void onScanTimeOut();
    }


    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void startScan();
        public abstract void startLiteScan();
        public abstract void stopLiteScan();
    }
}
