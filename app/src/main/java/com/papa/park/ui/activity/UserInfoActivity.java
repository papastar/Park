package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserInfoActivity extends BaseAppCompatActivity {


    @Bind(R.id.toolBar)
    Toolbar mToolBar;
    @Bind(R.id.head_img)
    ImageView mHeadImg;
    @Bind(R.id.name_tv)
    TextView mNameTv;
    @Bind(R.id.wallet_tv)
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
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
