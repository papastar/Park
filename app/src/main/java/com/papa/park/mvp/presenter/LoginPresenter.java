package com.papa.park.mvp.presenter;

import com.papa.park.api.BaseViewApiCallback;
import com.papa.park.api.SubscriberCallBack;
import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.mvp.LoginContract;

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
}
