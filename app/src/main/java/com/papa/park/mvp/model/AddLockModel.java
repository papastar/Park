package com.papa.park.mvp.model;


import android.text.TextUtils;

import com.papa.park.api.HttpManager;
import com.papa.park.data.DbManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.body.LockBody;
import com.papa.park.entity.body.SaveBody;
import com.papa.park.entity.database.BleData;
import com.papa.park.mvp.AddLockContract;
import com.papa.park.utils.JSONUtils;

import rx.Observable;
import rx.functions.Func1;

/**
 * User: PAPA
 * Date: 2016-11-14
 */

public class AddLockModel implements AddLockContract.Model {
    @Override
    public Observable<String> checkLockState(String bleAddress, String bleName) {
        return HttpManager.getInstance().getLockerApi().checkLocker(new LockBody(bleAddress,
                bleName));
    }

    @Override
    public Observable<Integer> saveLock(final SaveBody body) {
        return HttpManager.getInstance().getLockerApi().saveLock(body).map(new Func1<String,
                Integer>() {
            @Override
            public Integer call(String s) {
                String lockId = JSONUtils.getString(s, "_id", "");
                if (!TextUtils.isEmpty(lockId)) {
                    BleData bleData = new BleData();
                    bleData.lockId = lockId;
                    bleData.blueName = body.bluetoothName;
                    bleData.blueAddress = body.bluetooth;
                    bleData.noteName = body.note;
                    bleData.key = body.key;
                    bleData.sn = body.sn;
                    bleData.lockToken = body.lockerToken;
                    bleData.openId = UserInfoManager.getInstance().getCellPhone();
                    bleData.isOwner = "1";
                    bleData.isuse = "1";
                    bleData.lockLong = body.lockLng;
                    bleData.lockLat = body.lockLat;
                    bleData.lockAddress = body.lockAddress;
                    bleData.cityName = body.cityName;
                    bleData.parkingName = body.parkingName;
                    bleData.parkingAddress = body.parkingAddress;
                    bleData.cityCode = body.cityCode;
                    bleData.betocellphone = UserInfoManager.getInstance().getCellPhone();
                    return DbManager.getInstance().saveBleData(bleData);
                }
                return 0;
            }
        });
    }
}
