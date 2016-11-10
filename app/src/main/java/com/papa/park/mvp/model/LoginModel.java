package com.papa.park.mvp.model;

import com.papa.park.api.HttpManager;
import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.UserInfo;
import com.papa.park.entity.body.LoginBody;
import com.papa.park.mvp.LoginContract;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public class LoginModel implements LoginContract.Model{

    @Override
    public Observable<CodeBean> getCode(String cellphone) {
        return HttpManager.getInstance().getUserApi().getCode(cellphone);
    }

    @Override
    public Observable<UserInfo> login(String cellphone, String code) {
        return HttpManager.getInstance().getUserApi().login(new LoginBody(cellphone,code));
    }
}
