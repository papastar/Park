package com.papa.libcommon.mvp;

import android.content.Context;

import com.papa.libcommon.rx.RxManager;
import com.papa.libcommon.rx.RxSchedulers;

import rx.Observable;
import rx.Subscriber;


/**
 * Created by baixiaokang on 16/4/22.
 */
public abstract class BasePresenter<M, V> {
    public Context context;
    public M mModel;
    public V mView;
    private RxManager mRxManager = new RxManager();

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
    }


    protected <T> void addSubscription(Observable<T> observable, Subscriber subscriber) {
        mRxManager.add(observable.compose(RxSchedulers.io_main()).subscribe(subscriber));
    }


    public void onDestroy() {
        mRxManager.clear();
    }
}
