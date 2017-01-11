package com.ansai.uparking.mvp.presenter;

import com.ansai.uparking.api.BaseViewApiCallback;
import com.ansai.uparking.api.SubscriberCallBack;
import com.ansai.uparking.entity.bean.CodeBean;
import com.ansai.uparking.entity.bean.UserInfo;
import com.ansai.uparking.mvp.LoginContract;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void login(String cellPhone, String code) {
        addSubscription(mModel.login(cellPhone, code), new
                SubscriberCallBack<>(new BaseViewApiCallback<UserInfo>(mView) {
            @Override
            public void onSuccess(UserInfo data) {
                mView.onLoginSuccess(data);
            }
        }));
    }

    @Override
    public void getCode(String cellPhone) {
        addSubscription(mModel.getCode(cellPhone), new SubscriberCallBack<>(new BaseViewApiCallback<CodeBean>(mView) {
            @Override
            public void onSuccess(CodeBean data) {
                mView.onGetCodeSuccess(data);
            }
        }));
    }

    @Override
    public void getAndSaveLock() {
        addSubscription(mModel.getAndSaveLock(), new SubscriberCallBack<>(new BaseViewApiCallback<Integer>(mView) {
            @Override
            public void onSuccess(Integer data) {
                mView.onGetAndSaveLockComplete();
            }
        }));
    }
}
