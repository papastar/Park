package com.papa.park.mvp.model;


import com.papa.park.api.HttpManager;
import com.papa.park.entity.body.LockBody;
import com.papa.park.mvp.AddLockContract;

import rx.Observable;

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
}
