package com.papa.park.api;

import com.papa.libcommon.mvp.BaseView;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public abstract class BaseViewApiCallback<T> implements ApiCallback<T> {

    private BaseView mBaseView;

    public BaseViewApiCallback(BaseView baseView) {
        mBaseView = baseView;
        mBaseView.onRequestStart();
    }

    @Override
    public void onCompleted() {
        mBaseView.onRequestEnd();
    }

    @Override
    public void onFailure(int code, String message) {
        mBaseView.onRequestError(message);
    }


}
