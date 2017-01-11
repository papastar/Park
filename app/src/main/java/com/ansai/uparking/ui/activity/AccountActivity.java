package com.ansai.uparking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.uparking.R;
import com.ansai.uparking.data.UserInfoManager;
import com.ansai.uparking.entity.bean.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.locker_name_tv)
    TextView mNameTv;
    @Bind(R.id.locker_phone_tv)
    TextView mPhoneTv;

    UserInfo mUserInfo;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_account;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "账号管理");
        mUserInfo = UserInfoManager.getInstance().getUserInfo();
        if (mUserInfo != null) {
            mNameTv.setText(mUserInfo.name);
            mPhoneTv.setText(mUserInfo.cellphone);
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick(R.id.login_out_btn)
    void onLoginOutClick(){
        UserInfoManager.getInstance().clearUser();
        Intent[] intents = new Intent[2];
        intents[0] = new Intent(this,MainActivity.class);
        intents[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intents[1] = new Intent(this,LoginActivity.class);
        startActivities(intents);
     }

}
