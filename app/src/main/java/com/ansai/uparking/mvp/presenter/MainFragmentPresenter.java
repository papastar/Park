package com.ansai.uparking.mvp.presenter;

import com.ansai.uparking.entity.database.BleData;
import com.ansai.uparking.mvp.MainFragmentContract;

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
