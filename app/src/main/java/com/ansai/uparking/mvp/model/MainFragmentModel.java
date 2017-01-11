package com.ansai.uparking.mvp.model;

import com.ansai.uparking.data.DbManager;
import com.ansai.uparking.data.UserInfoManager;
import com.ansai.uparking.entity.database.BleData;
import com.ansai.uparking.mvp.MainFragmentContract;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/13.
 */

public class MainFragmentModel implements MainFragmentContract.Model {


    @Override
    public BleData queryLockers() {
        ArrayList<BleData> list = DbManager.getInstance().getBleData(UserInfoManager.getInstance
                ().getCellPhone());
        if (list != null && !list.isEmpty())
            return list.get(0);
        return null;
    }
}
