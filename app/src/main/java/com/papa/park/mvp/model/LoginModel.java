package com.papa.park.mvp.model;

import com.papa.park.api.HttpManager;
import com.papa.park.data.DbManager;
import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.LockerBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.body.LoginBody;
import com.papa.park.entity.database.BleData;
import com.papa.park.mvp.LoginContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<CodeBean> getCode(String cellphone) {
        return HttpManager.getInstance().getUserApi().getCode(cellphone);
    }

    @Override
    public Observable<UserInfo> login(String cellphone, String code) {
        return HttpManager.getInstance().getUserApi().login(new LoginBody(cellphone, code));
    }

    @Override
    public Observable<Integer> getAndSaveLock() {
        return HttpManager.getInstance().getLockerApi().getLocker().map(new Func1<List<LockerBean>,
                List<BleData>>() {
            @Override
            public List<BleData> call(List<LockerBean> lockerBean) {
                if (lockerBean != null) {
                    List<BleData> bleDataList = new ArrayList<BleData>(lockerBean.size());
                    boolean first = true;
                    for (LockerBean item : lockerBean) {
                        BleData data = DbManager.getInstance().covertLockBean(item);
                        if (first) {
                            data.isuse = "1";
                            first = false;
                        } else {
                            data.isuse = "0";
                        }
                        bleDataList.add(data);
                    }
                    return bleDataList;
                }
                return null;
            }
        }).map(new Func1<List<BleData>, Integer>() {
            @Override
            public Integer call(List<BleData> bleData) {
                if (bleData != null) {
                    return DbManager.getInstance().saveBleData(bleData);
                }
                return 0;
            }
        });
    }
}
