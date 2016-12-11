package com.papa.park.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.data.UserInfoManager;
import com.papa.park.entity.bean.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

public class AccountActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.name_tv)
    TextView mNameTv;
    @Bind(R.id.phone_tv)
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
