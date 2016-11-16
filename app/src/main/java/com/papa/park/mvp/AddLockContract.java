package com.papa.park.mvp;


import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-14
 */

public interface AddLockContract {

    interface Model extends BaseModel {
        public Observable<String> checkLockState(String bleAddress, String bleName);
    }

    interface View extends BaseView {
        public void onGetLockState(String state);
    }


    abstract class Presenter extends BasePresenter<Model, View> {

        public abstract void checkLockState(String bleAddress, String bleName);

        public abstract void save(String token, String bluetooth, String blueName, String
                lockAddress, String parkingName, String note);

    }
}
