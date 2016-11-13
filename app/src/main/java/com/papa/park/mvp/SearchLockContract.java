package com.papa.park.mvp;

import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;
import com.polidea.rxandroidble.RxBleScanResult;

import rx.Observable;

/**
 * Created by Administrator on 2016/11/13.
 */

public interface SearchLockContract {

    interface Model extends BaseModel {
        public Observable<RxBleScanResult> scanDevices();

    }

    interface View extends BaseView {
        void onGetScanResult(RxBleScanResult iBeacon);
    }


    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void startScan();
    }
}
