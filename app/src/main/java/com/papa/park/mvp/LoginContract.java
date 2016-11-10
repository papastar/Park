package com.papa.park.mvp;

import com.papa.libcommon.mvp.BaseModel;
import com.papa.libcommon.mvp.BasePresenter;
import com.papa.libcommon.mvp.BaseView;
import com.papa.park.entity.bean.CodeBean;
import com.papa.park.entity.bean.UserInfo;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public interface LoginContract {

    interface Model extends BaseModel {
        Observable<CodeBean> getCode(String cellphone);

        Observable<UserInfo> login(String cellphone, String code);
    }

    interface View extends BaseView {
        void onGetCodeSuccess(CodeBean response);
        void onLoginSuccess(UserInfo userInfoEntity);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String cellPhone, String code);
        public abstract void getCode(String cellPhone);
    }

}
