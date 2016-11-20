package com.papa.park.mvp.presenter;

import com.papa.park.entity.database.BleData;
import com.papa.park.mvp.MainFragmentContract;

/**
 * Created by Administrator on 2016/11/13.
 */

public class MainFragmentPresenter extends MainFragmentContract.Presenter {

    @Override
    public void checkLockers() {
        BleData bleData = mModel.queryLockers();
        if(bleData!=null){

        }

    }
}
