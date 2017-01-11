package com.ansai.uparking.mvp;

import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public interface MainContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }


    abstract class Presenter extends BasePresenter<Model, View> {

    }
}
