package com.papa.park.mvp;


import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;
import com.papa.park.entity.bean.CodeBean;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-14
 */

public interface AddLockContract {

    interface Model extends BaseModel {
        Observable<CodeBean> checkLockState(String bleAddress, String bleName);
    }

    interface View extends BaseView {

    }


    abstract class Presenter extends BasePresenter<MainContract.Model, MainContract.View> {

        public abstract void checkLockState(String bleAddress, String bleName);

    }
}
