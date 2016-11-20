package com.papa.park.data;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.papa.libcommon.util.AppUtils;
import com.papa.park.BuildConfig;
import com.papa.park.entity.bean.LockerBean;
import com.papa.park.entity.database.BleData;

import java.util.ArrayList;
import java.util.List;

/**
 * User: PAPA
 * Date: 2016-11-16
 */

public class DbManager {
    private static final String DB_NAME = "locker.db";
    private static DbManager instance = null;
    private LiteOrm mLiteOrm;

    private DbManager() {
        mLiteOrm = LiteOrm.newSingleInstance(AppUtils.getAppContext(), DB_NAME);
        if (BuildConfig.DEV_MODE)
            mLiteOrm.setDebugged(true);
    }

    public static DbManager getInstance() {
        synchronized (DbManager.class) {
            if (instance == null) {
                instance = new DbManager();
            }
        }
        return instance;
    }

    public LiteOrm getLiteOrm() {
        return mLiteOrm;
    }


    public int saveBleData(List<BleData> list) {
        return mLiteOrm.save(list);
    }

    public int saveBleData(BleData item) {
        return (int) mLiteOrm.save(item);
    }

    public BleData covertLockBean(LockerBean lockerBean) {
        BleData bleData = new BleData();
        bleData.lockId = lockerBean._id;
        bleData.blueName = lockerBean.bluetoothName;
        bleData.blueAddress = lockerBean.bluetooth;
        bleData.noteName = lockerBean.note;
        bleData.key = lockerBean.key;
        bleData.sn = lockerBean.sn;
        bleData.lockToken = lockerBean.token;
        bleData.openId = UserInfoManager.getInstance().getCellPhone();
        bleData.isOwner = "1";
        bleData.isuse = "1";
        bleData.lockLong = lockerBean.location.coordinates.get(0);
        bleData.lockLat = lockerBean.location.coordinates.get(1);
        bleData.lockAddress = lockerBean.address;
        bleData.cityName = lockerBean.parkingLot.cityName;
        bleData.parkingName = lockerBean.parkingLot.name;
        bleData.parkingAddress = lockerBean.parkingLot.address;
        bleData.cityCode = lockerBean.parkingLot.cityCode;
        bleData.betocellphone = lockerBean.owner;
        return bleData;
    }

    public boolean isOwnerUse(String blueAddress) {
        QueryBuilder queryBuilder = new QueryBuilder<BleData>(BleData.class);
        queryBuilder.whereEquals("blueAddress", blueAddress).whereAppendAnd().whereEquals
                ("isOwner", "1");
        return mLiteOrm.queryCount(queryBuilder) > 0;
    }

    public ArrayList<BleData> getBleData(String openId) {
        QueryBuilder<BleData> queryBuilder = new QueryBuilder<>(BleData.class);
        queryBuilder.whereEquals("openId", openId).whereAppendAnd().whereEquals
                ("isOwner", "1");
        return mLiteOrm.query(queryBuilder);
    }

}
