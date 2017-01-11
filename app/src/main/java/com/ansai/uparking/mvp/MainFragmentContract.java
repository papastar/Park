package com.ansai.uparking.mvp;

import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.ansai.uparking.entity.database.BleData;

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
