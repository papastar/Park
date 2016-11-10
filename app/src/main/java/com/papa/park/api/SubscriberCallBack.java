package com.papa.park.api;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class SubscriberCallBack<T> extends Subscriber<T> {

    private ApiCallback<T> mCallback;

    public SubscriberCallBack(ApiCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onCompleted() {
        mCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            mCallback.onFailure(code, msg);
        } else {
            mCallback.onFailure(0, e.getMessage());
        }
        mCallback.onCompleted();
    }

    @Override
    public void onNext(T t) {
        mCallback.onSuccess(t);
    }
}
