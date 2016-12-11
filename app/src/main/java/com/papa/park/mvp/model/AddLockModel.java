package com.papa.park.mvp.model;


import android.text.TextUtils;

import com.papa.park.api.ApiException;
import com.papa.park.api.BaiduConfig;
import com.papa.park.api.HttpManager;
import com.papa.park.data.DbManager;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.BaseBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.body.LockBody;
import com.papa.park.entity.body.SaveBody;
import com.papa.park.entity.database.BleData;
import com.papa.park.mvp.AddLockContract;
import com.papa.park.utils.JSONUtils;

import java.util.Map;

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
        return HttpManager.getInstance().getLockerApi().saveLock(body).flatMap(new Func1<String,
                Observable<BaseBean>>() {


            @Override
            public Observable<BaseBean> call(String s) {
                String lockerId = JSONUtils.getString(s, "_id", "");
                if (!TextUtils.isEmpty(lockerId)) {
                    saveDbData(body, lockerId);
                    return createPoiData(body, lockerId);
                }
                return Observable.error(new ApiException("保存失败"));
            }
        }).map(new Func1<BaseBean,
                Integer>() {
            @Override
            public Integer call(BaseBean bean) {
                return bean.status;
            }
        });
    }

    private Observable<BaseBean> createPoiData(final SaveBody body, String lockerId) {
        Map<String, String> commonParam = BaiduConfig.getCommonParam();
        commonParam.put("title", body.parkingName);
        commonParam.put("address", body.parkingAddress);
        commonParam.put("tags", "车位");
        commonParam.put("latitude", body.lockLat);
        commonParam.put("longitude", body.lockLng);
        commonParam.put("coord_type", "3");
        commonParam.put("lockerParkName", body.parkingName);
        commonParam.put("lockerId", lockerId);
        commonParam.put("lockerBlueAddress", body.bluetooth);
        commonParam.put("lockerBlueName", body.bluetoothName);
        commonParam.put("lockerKey", body.key);
        commonParam.put("lockerSn", body.sn);
        commonParam.put("lockerToken", body.lockerToken);
        UserInfo userInfo = UserInfoManager.getInstance().getUserInfo();
        commonParam.put("ownerId", userInfo._id);
        commonParam.put("ownerName", userInfo.name);
        commonParam.put("ownerPhone", userInfo.cellphone);
        commonParam.put("rentState", String.valueOf(0));
        return HttpManager.getInstance().getBaiduLBSApi().creatPoi(commonParam);
    }

    private int saveDbData(final SaveBody body, String lockerId) {
        BleData bleData = new BleData();
        bleData.lockId = lockerId;
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

}
