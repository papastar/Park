package com.ansai.uparking.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ansai.libcommon.base.BaseAppCompatActivity;
import com.ansai.uparking.R;
import com.ansai.uparking.data.UserInfoManager;
import com.ansai.uparking.entity.bean.UserInfo;

import butterknife.Bind;
import butterknife.OnClick;

import static com.ansai.uparking.R.id.wallet_tv;

public class UserInfoActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.head_img)
    ImageView mHeadImg;
    @Bind(R.id.locker_name_tv)
    TextView mNameTv;
    @Bind(wallet_tv)
    TextView mWalletTv;
    @Bind(R.id.locker_tv)
    TextView mLockerTv;
    @Bind(R.id.rent_record_tv)
    TextView mRentRecordTv;
    @Bind(R.id.version_tv)
    TextView mVersionTv;
    @Bind(R.id.about_tv)
    TextView mAboutTv;
    @Bind(R.id.driver_tv)
    TextView mDriverTv;
    @Bind(R.id.life_tv)
    TextView mLifeTv;

    UserInfo mUserInfo;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "用户中心");
        mUserInfo = UserInfoManager.getInstance().getUserInfo();
        if (mUserInfo != null) {
            mNameTv.setText(mUserInfo.name);
            mWalletTv.setText(getString(R.string.yuan_of, mUserInfo.balance));
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick(R.id.account_layout)
    void onAccountClick() {
        readyGo(AccountActivity.class);
    }

}
