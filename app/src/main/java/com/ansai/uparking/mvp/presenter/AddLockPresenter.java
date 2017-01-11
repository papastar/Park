package com.ansai.uparking.mvp.presenter;

import com.ansai.uparking.api.BaseViewApiCallback;
import com.ansai.uparking.api.SubscriberCallBack;
import com.ansai.uparking.data.DbManager;
import com.ansai.uparking.entity.body.SaveBody;
import com.ansai.uparking.mvp.AddLockContract;

/**
 * User: PAPA
 * Date: 2016-11-14
 */

public class AddLockPresenter extends AddLockContract.Presenter {
    @Override
    public void checkLockState(String bleAddress, String bleName) {
        addSubscription(mModel.checkLockState(bleAddress, bleName), new
                SubscriberCallBack<>(new BaseViewApiCallback<String>(mView) {
            @Override
            public void onSuccess(String data) {
                mView.onGetLockState(data);
            }
        }));
    }

    @Override
    public void save(SaveBody body) {
        boolean isOwner = DbManager.getInstance().isOwnerUse(body.bluetooth);
        if (isOwner) {

        } else {
            addSubscription(mModel.saveLock(body), new
                    SubscriberCallBack<>(new BaseViewApiCallback<Integer>(mView) {
                @Override
                public void onSuccess(Integer data) {
                    mView.onSaveLockResult(data);
                }
            }));
        }
    }
}
