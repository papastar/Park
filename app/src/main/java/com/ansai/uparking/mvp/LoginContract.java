package com.ansai.uparking.mvp;

import com.ansai.libcommon.mvp.BaseModel;
import com.ansai.libcommon.mvp.BasePresenter;
import com.ansai.libcommon.mvp.BaseView;
import com.ansai.uparking.entity.bean.CodeBean;
import com.ansai.uparking.entity.bean.UserInfo;

import rx.Observable;

/**
 * User: PAPA
 * Date: 2016-11-10
 */

public interface LoginContract {

    interface Model extends BaseModel {
        Observable<CodeBean> getCode(String cellphone);

        Observable<UserInfo> login(String cellphone, String code);

        Observable<Integer> getAndSaveLock();
    }

    interface View extends BaseView {
        void onGetCodeSuccess(CodeBean response);
        void onLoginSuccess(UserInfo userInfoEntity);
        void onGetAndSaveLockComplete();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String cellPhone, String code);
        public abstract void getCode(String cellPhone);
        public abstract void getAndSaveLock();

    }

}
