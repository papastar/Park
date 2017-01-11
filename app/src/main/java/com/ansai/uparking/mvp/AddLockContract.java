package com.ansai.uparking.mvp;


import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.ansai.uparking.entity.body.SaveBody;

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
