package com.papa.park.mvp.presenter;

import com.papa.park.api.ApiCallback;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.mvp.SearchLockContract;
import com.polidea.rxandroidble.RxBleScanResult;

/**
 * Created by Administrator on 2016/11/13.
 */

public class SearchLockPresenter extends SearchLockContract.Presenter {
    @Override
    public void startScan() {
        addSubscription(mModel.scanDevices(), new
                SubscriberCallBack<>(new ApiCallback<RxBleScanResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onFailure(int code, String message) {

            }

            @Override
            public void onSuccess(RxBleScanResult data) {
                mView.onGetScanResult(data);
            }
        }));
    }
}
