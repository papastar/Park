package com.papa.park.mvp;

import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;
import com.papa.park.entity.database.BleData;

/**
 * Created by Administrator on 2016/11/13.
 */

public interface MainFragmentContract {

    public interface Model extends BaseModel {

        public BleData queryLockers();
    }

    public interface View extends BaseView {

    }


     abstract class Presenter extends BasePresenter<Model, View> {

         public abstract void checkLockers();

    }
}
