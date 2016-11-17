package com.papa.libcommon.mvp;

import android.content.Context;

import com.papa.libcommon.rx.RxManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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


    protected <T> void addSubscription(Observable<T> observable, Subscriber<T> subscriber) {
        mRxManager.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }


    public void onDestroy() {
        mRxManager.clear();
    }
}
