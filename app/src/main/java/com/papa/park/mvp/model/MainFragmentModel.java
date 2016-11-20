package com.papa.park.mvp.model;

import com.papa.park.data.DbManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.database.BleData;
import com.papa.park.mvp.MainFragmentContract;

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
