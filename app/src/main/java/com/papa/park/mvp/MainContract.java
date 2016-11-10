package com.papa.park.mvp;

import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public interface MainContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }


    abstract class MainPresenter extends BasePresenter<Model, View> {

    }
}
