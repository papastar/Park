package com.papa.park.mvp;


import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;
import com.papa.park.entity.body.SaveBody;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-14
 */

public interface AddLockContract {

    interface Model extends BaseModel {
        public Observable<String> checkLockState(String bleAddress, String bleName);

        public Observable<Integer> saveLock(SaveBody body);
    }

    interface View extends BaseView {
        public void onGetLockState(String state);

        public void onSaveLockResult(Integer result);
    }


    abstract class Presenter extends BasePresenter<Model, View> {

        public abstract void checkLockState(String bleAddress, String bleName);

        public abstract void save(SaveBody body);

    }
}
